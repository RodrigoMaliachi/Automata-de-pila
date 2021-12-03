package com.example.automatadepila;

public final class Estado {
    private final String estado;
    private final String cadena;
    private final String pila;

    public Estado(String estado, String cadena, String pila) {
        this.estado = estado;
        this.cadena = cadena;
        this.pila = pila;
    }

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