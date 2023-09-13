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

/**
 * Контролер для вікна авторизації.
 */
public class LoginController {

    @FXML
    private Button btnLogin;

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

    UserDAO userDAO = new UserDAO();

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
        String login = txtLogin.getText().trim();
        String password = txtPassword.getText().trim();
        boolean result = userDAO.singIn(login, password);
        if (result) {
            if (userDAO.getRoleByUserId(userDAO.getLastUser()) == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Вітаємо!");
                alert.setHeaderText("Авторизація успішна");
                alert.setContentText("Ви авторизувалися як адмін");
                alert.showAndWait();

                Stage stg = (Stage) btnLogin.getScene().getWindow();
                Parent root = FXMLLoader.load(HelloApplication.class.getResource("mainMenu-view.fxml"));
                stg.setTitle("Login");

                Scene scene = new Scene(root);
                stg.setScene(scene);
                stg.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Вітаємо!");
                alert.setHeaderText("Авторизація успішна");
                alert.showAndWait();

                Parent root = FXMLLoader.load(HelloApplication.class.getResource("mainMenu-view.fxml"));
                Stage stg = (Stage) btnLogin.getScene().getWindow();
                stg.setTitle("Login");

                Scene scene = new Scene(root);
                stg.setScene(scene);
                stg.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помилка");
            alert.setHeaderText("Логін та/або пароль не вірні!");
            alert.showAndWait();
        }
    }


    /**
     * Обробник натискання кнопки "Зареєструватися".
     *
     * @param event подія натискання кнопки
     * @throws IOException викидається при помилці зчитування файлу FXML
     */
    @FXML
    void signUp(ActionEvent event) throws IOException {
        Stage stg = (Stage) btnSignUp.getScene().getWindow();
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("signup-view.fxml"));
        stg.setTitle("Sign Up");
        Scene scene = new Scene(root);
        stg.setScene(scene);
        stg.show();
    }
}
