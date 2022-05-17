package sample;
// Клас користувача системи

public class User {
    private String name; // ім'я користувача
    private String surname; // прізвище
    private String login;  // логін
    private String password; // пароль
    private String isadmin; // чи має користувач права адміна

    public User(String name, String surname, String login, String password, String isadmin) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.isadmin = isadmin;
    }

    public User() {

    }
// Сетери та Гетери
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }
}
