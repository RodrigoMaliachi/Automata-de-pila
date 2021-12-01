package com.example.automatadepila;

public record Estado(String estado, String cadena, String pila) {

    public String getEstado() {
        return estado;
    }

    public String getCadena() {
        return cadena;
    }

    public String getPila() {
        return pila;
    }
}
