package com.companbook.Pojos;

public class Datos_Empresa {
    private String uid;
    String url_logo;
    String nombre;
    String descripcion;
    String cif;
    String dirección;

    public Datos_Empresa(String uid, String url_logo, String nombre, String descripcion, String cif, String dirección) {
        this.uid = uid;
        this.url_logo = url_logo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cif = cif;
        this.dirección = dirección;
    }

    public String getUid() {
        return uid;
    }

    public String getUrl_logo() {
        return url_logo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCif() {
        return cif;
    }

    public String getDirección() {
        return dirección;
    }
}
