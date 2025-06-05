package models;

public class User {
    public enum Role { MANAGER, CASHIER }

    private int id;
    private String name;
    private Role role;
    private String pwd;
    private String usrName;

    // Constructors
    public User(int id, String name, Role role, String pwd, String usrName) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.pwd = pwd;
        this.usrName = usrName;
    }

    public User(String name, Role role, String pwd, String usrName) {
        this(0, name, role, pwd, usrName); // id will be auto-generated (thanks mySql)
    }

    // Getters and setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public String getUsrName() { return usrName; }
    public void setUsrName(String usrName) { this.usrName = usrName; }
}
