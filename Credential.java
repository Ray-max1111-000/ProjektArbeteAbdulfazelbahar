public abstract class Credential {
    protected String name, pass;
    public Credential(String name, String pass) { this.name = name; this.pass = pass; }
    public abstract String toCsv();
    public abstract void print();
}
