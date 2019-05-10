package Compilator;

public class Symbol {
    private KeyWords.SymType type;
    private String text;

    public Symbol (KeyWords.SymType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Symbol (KeyWords.SymType type) {
        this.type = type;
        this.text = "";
    }

    public KeyWords.SymType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setType(KeyWords.SymType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
}
