package com.example.proyectofinal.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author Jesús Escudero Gabarre
 * Clase utilizada para trabajar con objetos de tipo Cancion
 */
public class Cancion implements Serializable, Parcelable {
    private long id;
    private String titulo, artista, ruta, duracion, album;

    /**
     * Constructor de la clase Cancion
     * @param id Variable de tipo long donde almacenará la ID de la canción
     * @param titulo Variable de tipo String donde almacena el titulo de la canción
     * @param artista Variable de tipo String donde almacena el nombre del artista
     * @param ruta Variable de tipo String donde almacena la ruta de la canción
     * @param duracion Variable de tipo String donde almacena la duración de la canción
     * @param album Variable de tipo String donde almacena el nombre del album de la canción
     */
    public Cancion(long id, String titulo, String artista, String ruta, String duracion, String album) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.ruta = ruta;
        this.duracion = duracion;
        this.album = album;
    }

    /**
     * Constrcutor de la clase Cancion utilizado para crear serializar objeto de la clase Cancion y enviar los datos
     * @param in Objeto de la clase Parcel
     */
    protected Cancion(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        artista = in.readString();
        ruta = in.readString();
        duracion = in.readString();
    }

    /**
     * Método que nos permitirá crear instancias de una clase a partir de un objeto de la clase Parcel
     */
    public static final Creator<Cancion> CREATOR = new Creator<Cancion>() {
        /**
         * Método donde ejecuta de forma automática donde se crea y se instancia un objeto de la clase Cancion a partir de un objeto Parcel
         * @param in The Parcel to read the object's data from.
         * @return Devuelve un objeto de la clase Cancion
         */
        @Override
        public Cancion createFromParcel(Parcel in) {
            return new Cancion(in);
        }

        /**
         * Método donde permitirá crear un array de instancias de la clase Cancion
         * @param size Size of the array.
         * @return Devuelve un array de tipo Cancion
         */
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

    public String getDuracion() {
        return duracion;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Método donde comprueba si hay información adicional sobre el objeto de la clase Parcelable
     * @return Devuelve un int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Método donde nos permitirá recibir la información de la clase Cancion
     * @param parcel The Parcel in which the object should be written.
     * @param i Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(titulo);
        parcel.writeString(artista);
        parcel.writeString(ruta);
        parcel.writeString(duracion);
    }
}
