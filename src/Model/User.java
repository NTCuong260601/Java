package Model;

public class User {

    private String userName, password, position;

    public User() {
    }

    public User(String userName, String password, String position) {
        this.userName = userName;
        this.password = password;
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", password=" + password + ", position=" + position + '}';
    }

}
