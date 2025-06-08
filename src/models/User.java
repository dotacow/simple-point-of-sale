package models;

public class User {

    public enum Role {
        MANAGER, CASHIER
    }

    private int id;
    private String name;
    private Role role;
    private String pwd;
    private String usrName;

    // For creating
    public User(String name, Role role, String pwd, String usrName) {
        this.name = name;
        this.role = role;
        this.pwd = pwd;
        this.usrName = usrName;
    }

    // For getting and updating
    public User(int id, String name, Role role, String pwd, String usrName) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.pwd = pwd;
        this.usrName = usrName;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String pwd) {
        this.pwd = pwd;
    }

    public String getUsername() {
        return usrName;
    }

    public void setUsername(String usrName) {
        this.usrName = usrName;
    }

    public boolean isManager() {
        if (this.role == role.MANAGER) {
            return true;
        }
        return false;
    }
}
