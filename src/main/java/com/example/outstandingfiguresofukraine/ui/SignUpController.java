package com.example.outstandingfiguresofukraine.ui;

import com.example.outstandingfiguresofukraine.HelloApplication;
import com.example.outstandingfiguresofukraine.da.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Контролер для вікна реєстрації.
 */
public class SignUpController {

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnSignUp;

    @FXML
    private ImageView close;

    @FXML
    private Label message;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private HBox warning;

    /**
     * Закриття додатка при натисканні на іконку "Закрити".
     *
     * @param event подія натискання мишею
     */
    @FXML
    void exitApplication(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Обробник натискання кнопки "Увійти".
     *
     * @param event подія натискання кнопки
     * @throws IOException викидається при помилці зчитування файлу FXML
     */
    @FXML
    void signIn(ActionEvent event) throws IOException {
        Stage stg = (Stage) btnSignIn.getScene().getWindow();
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("Login.fxml"));
        stg.setTitle("Sign In");
        Scene scene = new Scene(root);
        stg.setScene(scene);
        stg.show();
    }

    UserDAO userDAO = new UserDAO();

    /**
     * Обробник натискання кнопки "Зареєструватися".
     *
     * @param event подія натискання кнопки
     * @throws IOException викидається при помилці зчитування файлу FXML
     */
    @FXML
    void signUp(ActionEvent event) throws IOException, SQLException {
        String login = txtLogin.getText().trim();
        String password = txtPassword.getText().trim();
        if (!login.equals("") && !password.equals("")) {
            boolean result = userDAO.addUser(login, password);
            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Вітаємо!");
                alert.setHeaderText("Реєстрація успішна");
                alert.showAndWait();

                Stage stg = (Stage) btnSignUp.getScene().getWindow();
                Parent root = FXMLLoader.load(HelloApplication.class.getResource("Login.fxml"));
                stg.setTitle("Sign In");
                Scene scene = new Scene(root);
                stg.setScene(scene);
                stg.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Попередження!");
                alert.setHeaderText("Пароль не вірний");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Попередження!");
            alert.setHeaderText("Для реєстрації всі поля мають бути заповнені");
            alert.showAndWait();
        }
    }

}
