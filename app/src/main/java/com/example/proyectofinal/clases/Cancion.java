package com.example.proyectofinal.clases;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author Jesús Escudero Gabarre
 * Clase utilizada para trabajar con objetos de tipo Cancion
 */
public class Cancion implements Serializable, Parcelable {
    private Uri imagen;
    private long id;
    private String  imagen_URL, titulo, artista, ruta, duracion, album;


    public Cancion(Uri imagen, long id, String titulo, String artista, String ruta, String duracion, String album) {
        this.imagen = imagen;
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.ruta = ruta;
        this.duracion = duracion;
        this.album = album;
    }

    public Cancion(long id, String imagen_URL, String titulo, String artista, String ruta, String duracion, String album) {
        this.id = id;
        this.imagen_URL = imagen_URL;
        this.titulo = titulo;
        this.artista = artista;
        this.ruta = ruta;
        this.duracion = duracion;
        this.album = album;
    }

    protected Cancion(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        artista = in.readString();
        ruta = in.readString();
        duracion = in.readString();
    }

    public static final Creator<Cancion> CREATOR = new Creator<Cancion>() {
        @Override
        public Cancion createFromParcel(Parcel in) {
            return new Cancion(in);
        }

        @Override
        public Cancion[] newArray(int size) {
            return new Cancion[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getImagen() {
        return imagen;
    }

    public String getImagen_URL() {
        return imagen_URL;
    }

    public void setImagen_URL(String imagen_URL) {
        this.imagen_URL = imagen_URL;
    }

    public void setImagen(Uri imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(titulo);
        parcel.writeString(artista);
        parcel.writeString(ruta);
        parcel.writeString(duracion);
    }
}
