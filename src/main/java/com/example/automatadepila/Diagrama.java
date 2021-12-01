package com.example.automatadepila;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.List;

public class Diagrama {

    private enum Eraser { LECTURE, PILE, LECTURE_AND_PILE, STATE_S, STATE_F, TRANSITION_A_S, TRANSITION_B_S, TRANSITION_A_F, TRANSITION_B_F, EMPTY_TRANSITION, NULL}

    // pad = Padding : Espacio extra entre el tamaño del canva y el cuadrado
    private final double pad = 0;

    // wS = widthSquare : Ancho del Cuadrado
    // hS = heigthSquare : Alto del Cuadrado
    private final double wS = 300;
    private final double hS = 200;

    // cD = circleDiameter : Diámetro del círculo
    private final double cD = 40;

    // wA = widthArc : Ancho del Arco
    // hA = heigthArc : Alto del Arco
    private final double wA = 20;
    private final double hA = 60;

    //yArrow  : Coordenada y del primer punto de las flechas
    private final double yArrow  = pad + hS/2 - 10*cD/16;

    //Contador de transiciones en el timeline
    private int counter = 0;
    private Eraser lastTransition = Eraser.NULL;

    private final GraphicsContext gc;

    private Timeline timeline;

    public Diagrama(Canvas canva) {
        gc = canva.getGraphicsContext2D();
        drawDiagram();
    }

    private void drawDiagram(){
        //Dibuja el cuadrado principal
        gc.strokeRect(pad,pad,wS,hS);
//        gc.fillRect(pad,pad, wS, hS);

        //Dibuja los circulos de los estados S y F
        drawStateS(Color.BLACK);
        drawStateF(Color.BLACK);

        //Dibuja las lineas de transición de los estados S, F y S-F
        drawTransitionLineS(Color.BLACK);
        drawTransitionLineF(Color.BLACK);
        drawTransitionStoF(Color.BLACK);

        //Escribe los textos de las transiciones
        writeOptionAStateS(Color.BLACK);
        writeOptionBStateS(Color.BLACK);
        writeOptionAStateF(Color.BLACK);
        writeOptionBStateF(Color.BLACK);
    }

    private void drawStateS(Paint paint) {
        changeColor(paint);
        //Dibuja el circulo del estado S
        gc.strokeOval(pad + 20,pad + hS/2 - cD/2,cD,cD);
        //Se alinea el texto al centro del circulo
        alignTextToCenter();
        //Texto del estado
        gc.fillText("S", pad + cD/2 + 20, pad + hS/2);
    }

    private void drawStateF(Paint paint) {
        changeColor(paint);
        //Dibuja los circulos del estado F
        gc.strokeOval(pad + wS - cD - 20, pad + hS/2 - cD/2, cD, cD);
        gc.strokeOval(pad + wS - cD - 18, pad + hS/2 - cD/2 + 2, cD - 4, cD - 4);
        //Se alinea el texto al centro del circulo
        alignTextToCenter();
        //Texto del estado
        gc.fillText("F", pad + wS - cD/2 - 20, pad + hS/2);
    }

    private void drawTransitionLineS(Paint paint) {

        //xArrowS : Coordenada x del primer punto de la flecha del estado S
        double xArrowS = pad + 9 * cD / 16 + 20;

        changeColor(paint);

        //Linea
        gc.strokeArc(pad + 20 + cD/4,pad + hS/2 - cD, wA, hA, 14, 152, ArcType.OPEN);
        //Flecha
        gc.fillPolygon(new double[]{xArrowS, xArrowS + 14, xArrowS + 7}, new double[]{yArrow, yArrow, yArrow + 7}, 3);
    }

    private void drawTransitionLineF(Paint paint) {

        //xArrowF : Coordenada x del primer punto de la flecha del estado F
        double xArrowF = pad + wS - 7 * cD / 16 - 20;

        changeColor(paint);

        //Linea
        gc.strokeArc(pad + wS - 3*cD/4 - 20,pad + hS/2 - cD, wA, hA, 14, 152, ArcType.OPEN);
        //Flecha
        gc.fillPolygon(new double[]{xArrowF, xArrowF + 14, xArrowF + 7}, new double[]{yArrow, yArrow, yArrow + 7}, 3);
    }

    private void drawTransitionStoF(Paint paint) {
        changeColor(paint);
        //Linea
        gc.strokeLine(pad + cD + 20, pad + hS/2, pad + wS - cD - 20, pad + hS/2);
        //Flecha
        gc.fillPolygon(new double[]{pad + wS - cD - 20 -7, pad + wS - cD - 20 -7, pad + wS - cD - 20}, new double[]{pad + hS/2 - 7, pad + hS/2 + 7, pad + hS/2}, 3);
        //Texto
        alignTextToCenter();
        gc.fillText("\u03B5/\u03B5/\u03B5", pad + wS/2, pad + hS/2 - 10);
    }

    private void writeOptionAStateS(Paint paint) {
        changeColor(paint);
        alignTextDefault();
        gc.fillText("a/\u03B5/a",pad + 5, pad + hS/2 - 50);
    }

    private void writeOptionBStateS(Paint paint) {
        changeColor(paint);
        alignTextDefault();
        gc.fillText("b/\u03B5/b",pad + 45, pad + hS/2 - 50 );
    }

    private void writeOptionAStateF(Paint paint) {
        changeColor(paint);
        alignTextDefault();
        gc.fillText("a/a/\u03B5",pad + wS - cD - 35, pad + hS/2 - 50 );
    }

    private void writeOptionBStateF(Paint paint) {
        changeColor(paint);
        alignTextDefault();
        gc.fillText("b/b/\u03B5",pad + wS - cD + 5, pad + hS/2 - 50 );
    }

