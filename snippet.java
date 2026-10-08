public abstract class snippet {
    private String description;
    private String codeText;

    public snippet(String description, String codeText) {
        this.description = description;
        this.codeText = codeText;
    }

    public String getDescription() {
        return description;
    }

    public String getCodeText() {
        return codeText;
    }

}
