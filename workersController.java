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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class workersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addbutton;

    @FXML
    private TextField addname;

    @FXML
    private TextField addposada;

    @FXML
    private TextField addsurname;

    @FXML
    private TextField addthird;

    @FXML
    private Label answerlabel;

    @FXML
    private Button contracts;

    @FXML
    private Label deleteanswer;

    @FXML
    private Button deletebutton;

    @FXML
    private TextField deletefield;

    @FXML
    private Label resultlabel;

    @FXML
    private Button robotas;

    @FXML
    private Button searchbutton;

    @FXML
    private Tab searchtab;

    @FXML
    private TextField searchworker;

    @FXML
    private Button workers;

    @FXML
    private TextField addworkstart;

    @FXML
    private Button abonents;


    @FXML
    void initialize() {
        final dbhandler hand = new dbhandler();

        robotas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(workersController.this.getClass().getResource("admin_works.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Роботи та аварії");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
                stage.setResizable(false);

                robotas.getScene().getWindow().hide();
            }
        });

        contracts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(workersController.this.getClass().getResource("admin_contracts.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Роботи та аварії");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
                stage.setResizable(false);

                robotas.getScene().getWindow().hide();
            }
        });

        abonents.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(workersController.this.getClass().getResource("admin_abonents.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Роботи та аварії");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
                stage.setResizable(false);

                robotas.getScene().getWindow().hide();
            }
        });

        searchbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String surname = searchworker.getText().trim();
                try {
                    if(surname != "") {
                        String result = hand.findworker(surname);
                        resultlabel.setText(result);
                    } else {
                        resultlabel.setText("Заповніть поле");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        addbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = addname.getText().trim();
                String surname = addsurname.getText().trim();
                String third = addthird.getText().trim();
                String workstart = addworkstart.getText().trim();
                String posada = addposada.getText().trim();
                try {
                    if(name != "" && surname != "" && third != "" && workstart != "" && posada != "") {
                        hand.addworker(name, surname, third, posada, workstart);
                        answerlabel.setText("Працівник доданий до системи");
                    } else {
                        answerlabel.setText("Заповніть усі поля");
                    }

                    addname.setText("");
                    addsurname.setText("");
                    addthird.setText("");
                    addposada.setText("");
                    addworkstart.setText("");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        deletebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String surname = deletefield.getText().trim();
                try {
                    if(surname != "") {
                        hand.deleteworker(surname);
                        deleteanswer.setText("Працівник видалений");
                    } else {
                         deleteanswer.setText("Заповніть поле");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
