package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Date;
// Клас-контролер вікна "Роботи"
public class worksController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button abonents;

    @FXML
    private Button addcrashbutton;

    @FXML
    private Button addworkbutton;

    @FXML
    private Label answer;

    @FXML
    private Label answer1;

    @FXML
    private Button contracts;

    @FXML
    private TextField crashadressfield;

    @FXML
    private TextField crashtypefield;

    @FXML
    private Button findbyadressbutton;

    @FXML
    private Button findbydatebutton;

    @FXML
    private TextField workadressfield;

    @FXML
    private TextField workdateadd;

    @FXML
    private TextField workdatefield;

    @FXML
    private TextField worktypefield;

    @FXML
    void initialize() {
        final dbhandler handler = new dbhandler();
// Виведення номери бригади за датою роботи після натискання кнопки
        findbydatebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String date = workdatefield.getText();
                String result = null;
                try {

                    result = handler.findbrigadebydate(date);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                answer1.setText(result);
            }
        });
// Виведення номеру бригади за адресою аварії після натискання кнопки
        findbyadressbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String adress = workadressfield.getText();
                String result = null;
                try {
                    result = handler.findbrigadebyadress(adress);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                answer.setText(result);


            }
        });
// Переадресація на вікно "Договори"
        contracts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(worksController.this.getClass().getResource("contracts.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Контракти");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
                stage.setResizable(false);

                contracts.getScene().getWindow().hide();
            }
        });

// Переадресація на вікно "Абоненти"
        abonents.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(worksController.this.getClass().getResource("abonents.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Абоненти");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
                stage.setResizable(false);

                abonents.getScene().getWindow().hide();
            }
        });
        // Додання нової аваріх до системи після натиску кнопки
        addcrashbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String type = crashtypefield.getText().trim();
                String adress = crashadressfield.getText();
                try {
                    if(type != "" && adress != "") {
                        handler.addcrash(type, adress);

                    }
                    crashadressfield.setText("");
                    crashtypefield.setText("");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
// Додання нової роботи до системи за натисканням кнопки
        addworkbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String type = worktypefield.getText().trim();
                String date = workdateadd.getText();

                try {
                    if(type != "" && date != "") {
                        handler.addwork(type, date);
                    }

                    worktypefield.setText("");
                    workdateadd.setText("");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
