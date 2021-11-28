package com.example.automatadepila;

public class Estado {

    private String estado;
    private String cadena;
    private String pila;

    public Estado(String estado, String cadena, String pila) {
        this.estado = estado;
        this.cadena = cadena;
        this.pila = pila;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getPila() {
        return pila;
    }

    public void setPila(String pila) {
        this.pila = pila;
    }
}
