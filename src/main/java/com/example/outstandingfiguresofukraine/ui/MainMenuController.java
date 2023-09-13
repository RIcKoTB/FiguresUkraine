package com.example.outstandingfiguresofukraine.ui;

import com.example.outstandingfiguresofukraine.HelloApplication;
import com.example.outstandingfiguresofukraine.da.HumansDAO;
import com.example.outstandingfiguresofukraine.da.UserDAO;
import com.example.outstandingfiguresofukraine.da.entity.Humans;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class MainMenuController {

    @FXML
    private AnchorPane adminPanelPage;

    @FXML
    private AnchorPane allChildPage;

    @FXML
    private Button btnAddHuman;

    @FXML
    public Button btnAdminPanel;

    @FXML
    private Button btnChildPage;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnSearchDatas;

    @FXML
    private Button btnSearchWord;

    @FXML
    private ImageView close;

    @FXML
    private ImageView imgHuman;

    @FXML
    private AnchorPane searchForDatePage;

    @FXML
    private Button btnAddPhoto;

    @FXML
    private AnchorPane searchWordPage;

    @FXML
    private TextField txtDataBirthd;

    @FXML
    private TextField txtMainInfo;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearchData;

    @FXML
    private TextField txtSearchWord;

    @FXML
    private VBox vboxAllHuman;

    @FXML
    private VBox vboxSearchDate;

    @FXML
    private VBox vboxSearchWord;

    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження виходу");
        alert.setHeaderText("Ви впевнені, що хочете вийти?");
        alert.setContentText("Увага: незбережені зміни можуть бути втрачені.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stg = (Stage) btnExit.getScene().getWindow();
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("Login.fxml"));
            stg.setTitle("Sign In");
            Scene scene = new Scene(root);
            stg.setScene(scene);
            stg.show();
        } else {
            // Користувач скасував виход, нічого не робимо
        }
    }

    @FXML
    void searchWord(ActionEvent event) throws SQLException, IOException {
        if(txtSearchWord.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка!");
            alert.setHeaderText("Заповніть поле пошуку");
            alert.showAndWait();
        }else {
            loadChildWord();
        }
    }

    private void loadChildWord() throws IOException, SQLException {
        vboxSearchWord.getChildren().clear();
        // Отримати список повідомлень для користувача
        String name = txtSearchWord.getText();

        List<Humans> humansList = humansDAO.getHumansByName(name);

        // Пройтися по списку повідомлень і додати їх до контейнера ззаду на перед
        for (Humans human : humansList) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("element-view.fxml"));
            Parent root = loader.load();
            ElementController elementController = loader.getController();

            String bio = human.getBio();
            String pib = human.getPib();
            String date = String.valueOf(human.getDate());
            Image photo = human.getPhoto();

            elementController.lblName.setText(pib);
            elementController.lblDate.setText(date);
            elementController.lblContent.setText(bio);
            elementController.imgHuman.setImage(photo);

            // Вставити елемент у початок списку повідомлень
            vboxSearchWord.getChildren().add(0, root);
        }
    }

    @FXML
    void searchData(ActionEvent event) throws SQLException, IOException {
        loadChildDate();
    }

    private void loadChildDate() throws IOException, SQLException {
        vboxSearchDate.getChildren().clear();
        // Отримати список повідомлень для користувача
        String dateSearch = txtSearchData.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate;

        try {
            utilDate = dateFormat.parse(dateSearch);
        } catch (ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка!");
            alert.setHeaderText("Некоректний формат дати");
            alert.setContentText("Будь ласка, введіть дату у форматі 'yyyy-MM-dd'");
            alert.showAndWait();
            return; // Перериваємо виконання методу, якщо дата введена некоректно
        }

        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        List<Humans> humansList = humansDAO.getHumansByDateOfBirth(sqlDate);

        // Пройтися по списку повідомлень і додати їх до контейнера ззаду на перед
        for (Humans human : humansList) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("element-view.fxml"));
            Parent root = loader.load();
            ElementController elementController = loader.getController();

            String bio = human.getBio();
            String pib = human.getPib();
            String date = String.valueOf(human.getDate());
            Image photo = human.getPhoto();

            elementController.lblName.setText(pib);
            elementController.lblDate.setText(date);
            elementController.lblContent.setText(bio);
            elementController.imgHuman.setImage(photo);

            // Вставити елемент у початок списку повідомлень
            vboxSearchDate.getChildren().add(0, root);
        }
    }

    @FXML
    void addPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a photo");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);

                // Конвертуємо BufferedImage в javafx.scene.image.Image
                WritableImage fxImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
                PixelWriter pixelWriter = fxImage.getPixelWriter();

                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    for (int x = 0; x < bufferedImage.getWidth(); x++) {
                        pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
                    }
                }

                // Встановлюємо зображення у imgFilmAdd
                imgHuman.setImage(fxImage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    HumansDAO humansDAO = new HumansDAO();

    @FXML
    void addHuman(ActionEvent event) throws ParseException, SQLException {
        String dateBir = txtDataBirthd.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate;

        try {
            utilDate = dateFormat.parse(dateBir);
        } catch (ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка!");
            alert.setHeaderText("Некоректний формат дати");
            alert.setContentText("Будь ласка, введіть дату у форматі 'yyyy-MM-dd'");
            alert.showAndWait();
            return; // Перериваємо виконання методу, якщо дата введена некоректно
        }


        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        String pib = txtName.getText();
        String bio = txtMainInfo.getText();
        Image photo = imgHuman.getImage();

        if(pib == null || bio.isEmpty() || photo == null || dateBir == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помилка!");
            alert.setHeaderText("Заповніть всі дані");
            alert.showAndWait();
        }else {
            Humans humans = new Humans(photo, pib, bio, sqlDate);
            humansDAO.createHuman(humans);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Вітаємо!");
            alert.setHeaderText("Ви успішно додали постать");
            alert.showAndWait();
        }
    }

    UserDAO userDAO = new UserDAO();

    @FXML
    void adminPanel(ActionEvent event) {

        if (userDAO.getRoleByUserId(userDAO.getLastUser()) == 1) {
            searchForDatePage.setVisible(false);
            searchWordPage.setVisible(false);
            adminPanelPage.setVisible(true);
            allChildPage.setVisible(false);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помилка!");
            alert.setHeaderText("Це вікно доступне тільки для адмінів");
            alert.showAndWait();
        }

    }

    @FXML
    void childPage(ActionEvent event) throws SQLException, IOException {
        searchForDatePage.setVisible(false);
        searchWordPage.setVisible(false);
        adminPanelPage.setVisible(false);
        allChildPage.setVisible(true);
        loadChild();
    }

    private void loadChild() throws IOException, SQLException {
        vboxAllHuman.getChildren().clear();
        // Отримати список повідомлень для користувача
        List<Humans> humansList = humansDAO.getAllHumans();

        // Пройтися по списку повідомлень і додати їх до контейнера ззаду на перед
        for (Humans human : humansList) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("element-view.fxml"));
            Parent root = loader.load();
            ElementController elementController = loader.getController();

            String bio = human.getBio();
            String pib = human.getPib();
            String date = String.valueOf(human.getDate());
            Image photo = human.getPhoto();

            elementController.lblName.setText(pib);
            elementController.lblDate.setText(date);
            elementController.lblContent.setText(bio);
            elementController.imgHuman.setImage(photo);

            // Вставити елемент у початок списку повідомлень
            vboxAllHuman.getChildren().add(0, root);
        }
    }



    @FXML
    void exitApplication(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void searchPage(ActionEvent event) {
        searchForDatePage.setVisible(false);
        searchWordPage.setVisible(true);
        adminPanelPage.setVisible(false);
        allChildPage.setVisible(false);
    }

    @FXML
    void searchDataPage(ActionEvent event) {
        searchForDatePage.setVisible(true);
        searchWordPage.setVisible(false);
        adminPanelPage.setVisible(false);
        allChildPage.setVisible(false);
    }

}
