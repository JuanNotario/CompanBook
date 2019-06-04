package com.companbook.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Datos_Empresa implements Parcelable {
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

    public Datos_Empresa() {
    }

    protected Datos_Empresa(Parcel in) {
        uid = in.readString();
        url_logo = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        cif = in.readString();
        dirección = in.readString();
    }

    public static final Creator<Datos_Empresa> CREATOR = new Creator<Datos_Empresa>() {
        @Override
        public Datos_Empresa createFromParcel(Parcel in) {
            return new Datos_Empresa(in);
        }

        @Override
        public Datos_Empresa[] newArray(int size) {
            return new Datos_Empresa[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(url_logo);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(cif);
        dest.writeString(dirección);
    }
}
