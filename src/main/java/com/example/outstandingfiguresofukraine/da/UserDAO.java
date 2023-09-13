package com.example.outstandingfiguresofukraine.da;

import com.example.outstandingfiguresofukraine.db.Connect;
import com.example.outstandingfiguresofukraine.da.entity.Session;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Клас, який виконує операції з базою даних для сутності User.
 */
public class UserDAO {

    private Connection connection = Connect.connect();

    /**
     * Виконує процес входу користувача до системи.
     *
     * @param login    логін користувача
     * @param password пароль користувача
     * @return true, якщо вхід успішний, або false, якщо вхід неуспішний
     */
    public boolean singIn(String login, String password) {
        String SQL = """
                SELECT users.id,
                       users.login,
                       users.password
                FROM users
                WHERE users.login = ? 
                AND users.password = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                saveSession(new Session(
                        resultSet.getInt("id"),
                        LocalDateTime.now()
                ));
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    /**
     * Зберігає сеанс користувача в базі даних.
     *
     * @param session сеанс користувача
     */
    public void saveSession(Session session) {
        String SQL = """
                INSERT INTO session(user_id,date)
                VALUES (?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, session.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(session.getDate()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println("+");
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    /**
     * Отримує ідентифікатор останнього користувача з бази даних.
     *
     * @return ідентифікатор останнього користувача
     */
    public int getLastUser() {
        String SQL = """
                SELECT session.user_id
                FROM session
                WHERE session.date = (SELECT MAX(date) FROM `session`)
                """;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
        }
        return 0;
    }

    /**
     * Отримує ідентифікатор ролі користувача за його ідентифікатором.
     *
     * @param id ідентифікатор користувача
     * @return ідентифікатор ролі користувача
     */
    public int getRoleByUserId(int id) {
        String SQL = """
                SELECT users.role_id
                FROM users
                WHERE users.id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("users.role_id");
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return 0;
    }

    /**
     * Додає нового користувача до бази даних.
     *
     * @param login    логін користувача
     * @param password пароль користувача
     * @return true, якщо користувача було успішно додано, або false, якщо сталась помилка
     */
    public boolean addUser(String login, String password) {
        String SQL = """
                INSERT INTO users(login, password, role_id) 
                VALUES (?,?,2) 
                """;
        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new RuntimeException(throwables);
        }
    }
}
