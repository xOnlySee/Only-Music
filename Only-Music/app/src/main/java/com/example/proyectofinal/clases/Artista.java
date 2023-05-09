package com.example.proyectofinal.clases;

import java.io.Serializable;

/**
 * @author Jesús Escudero Gabarre
 * Clase utilizada para trabajar con objetos de tipo Artista
 */
public class Artista implements Serializable {
    private String apellidos, biografia, fecha_nacimiento, genero_musical, imagen, nacionalidad, nombre, numero_albums;

    /**
     * Constructor de la clase Artista
     * @param apellidos Variable de tipo String donde almacenerá los apellidos
     * @param biografia Variable de tipo String donde almacenerá la biografia
     * @param fecha_nacimiento Variable de tipo String donde almacenerá la fecha de nacimiento
     * @param genero_musical Variable de tipo String donde almacenerá el género musical
     * @param imagen Variable de tipo String donde almacenerá la URL de la imágen principal
     * @param nacionalidad Variable de tipo String donde almacenerá la nacionalidad
     * @param nombre Variable de tipo String donde almacenerá el nombre del artista
     * @param numero_albums Variable de tipo String donde almacenerá el número de albums
     */
    public Artista(String apellidos, String biografia, String fecha_nacimiento, String genero_musical, String imagen, String nacionalidad, String nombre, String numero_albums) {
        this.apellidos = apellidos;
        this.biografia = biografia;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero_musical = genero_musical;
        this.imagen = imagen;
        this.nacionalidad = nacionalidad;
        this.nombre = nombre;
        this.numero_albums = numero_albums;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getGenero_musical() {
        return genero_musical;
    }

    public void setGenero_musical(String genero_musical) {
        this.genero_musical = genero_musical;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero_albums() {
        return numero_albums;
    }

    public void setNumero_albums(String numero_albums) {
        this.numero_albums = numero_albums;
    }
}
