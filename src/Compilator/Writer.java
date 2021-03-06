package Compilator;

import java.io.*;

public class Writer {
    public static Writer instance = null;
    private String destinationFile;
    private FileWriter fileWriter = null;

    private Writer(String destinationFile)
    {
        this.destinationFile = destinationFile;
        try {
            fileWriter = new FileWriter(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Writer getInstance()
    {
        return instance;
    }

    public static synchronized Writer createWriter(String destinationFile)
    {
        instance = new Writer(destinationFile);
        return instance;
    }

    public void write(String string, int incision)
    {
        try {
            for(int i = 0; i < incision; ++i)
                fileWriter.write("\t");
            fileWriter.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void error(String string)
    {
        write(string, 0);
        close();
    }

    public void close()
    {
        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.print("Can't save file: " + destinationFile);
        }
    }

}
