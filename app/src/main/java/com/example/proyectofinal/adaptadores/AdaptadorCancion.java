package com.example.proyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Cancion;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Adaptador correspondiente a la visualización de canciones
 */
public class AdaptadorCancion extends BaseAdapter {
    //ArrayList que contienen las canciones almacenadas
    private final ArrayList<Cancion> listado_canciones;

    //ArrayList que contienen las canciones filtradas
    private final ArrayList<Cancion> listado_canciones_filtradas;

    //Variable de tipo LayoutInflate
    private final LayoutInflater layoutInflater;

    /**
     * Constructor de la clase Adaptador
     * @param contexto Variable de tipo Context
     * @param listado_canciones ArrayList que contiene las canciones
     */
    public AdaptadorCancion(Context contexto, ArrayList<Cancion> listado_canciones) {
        this.listado_canciones = listado_canciones;
        this.layoutInflater = LayoutInflater.from(contexto);

        listado_canciones_filtradas = new ArrayList<>();
        listado_canciones_filtradas.addAll(listado_canciones);
    }

    /**
     * Método con que el obtendremos el número de elementos del ArrayList
     * @return Devuelve el número del elementos del ArrayList
     */
    @Override
    public int getCount() {
        return listado_canciones.size();
    }

    /**
     * Devuelve el objeto del ArrayList de canciones
     * @param i Variable de tipo int que representa la posición del elemento
     * @return Devuelve el elemento de tipo Cancion
     */
    @Override
    public Object getItem(int i) {
        return listado_canciones.get(i);
    }

    /**
     * Método que nos devuelve una posición de la lista
     * @param i Variable de tipo int que representa la posición de la lista
     * @return Devuelve una posicón de la lista
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Método donde mostrar el nombre de las canciones con su respectivo artista
     * @param i Variable del tipo int que representa la posición del elemento de la lista
     * @param view Variable de tipo View
     * @param viewGroup Variable de tipo ViewGroup
     * @return Devuelve el layout correspondiente (ya inflado por los elementos)
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Objeto de tipo LinearLayout utilizado para inflarlo
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.listado_canciones, null, false);

        //TextViews que usaremos para mostrar la información en la lista de canciones
        TextView titulo_cancion = layout.findViewById(R.id.tituloCancion_fragmentoCanciones),
        informacion_cancion = layout.findViewById(R.id.informacionCancion_fragmentoCanciones);

        //Objeto de tipo canción que almacenará la canción que pulse el usuario en la lista de canciones
        Cancion cancion = listado_canciones.get(i);

        //Con el objeto Canción obtenido de la lista, añadiremos la información en los TextView
        titulo_cancion.setText(cancion.getTitulo());

        //Variable de tipo int donde obtendremos la duración de la canción en milisegundos
        int duracion = Integer.parseInt(cancion.getDuracion());

        //Variables de tipo long donde obtendremos la duración de la canción en minutos y en segundos
        long minutos = (duracion/1000)/60,
        segundos = (duracion/1000)%60;

        //Añadimos la información necesarias al TextView; mostraremos el nombre del artista de la canción, el nombre del albúm y la duración de la canción
        informacion_cancion.setText(cancion.getArtista() + " ● " + cancion.getAlbum() + " ● " + minutos + ":" + segundos);

        //Usamos el método .setTag() donde le pasamos la posición
        layout.setTag(i);

        //Devolvemos el layout ya inflado
        return layout;
    }
}
