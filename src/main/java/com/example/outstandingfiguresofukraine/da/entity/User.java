package com.example.outstandingfiguresofukraine.da.entity;


/**
 * Клас, що представляє користувача системи.
 */
public class User {
    int id;
    String login;
    String password;
    int roleID;

    /**
     * Конструктор класу User.
     *
     * @param id       Ідентифікатор користувача.
     * @param login    Логін користувача.
     * @param password Пароль користувача.
     * @param roleID     Роль користувача.
     */
    public User(int id, String login, String password, int roleID) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roleID = roleID;
    }

    public User(String login, String password, int roleID) {
        this.login = login;
        this.password = password;
        this.roleID = roleID;
    }

    /**
     * Отримує ідентифікатор користувача.
     *
     * @return Ідентифікатор користувача.
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор користувача.
     *
     * @param id Ідентифікатор користувача.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Отримує логін користувача.
     *
     * @return Логін користувача.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Встановлює логін користувача.
     *
     * @param login Логін користувача.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Отримує пароль користувача.
     *
     * @return Пароль користувача.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Встановлює пароль користувача.
     *
     * @param password Пароль користувача.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Отримує роль користувача.
     *
     * @return Роль користувача.
     */
    public int getRole() {
        return roleID;
    }

    /**
     * Встановлює роль користувача.
     *
     * @param role Роль користувача.
     */
    public void setRole(int roleId) {
        this.roleID = roleID;
    }

    /**
     * Перевизначений метод toString() для зручного відображення об'єкту User.
     *
     * @return Рядкове представлення об'єкту User.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}