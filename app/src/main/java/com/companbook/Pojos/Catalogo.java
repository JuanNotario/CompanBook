package com.companbook.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Catalogo implements Parcelable {
    String uid;
    String nombre;
    String referencia;
    String desc;
    int random;
    Double precio;
    String url_foto;
    String palabraClave;
    String potencia;
    String tamaño;

    public Catalogo(int random, String uid, String nombre, String referencia, String desc, Double precio, String url_foto, String palabraClave, String potencia, String tamaño) {
        this.random = random;
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

    protected Catalogo(Parcel in) {
        random = in.readInt();
        uid = in.readString();
        nombre = in.readString();
        referencia = in.readString();
        desc = in.readString();
        precio = in.readDouble();
        url_foto = in.readString();
        palabraClave = in.readString();
        potencia = in.readString();
        tamaño = in.readString();
    }

    public static final Creator<Catalogo> CREATOR = new Creator<Catalogo>() {
        @Override
        public Catalogo createFromParcel(Parcel in) {
            return new Catalogo(in);
        }

        @Override
        public Catalogo[] newArray(int size) {
            return new Catalogo[size];
        }
    };

    public int getRandom() {
        return random;
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

    public Double getPrecio() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(random);
        dest.writeString(uid);
        dest.writeString(nombre);
        dest.writeString(referencia);
        dest.writeString(desc);
        dest.writeDouble(precio);
        dest.writeString(url_foto);
        dest.writeString(palabraClave);
        dest.writeString(potencia);
        dest.writeString(tamaño);
    }
}
