package com.example.outstandingfiguresofukraine.da;

import com.example.outstandingfiguresofukraine.da.entity.Humans;
import com.example.outstandingfiguresofukraine.db.Connect;
import javafx.scene.image.Image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HumansDAO {
    private Connection connection = Connect.connect();

    // Метод для створення запису про людину
    public void createHuman(Humans human) throws SQLException {
        String sql = "INSERT INTO humans (photo, pib, bio, date_birthday) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            byte[] photoData = imageToByteArray(human.getPhoto());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            preparedStatement.setBinaryStream(1, inputStream, photoData.length);
            preparedStatement.setString(2, human.getPib());
            preparedStatement.setString(3, human.getBio());
            preparedStatement.setDate(4, human.getDate());
            preparedStatement.executeUpdate();
        }
    }

    public List<Humans> getHumansByName(String name) throws SQLException, IOException {
        List<Humans> humansList = new ArrayList<>();
        String sql = "SELECT * FROM humans WHERE pib LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Blob photoData = resultSet.getBlob("photo");
                    Image photo = convertBlobToImage(photoData);
                    String pib = resultSet.getString("pib");
                    String bio = resultSet.getString("bio");
                    java.sql.Date date = resultSet.getDate("date_birthday");
                    Humans human = new Humans(photo, pib, bio, date);
                    human.setId(id);
                    humansList.add(human);
                }
            }
        }
        return humansList; // Повертаємо список користувачів, знайдених за іменем
    }


    public List<Humans> getHumansByDateOfBirth(java.sql.Date dateOfBirth) throws SQLException, IOException {
        List<Humans> humansList = new ArrayList<>();
        String sql = "SELECT * FROM humans WHERE date_birthday = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, dateOfBirth);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Blob photoData = resultSet.getBlob("photo");
                    Image photo = convertBlobToImage(photoData);
                    String pib = resultSet.getString("pib");
                    String bio = resultSet.getString("bio");
                    java.sql.Date date = resultSet.getDate("date_birthday");
                    Humans human = new Humans(photo, pib, bio, date);
                    human.setId(id);
                    humansList.add(human);
                }
            }
        }
        return humansList; // Повертаємо список користувачів
    }


    private byte[] imageToByteArray(Image image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для отримання запису про людину за ідентифікатором
    public Humans getHumanById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM humans WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Blob photoData = resultSet.getBlob("photo");
                    Image photo = convertBlobToImage(photoData);
                    String pib = resultSet.getString("pib");
                    String bio = resultSet.getString("bio");
                    java.sql.Date date = resultSet.getDate("date_birthday");
                    Humans human = new Humans(photo, pib, bio, date);
                    human.setId(id);
                    return human;
                }
            }
        }
        return null; // Якщо запис не знайдено
    }

    /**
     * Конвертувати BLOB в об'єкт типу Image.
     *
     * @param blob BLOB-дані для конвертації
     * @return об'єкт типу Image
     * @throws SQLException виникає у разі помилок під час роботи з BLOB-даними або зображенням
     * @throws IOException  виникає у разі помилок під час роботи з потоками даних
     */
    public static Image convertBlobToImage(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null;
        }

        try (InputStream inputStream = blob.getBinaryStream()) {
            return new Image(inputStream);
        }
    }

    // Метод для отримання всіх записів про людей
    public List<Humans> getAllHumans() throws SQLException {
        List<Humans> humans = new ArrayList<>();
        String sql = "SELECT * FROM humans";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Blob photoData = resultSet.getBlob("photo");
                Image photo = convertBlobToImage(photoData);
                String pib = resultSet.getString("pib");
                String bio = resultSet.getString("bio");
                java.sql.Date date = resultSet.getDate("date_birthday");
                Humans human = new Humans(photo, pib, bio, date);
                human.setId(id);
                humans.add(human);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return humans;
    }

    // Метод для оновлення запису про людину
//    public void updateHuman(Humans human) throws SQLException {
//        String sql = "UPDATE humans SET photo = ?, pib = ?, bio = ?, date = ? WHERE id = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setBlob(1, human.getPhoto());
//            preparedStatement.setString(2, human.getPib());
//            preparedStatement.setString(3, human.getBio());
//            preparedStatement.setDate(4, human.getDate());
//            preparedStatement.setInt(5, human.getId());
//            preparedStatement.executeUpdate();
//        }
//    }

    // Метод для видалення запису про людину за ідентифікатором
    public void deleteHuman(int id) throws SQLException {
        String sql = "DELETE FROM humans WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
