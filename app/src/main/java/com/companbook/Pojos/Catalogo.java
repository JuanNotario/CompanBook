package com.companbook.Pojos;

public class Catalogo {
    String uid;
    String nombre;
    String referencia;
    String desc;
    int precio;
    String url_foto;
    String palabraClave;
    String potencia;
    String tamaño;

    public Catalogo(String uid, String nombre, String referencia, String desc, int precio, String url_foto, String palabraClave, String potencia, String tamaño) {
        this.uid = uid;
        this.nombre = nombre;
        this.referencia = referencia;
        this.desc = desc;
        this.precio = precio;
        this.url_foto = url_foto;
        this.palabraClave = palabraClave;
        this.potencia = potencia;
        this.tamaño = tamaño;
    }

    public Catalogo() {
    }

    public String getUid() {
        return uid;
    }

    public String getNombre() {
        return nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrecio() {
        return precio;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public String getPalabraClave() {
        return palabraClave;
    }

    public String getPotencia() {
        return potencia;
    }

    public String getTamaño() {
        return tamaño;
    }
}