    public void animateTransitions(List<Estado> transitions) {

        counter = 0;

        if (timeline != null)
            timeline.stop();

        KeyFrame lectureFrame = new KeyFrame(Duration.seconds(1), e -> {

            redrawTransition(lastTransition, Color.BLACK);

            cleanUpObject(Eraser.LECTURE_AND_PILE);
            writeStringLecture(transitions.get(counter).getCadena(), Color.BLUE);

            if (counter == 0)
                writePileValue("", Color.BLACK);
            else
                writePileValue(transitions.get(counter).getPila(), Color.BLACK);

        });

        KeyFrame pileFrame = new KeyFrame(Duration.seconds(2), e -> {

            cleanUpObject(Eraser.LECTURE_AND_PILE);
            writeStringLecture(transitions.get(counter).getCadena(), Color.BLACK);
            writePileValue(transitions.get(counter).getPila(), Color.BLUE);

        });

        KeyFrame stateFrame = new KeyFrame(Duration.seconds(3), e -> {

            cleanUpObject(Eraser.PILE);
            writePileValue(transitions.get(counter).getPila(), Color.BLACK);

            if (transitions.get(counter).getEstado().equals("S") || counter == transitions.size()/2 ) {
                cleanUpObject(Eraser.STATE_S);
                drawStateS(Color.BLUE);
                drawTransitionLineS(Color.BLACK);
            } else {
                cleanUpObject(Eraser.STATE_F);
                drawStateF(Color.BLUE);
                drawTransitionLineF(Color.BLACK);
            }

        });

        KeyFrame transitionFrame = new KeyFrame(Duration.seconds(4), e -> {

            String option = transitions.get(counter).getEstado() + transitions.get(counter).getCadena().charAt(0);

            lastTransition = switch (option) {
                case "Sa" -> Eraser.TRANSITION_A_S;
                case "Sb" -> Eraser.TRANSITION_B_S;
                case "Fa" -> Eraser.TRANSITION_A_F;
                case "Fb" -> Eraser.TRANSITION_B_F;
                default -> Eraser.NULL;
            };

            if (counter == transitions.size() / 2)
                lastTransition = Eraser.EMPTY_TRANSITION;

            redrawTransition(lastTransition, Color.BLUE);

            if (++counter >= transitions.size()) {
                counter = 0;
            }
        });
        
        timeline = new Timeline(lectureFrame, pileFrame, stateFrame, transitionFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void redrawTransition(Eraser transition, Paint paint) {

        cleanUpObject(transition);

        switch (transition) {
            case TRANSITION_A_S -> {
                drawTransitionLineS(paint);
                drawStateS(Color.BLACK);
                writeOptionAStateS(paint);
            }
            case TRANSITION_B_S -> {
                drawTransitionLineS(paint);
                drawStateS(Color.BLACK);
                writeOptionBStateS(paint);
            }
            case TRANSITION_A_F -> {
                drawTransitionLineF(paint);
                drawStateF(Color.BLACK);
                writeOptionAStateF(paint);
            }
            case TRANSITION_B_F -> {
                drawTransitionLineF(paint);
                drawStateF(Color.BLACK);
                writeOptionBStateF(paint);
            }
            case EMPTY_TRANSITION -> {
                drawStateS(Color.BLACK);
                drawTransitionLineS(Color.BLACK);
                drawTransitionStoF(paint);
            }
        }
    }

    private void cleanUpObject(Eraser option) {
        
        switch (option) {
            case LECTURE -> gc.clearRect(pad + 1, pad + 10, wS - 2, 25);
            case PILE -> gc.clearRect(pad + 1, pad + hS - 65, wS - 2, 25);
            case LECTURE_AND_PILE -> {
                gc.clearRect(pad + 1, pad + 10, wS - 2, 25);
                gc.clearRect(pad + 1, pad + hS - 65, wS - 2, 25);
            }
            case STATE_S -> gc.clearRect(pad + 20,pad + hS/2 - cD, cD, 3*cD/2 );
            case STATE_F -> gc.clearRect(pad + wS - 3*cD/4 - 20,pad + hS/2 - cD, cD, 3*cD/2 );
            case TRANSITION_A_S -> {
                cleanUpObject(Eraser.STATE_S);
                gc.clearRect(pad + 5, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_B_S -> {
                cleanUpObject(Eraser.STATE_S);
                gc.clearRect(pad + 45, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_A_F -> {
                cleanUpObject(Eraser.STATE_F);
                gc.clearRect(pad + wS - cD - 35, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_B_F -> {
                cleanUpObject(Eraser.STATE_F);
                gc.clearRect(pad + wS - cD + 5, pad + hS/2 - 55, 30, 25);
            }
            case EMPTY_TRANSITION -> {
                cleanUpObject(Eraser.STATE_S);
                gc.clearRect(pad + cD + 20, pad + hS/2 - 15, wS - 40 - 2*cD, 40);
            }
        }
    }

    private void writeStringLecture(String cadena, Paint paint) {
        changeColor(paint);
        alignTextToCenter();
        gc.fillText("Lectura de la cadena:", pad + wS/2, pad + 15);
        gc.fillText(cadena, pad + wS/2, pad + 30);
    }

    private void writePileValue(String pila, Paint paint) {
        changeColor(paint);
        alignTextToCenter();
        gc.fillText("Pila:", pad + wS/2, pad + hS - 60);
        gc.fillText(pila, pad + wS/2, pad + hS - 45);
    }

    private void alignTextToCenter(){
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
    }

    private void alignTextDefault(){
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }

    private void changeColor(Paint paint) {
        gc.setStroke(paint);
        gc.setFill(paint);
    }
}
