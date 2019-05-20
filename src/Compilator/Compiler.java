package Compilator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Compiler {
    String sourcePath;
    String destinationPath;
    MyScanner myScanner;

    private void preparePaths(String sourcePath) {
        this.sourcePath = sourcePath;
        //prepare destination filename with localisation
        destinationPath = sourcePath.substring(0, sourcePath.lastIndexOf("."));
        StringBuilder stringBuilder = new StringBuilder(destinationPath);
        stringBuilder.append(".cpp");
        destinationPath = stringBuilder.toString();
    }

    private void copyLibraries() throws IOException {
        //copy cpp libraries
        StringBuilder sourceFolder = new StringBuilder(System.getProperty("user.dir"));
        sourceFolder.append("\\src\\CppLibraries\\");
        StringBuilder destFolder = new StringBuilder(sourcePath.substring(0, sourcePath.lastIndexOf("/")+1));

        File source = new File(sourceFolder.toString()+"Wallet.h");
        File dest = new File(destFolder.toString()+"Wallet.h");
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        source = new File(sourceFolder.toString()+"CurrenciesLibrary.h");
        dest = new File(destFolder.toString()+"CurrenciesLibrary.h");
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void compile(String path) throws Exception {
        //Prepare paths and add them to singleton object
        preparePaths(path);
        Writer.createWriter(destinationPath);
        myScanner = new MyScanner(sourcePath);
        Parser parser = new Parser(myScanner);
        Generator generator = new Generator(Writer.getInstance());
        //here the generators start the parser and it's own work, parser is starting lexer
        generator.generate(parser.program());
        Writer.getInstance().close();

        //copy libraries
        copyLibraries();
    }
}