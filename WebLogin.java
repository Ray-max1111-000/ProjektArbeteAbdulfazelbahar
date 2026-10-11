public class WebLogin extends Credential {
    private String user;
    public WebLogin(String n, String u, String p) { super(n, p); this.user = u; }
    public String toCsv() { return "WEB," + name + "," + user + "," + pass; }
}
