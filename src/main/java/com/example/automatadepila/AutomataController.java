package com.example.automatadepila;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AutomataController implements Initializable {
    @FXML
    private TextField cadenaTextField;
    @FXML
    private Label resultLabel;
    @FXML
    private TableView<Estado> tableView;
    @FXML
    private TableColumn<Estado,String> estadoColumn;
    @FXML
    private TableColumn<Estado,String> cadenaColumn;
    @FXML
    private TableColumn<Estado,String> pilaColumn;

    private ObservableList<Estado> transitions;

    @FXML
    private Canvas canva;

    @FXML
    protected void onContinueButtonClick() {
        String cadena = cadenaTextField.getText();
        resultLabel.setText("");
        cadenaTextField.setText("");

        if (cadena.isEmpty()) {
            resultLabel.setText("La cadena está vacía");
            return;
        }

        for (char letra : cadena.toCharArray()) {
            if (letra != 'a' && letra != 'b') {
                resultLabel.setText("Solo se permite palabras con a y b");
                return;
            }
        }

        if (cadena.length() % 2 != 0) {
            resultLabel.setText("La cadena debe ser de longitud par");
            return;
        }

        automata(cadena);
    }

    private void automata(String cadena) {

        int length = cadena.length();
        String pila = "";

        transitions.clear();

        transitions.add(new Estado("S",cadena, "\u03B5"));

        for (int i = 0; i < length/2; i++) {

            if ( cadena.charAt(i) == 'a' ) {
                pila = "a".concat(pila);
            } else {
                pila = "b".concat(pila);
            }
            transitions.add(new Estado("S", cadena.substring(i + 1), pila));
        }

        transitions.add(new Estado("F", cadena.substring(length/2), pila));

        for (int i = length/2; i < length; i++) {
            if ( cadena.charAt(i) != pila.charAt(0) ) {
                resultLabel.setText("No es palindromo");
                return;
            }

            if (pila.length() > 1) {
                pila = pila.substring(1);
                transitions.add(new Estado("F", cadena.substring(i+1), pila));
            }
        }

        transitions.add(new Estado("F", "\u03B5", "\u03B5"));

        resultLabel.setText( "Es palindromo" );
    }

    private void initializeTable() {
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cadenaColumn.setCellValueFactory(new PropertyValueFactory<>("cadena"));
        pilaColumn.setCellValueFactory(new PropertyValueFactory<>("pila"));

        transitions = FXCollections.observableArrayList();
        tableView.setItems(transitions);
    }

    private void drawDiagram(){
        new Diagrama(canva);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        drawDiagram();
    }
}