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

    // La clase enum Eraser ayuda a identificar el objeto que debe ser borrado del canvas
    private enum Eraser { LECTURE, PILE, LECTURE_AND_PILE, STATE_S, STATE_F, TRANSITION_S_A, TRANSITION_S_B, TRANSITION_F_A, TRANSITION_F_B, EMPTY_TRANSITION, NULL }

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

    private final Color defaultColor = Color.BLACK;
    private final Color animateColor = Color.BLUE;

    public Diagrama(Canvas canva) {
        gc = canva.getGraphicsContext2D();
        drawDiagram();
    }

    private void drawDiagram(){

        changeColor(defaultColor);

        //Dibuja el cuadrado principal
        gc.strokeRect(pad,pad,wS,hS);

        //Dibuja los circulos de los estados S y F
        drawStateS(defaultColor);
        drawStateF(defaultColor);

        //Dibuja las lineas de transición de los estados S, F y S-F
        drawTransitionLineS(defaultColor);
        drawTransitionLineF(defaultColor);
        drawTransitionStoF(defaultColor);

        //Escribe los textos de las transiciones
        writeOptionAStateS(defaultColor);
        writeOptionBStateS(defaultColor);
        writeOptionAStateF(defaultColor);
        writeOptionBStateF(defaultColor);
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

        //Se inicializa el contador de las transiciones en 0 para comenzar una nueva animación
        counter = 0;

        //Animación de "Lectura de la cadena"
        KeyFrame lectureFrame = new KeyFrame(Duration.seconds(1), e -> {

            //Se redibuja el último objeto animado del diagrama
            //Sirve para limpiar el diagrama cuando se introduce una nueva palabra palindroma
            redrawTransition(lastTransition, defaultColor);

            //Se borra el texto de "Lectura de cadena" y "Pila"
            cleanUpObject(Eraser.LECTURE_AND_PILE);

            //Se escribe el texto de "Lectura de cadena" en azul
            writeStringLecture(transitions.get(counter).getCadena(), animateColor);

            //Si es el estado inicial, la pila se muestra vacia en señal de que aún no está inicializada
            if (counter == 0)
                writePileValue("", defaultColor);
            else //Sino, escribe el contenido de la pila en el estado anterior
                writePileValue(transitions.get(counter-1).getPila(), defaultColor);

        });

        //Animación de "Pila"
        KeyFrame pileFrame = new KeyFrame(Duration.seconds(2), e -> {

            //Se borra el texto de "Lectura de cadena" y "Pila"
            cleanUpObject(Eraser.LECTURE_AND_PILE);

            //Se escribe el texto de "Lectura de cadena" en negro
            writeStringLecture(transitions.get(counter).getCadena(), defaultColor);

            //Se escribe el texto de "Pila" en azul
            writePileValue(transitions.get(counter).getPila(), animateColor);

        });

        //Animación del Estado (S o F)
        KeyFrame stateFrame = new KeyFrame(Duration.seconds(3), e -> {

            //Se limpia el texto de "Pila" y se vuelve a escribir en negro
            cleanUpObject(Eraser.PILE);
            writePileValue(transitions.get(counter).getPila(), defaultColor);

            //Redibuja el estado correspondiente en color azul
            if (transitions.get(counter).getEstado().equals("S")) {
                cleanUpObject(Eraser.STATE_S);
                drawStateS(animateColor);
                drawTransitionLineS(defaultColor);
            } else {
                cleanUpObject(Eraser.STATE_F);
                drawStateF(animateColor);
                drawTransitionLineF(defaultColor);
            }

        });

        //Animación de las lineas y textos de transición
        KeyFrame transitionFrame = new KeyFrame(Duration.seconds(4), e -> {

            //Se combina el estado con la primera letra de la cadena restante para definir la transición
            String option = transitions.get(counter).getEstado() + transitions.get(counter).getCadena().charAt(0);

            //Se actualiza lastTransition con una opción de Eraser
            lastTransition = switch (option) {
                case "Sa" -> Eraser.TRANSITION_S_A;
                case "Sb" -> Eraser.TRANSITION_S_B;
                case "Fa" -> Eraser.TRANSITION_F_A;
                case "Fb" -> Eraser.TRANSITION_F_B;
                default -> Eraser.NULL;
            };

            //Si el contador está a la mitad, entonces es la transición del estado S al F
            if (counter == transitions.size() / 2 - 1)
                lastTransition = Eraser.EMPTY_TRANSITION;

            //La transición se anima solo si no es la última transición
            if (counter != transitions.size() - 1)
                redrawTransition(lastTransition, animateColor);
            else
                redrawTransition(lastTransition, defaultColor);

            counter = (counter + 1) % transitions.size();
        });

        if (timeline != null) {

            //Si el timeline ya existe, se detiene para eliminar los antiguos KeyFrames y añadir los nuevos
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(lectureFrame, pileFrame, stateFrame, transitionFrame);

        } else {

            //Sino, se crea una nueva instancia de timeline
            timeline = new Timeline(lectureFrame, pileFrame, stateFrame, transitionFrame);

            //Hace que la animación no se detenga
            timeline.setCycleCount(Animation.INDEFINITE);

        }

        timeline.play();
    }

    private void redrawTransition(Eraser transition, Paint paint) {

        //Se limpia el área donde está el objeto
        cleanUpObject(transition);

        //Se dibuja el objeto con el color deseado
        switch (transition) {
            case TRANSITION_S_A -> {
                drawTransitionLineS(paint);
                drawStateS(defaultColor);
                writeOptionAStateS(paint);
            }
            case TRANSITION_S_B -> {
                drawTransitionLineS(paint);
                drawStateS(defaultColor);
                writeOptionBStateS(paint);
            }
            case TRANSITION_F_A -> {
                drawTransitionLineF(paint);
                drawStateF(defaultColor);
                writeOptionAStateF(paint);
            }
            case TRANSITION_F_B -> {
                drawTransitionLineF(paint);
                drawStateF(defaultColor);
                writeOptionBStateF(paint);
            }
            case EMPTY_TRANSITION -> {
                drawStateS(defaultColor);
                drawTransitionLineS(defaultColor);
                drawTransitionStoF(paint);
            }
            case NULL -> {
                drawStateF(paint);
                drawTransitionLineF(paint);
            }
        }
    }

    private void cleanUpObject(Eraser option) {
        //Limpia el área del objeto correspondiente
        switch (option) {
            case LECTURE -> gc.clearRect(pad + 1, pad + 10, wS - 2, 25);
            case PILE -> gc.clearRect(pad + 1, pad + hS - 65, wS - 2, 25);
            case LECTURE_AND_PILE -> {
                cleanUpObject(Eraser.LECTURE);
                cleanUpObject(Eraser.PILE);
            }
            case STATE_S -> gc.clearRect(pad + 20,pad + hS/2 - cD, cD, 3*cD/2 );
            case NULL, STATE_F -> gc.clearRect(pad + wS - 3*cD/4 - 20,pad + hS/2 - cD, cD, 3*cD/2 );
            case TRANSITION_S_A -> {
                cleanUpObject(Eraser.STATE_S);
                gc.clearRect(pad + 5, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_S_B -> {
                cleanUpObject(Eraser.STATE_S);
                gc.clearRect(pad + 45, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_F_A -> {
                cleanUpObject(Eraser.STATE_F);
                gc.clearRect(pad + wS - cD - 35, pad + hS/2 - 55, 30, 25);
            }
            case TRANSITION_F_B -> {
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
        //Escribe el texto de "Lectura de cadena"
        changeColor(paint);
        alignTextToCenter();
        gc.fillText("Lectura de la cadena:", pad + wS/2, pad + 15);
        gc.fillText(cadena, pad + wS/2, pad + 30);
    }

    private void writePileValue(String pila, Paint paint) {
        //Escribe el texto de "Pila"
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

    public void stopAnimation() {
        if (timeline != null) {
            timeline.stop();
            gc.clearRect(pad,pad,wS,hS);
            drawDiagram();
        }
    }
}