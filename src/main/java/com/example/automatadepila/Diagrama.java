package com.example.automatadepila;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;

public class Diagrama {

    // pad = Padding : Espacio extra entre el tamaño del canva y el cuadrado
    final double pad = 10;

    // wS = widthSquare : Ancho del Cuadrado
    // hS = heigthSquare : Alto del Cuadrado
    final double wS = 280;
    final double hS = 180;

    // cD = circleDiameter : Diámetro del círculo
    final double cD = 40;

    // wA = widthArc : Ancho del Arco
    // hA = heigthArc : Alto del Arco
    final double wA = 20;
    final double hA = 60;

    //xArrowS : Coordenada x del primer punto de la flecha del estado S
    //xArrowF : Coordenada x del primer punto de la flecha del estado F
    //yArrow  : Coordenada y del primer punto de las flechas
    final double xArrowS = pad + 9*cD/16 + 20;
    final double xArrowF = pad + wS - 7*cD/16 - 20;
    final double yArrow  = pad + hS/2 - 10*cD/16;

    private final GraphicsContext gc;

    public Diagrama(Canvas canva) {
        gc = canva.getGraphicsContext2D();
        drawDiagram();
    }

    private void drawDiagram(){
        //Dibuja el cuadrado principal
        gc.strokeRect(pad,pad,wS,hS);

        //Dibuja los circulos de los estados S y F
        drawStateS();
        drawStateF();

        //Dibuja las lineas de transición de los estados S, F y S-F
        drawTransitionLineS();
        drawTransitionLineF();
        drawTransitionStoF();

        //Escribe los textos de las transiciones
        writeOptionAStateS();
        writeOptionBStateS();
        writeOptionAStateF();
        writeOptionBStateF();
    }

    private void drawStateS() {
        //Dibuja el circulo del estado S
        gc.strokeOval(pad + 20,pad + hS/2 - cD/2,cD,cD);
        //Se alinea el texto al centro del circulo
        alignTextToCenter();
        //Texto del estado
        gc.fillText("S", pad + cD/2 + 20, pad + hS/2);
    }

    private void drawStateF() {
        //Dibuja los circulos del estado F
        gc.strokeOval(pad + wS - cD - 20, pad + hS/2 - cD/2, cD, cD);
        gc.strokeOval(pad + wS - cD - 18, pad + hS/2 - cD/2 + 2, cD - 4, cD - 4);
        //Se alinea el texto al centro del circulo
        alignTextToCenter();
        //Texto del estado
        gc.fillText("F", pad + wS - cD/2 - 20, pad + hS/2);
    }

    private void drawTransitionLineS() {
        //Linea
        gc.strokeArc(pad + 20 + cD/4,pad + hS/2 - cD, wA, hA, 14, 152, ArcType.OPEN);
        //Flecha
        gc.fillPolygon(new double[]{xArrowS, xArrowS + 14, xArrowS + 7}, new double[]{yArrow, yArrow, yArrow + 7}, 3);
    }

    private void drawTransitionLineF() {
        //Linea
        gc.strokeArc(pad + wS - 3*cD/4 - 20,pad + hS/2 - cD, wA, hA, 14, 152, ArcType.OPEN);
        //Flecha
        gc.fillPolygon(new double[]{xArrowF, xArrowF + 14, xArrowF + 7}, new double[]{yArrow, yArrow, yArrow + 7}, 3);
    }

    private void drawTransitionStoF() {
        //Linea
        gc.strokeLine(pad + cD + 20, pad + hS/2, pad + wS - cD - 20, pad + hS/2);
        //Flecha
        gc.fillPolygon(new double[]{pad + wS - cD - 20 -7, pad + wS - cD - 20 -7, pad + wS - cD - 20}, new double[]{pad + hS/2 - 7, pad + hS/2 + 7, pad + hS/2}, 3);
        //Texto
        alignTextToCenter();
        gc.fillText("\u03B5/\u03B5/\u03B5", pad + wS/2, pad + hS/2 - 10);
    }

    private void writeOptionAStateS() {
        alignTextDefault();
        gc.fillText("a/\u03B5/a",pad + 5, pad + hS/2 - 40 );
    }

    private void writeOptionBStateS() {
        alignTextDefault();
        gc.fillText("b/\u03B5/b",pad + 45, pad + hS/2 - 40 );
    }

    private void writeOptionAStateF() {
        alignTextDefault();
        gc.fillText("a/a/\u03B5",pad + wS - cD - 35, pad + hS/2 - 40 );
    }

    private void writeOptionBStateF() {
        alignTextDefault();
        gc.fillText("b/b/\u03B5",pad + wS - cD + 5, pad + hS/2 - 40 );
    }

    private void alignTextToCenter(){
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
    }

    private void alignTextDefault(){
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }
}
