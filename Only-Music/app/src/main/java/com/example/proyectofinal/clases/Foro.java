package com.example.proyectofinal.clases;

import java.io.Serializable;

/**
 * @author Jesús Escudero Gabarre
 * Clase utilizada para trabajar con objetos de tipo Foro
 */
public class Foro implements Serializable {
    private String imagen_perfil, nombre_usuario, fecha_publicacion, titulo_mensaje, mensaje, categoria, email;

    /**
     * Constructor de la clase Foro
     * @param imagen_perfil Variable de tipo String donde almacena la ruta de la imagen del usuario en FireBase
     * @param nombre_usuario Variable de tipo String donde almacena el nombre del usuario que ha realizado la publicación
     * @param fecha_publicacion Variable de tipo String donde almacena la fecha de publicación
     * @param titulo_mensaje Variable de tipo String donde almacena el titulo del post
     * @param mensaje Variable de tipo String donde almacena el mensaje del post
     */
    public Foro(String imagen_perfil, String nombre_usuario, String fecha_publicacion, String titulo_mensaje, String mensaje) {
        this.imagen_perfil = imagen_perfil;
        this.nombre_usuario = nombre_usuario;
        this.fecha_publicacion = fecha_publicacion;
        this.titulo_mensaje = titulo_mensaje;
        this.mensaje = mensaje;
    }

    /**
     * Constructor de la clase Foro
     * @param fecha_publicacion Variable de tipo String donde almacena la fecha de publicación
     * @param titulo_mensaje Variable de tipo String donde almacena el titulo del post
     * @param mensaje Variable de tipo String donde almacena el mensaje del post
     * @param categoria Variable de tipo String donde almacena la categoria del post
     */
    public Foro(String fecha_publicacion, String titulo_mensaje, String mensaje, String categoria) {
        this.fecha_publicacion = fecha_publicacion;
        this.titulo_mensaje = titulo_mensaje;
        this.mensaje = mensaje;
        this.categoria = categoria;
    }

    public Foro(String imagen_perfil, String nombre_usuario, String fecha_publicacion, String titulo_mensaje, String mensaje, String categoria, String email) {
        this.imagen_perfil = imagen_perfil;
        this.nombre_usuario = nombre_usuario;
        this.fecha_publicacion = fecha_publicacion;
        this.titulo_mensaje = titulo_mensaje;
        this.mensaje = mensaje;
        this.categoria = categoria;
        this.email = email;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getTitulo_mensaje() {
        return titulo_mensaje;
    }

    public void setTitulo_mensaje(String titulo_mensaje) {
        this.titulo_mensaje = titulo_mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Foro{" +
                "imagen_perfil='" + imagen_perfil + '\'' +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", fecha_publicacion='" + fecha_publicacion + '\'' +
                ", titulo_mensaje='" + titulo_mensaje + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", categoria='" + categoria + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
