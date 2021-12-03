package com.example.automatadepila;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
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
    private Button outButton;

    private Diagrama diagrama;

    @FXML
    protected void onContinueButtonClick() {

        String cadena = cadenaTextField.getText();
        resultLabel.setText("");
        cadenaTextField.setText("");

        cadena = cadena.toLowerCase(Locale.ROOT);

        if (cadena.isEmpty()) {
            emptyEntry();
            return;
        }

        for (char letra : cadena.toCharArray()) {
            if (letra != 'a' && letra != 'b') {
                resultLabel.setText("Solo se permite palabras con a y b");
                stopAnimation();
                transitions.clear();
                return;
            }
        }

        if (cadena.length() % 2 != 0) {
            resultLabel.setText("La cadena debe ser de longitud par");
            stopAnimation();
            transitions.clear();
            return;
        }

        addTransitionsToTable(cadena);
    }

    private void stopAnimation() {
        if (diagrama != null)
            diagrama.stopAnimation();
    }

    private void emptyEntry() { //La cadena vacía cuenta como palindromo
        transitions.clear();
        transitions.add(new Estado("S","\u03B5","\u03B5"));
        transitions.add(new Estado("F","\u03B5","\u03B5"));
        resultLabel.setText("Es palíndromo");
        animateDiagram();
    }

    private void addTransitionsToTable(String cadena) {

        boolean isPalindrome = true;
        int length = cadena.length();
        String pila = "";

        //Liberamos la tabla de valores previos
        transitions.clear();

        //Añadimos el estado inicial del autómata
        transitions.add(new Estado("S",cadena, "\u03B5"));

        //Añadimos las transiciones del estado S
        for (int i = 0; i < length/2; i++) {

            if ( cadena.charAt(i) == 'a' ) {
                pila = "a".concat(pila);
            } else {
                pila = "b".concat(pila);
            }
            transitions.add(new Estado("S", cadena.substring(i + 1), pila));
        }

        //Añadimos la transición de S a F
        transitions.add(new Estado("F", cadena.substring(length/2), pila));

        //Añadimos las transiciones del estado F
        for (int i = length/2; i < length; i++) {
            if ( cadena.charAt(i) != pila.charAt(0) ) {
                isPalindrome = false;
                pila = cadena.charAt(i) + pila;
            }

            pila = pila.substring(1);

            if (cadena.substring(i+1).length() > 0) {
                transitions.add(
                        new Estado(
                                "F",
                                cadena.substring(i+1),
                                pila
                        )
                );
            } else {
                transitions.add(
                        new Estado(
                                "F",
                                "\u03B5",
                                pila.isBlank() ? "\u03B5" : pila
                        )
                );
            }
        }

        if (isPalindrome)
            resultLabel.setText( "Es palíndromo" );
        else
            resultLabel.setText("No es palíndromo");

        animateDiagram();
    }

    private void initializeTable() {

        //Al chile no sé para que sirve esto, pero si se lo quito no se ven las palabras en la tabla
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cadenaColumn.setCellValueFactory(new PropertyValueFactory<>("cadena"));
        pilaColumn.setCellValueFactory(new PropertyValueFactory<>("pila"));
        //Solo sé que las instancias del PropertyValueFactory tienen en el constructor los identificadores de las
        //propiedades de la clase Estado

        //Se añade la lista observable de los estados a la tabla
        transitions = FXCollections.observableArrayList();
        tableView.setItems(transitions);
    }

    private void drawDiagram(){
        diagrama = new Diagrama(canva);
    }

    private void animateDiagram() {
        diagrama.animateTransitions(transitions.stream().toList());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        drawDiagram();
    }

    @FXML
    public void onOutButtonClick(){
        Stage stage = (Stage) outButton.getScene().getWindow();
        stage.close();
    }
}