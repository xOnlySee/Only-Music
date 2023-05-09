package com.example.proyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Artista;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente al adaptador del RecyclerView
 */
public class AdaptadorArtista extends RecyclerView.Adapter<AdaptadorArtista.ViewHolder> {
    //ArrayList que representan los artistas almacenados
    private final ArrayList<Artista> listado_artistas;

    //ArrayList que contienen las artistas filtrados
    private final ArrayList<Artista> listado_artistas_filtrados;

    //Variable de tipo OnClicListener utilizado para darle funcionabilidad a la lista
    private View.OnClickListener onClickListener;

    /**
     * Método para darle funcionabilidad a la lista
     * @param onClickListener Variable de tipo OnClicListiener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Constructor de la clase Adaptador
     * @param listado_artistas ArrayList donde contiene los datos
     * @param contexto Contexto
     * @param r_layout_id ID del layout
     */
    public AdaptadorArtista(ArrayList<Artista> listado_artistas, Context contexto, int r_layout_id) {
        this.listado_artistas = listado_artistas;

        listado_artistas_filtrados = new ArrayList<>();
        listado_artistas_filtrados.addAll(listado_artistas);
    }

    /**
     * Método donde podremos inflar la vista y representar los elementos en la lista
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return Devuelve el ViewHolder con la vista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflamos la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_artistas, parent,false);

        //Añadimos el método setOnClicListener a la vista
        view.setOnClickListener(onClickListener);

        //Devolvemos el ViewHolder con la vista
        return new ViewHolder(view);
    }

    /**
     * Método para obtener el objeto Artista del ArrayList
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artista artista = (Artista) listado_artistas.get(position);
        holder.representacionElementos(artista);
    }

    /**
     * Método para obtener el tamaño del ArrayList de tipo Artista
     * @return Devuele el tamaño del ArrayList
     */
    @Override
    public int getItemCount() {
        return listado_artistas.size();
    }

    //Clase estatica correspondiente al Adaptador
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imagen;
        public final TextView nombre_artista;

        /**
         * Constructor de la clase ViewHolder utilizada para identificar a los elementos
         * @param itemView Variable de tipo View
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imagen = itemView.findViewById(R.id.imagenArtista_listadoArtista);
            this.nombre_artista = itemView.findViewById(R.id.campoNombreArtista_listadoArtista);
        }

        /**
         * Método para añadir información a los elementos del RecyclerView
         * @param artista Objeto de la clase Artista
         */
        public void representacionElementos(Artista artista) {
            Glide.with(itemView).load(artista.getImagen()).into(imagen);
            nombre_artista.setText(artista.getNombre());
        }
    }

    /**
     * Método donde filtraremos el listado por medio de la cadena introducida por el usuario en el SerachView
     * @param cadena Variable de tipo String donde almacenerá la cadena introducida por el usuario en el SerachView
     */
    public void filtrado(String cadena) {
        //Comprobamos mediante un if-else el contenido del SearchView

        //En caso de que la cadena tenga tamaño 0 (es decir, el usuario no ha introducido nada)
        if (cadena.length() == 0) {
            listado_artistas.clear();
            listado_artistas.addAll(listado_artistas_filtrados);

        //En caso de que el usuario haya introducido información en el SearchView, mostraremos los elementos filtrados
        } else {
            /* Dos maneras de mostrar la información filtrada dependiendo de que versión del dispositivo Android usen */

            //Creamos una nueva colección donde añadiremos los elementos filtrados y los añadimos al ArrayList donde contiene los elementos filtrados
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Artista> collecion = listado_artistas.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(cadena.toLowerCase()))
                        .collect(Collectors.toList());
                listado_artistas.clear();
                listado_artistas.addAll(collecion);

            //Por cada elemento de la lista, comprobamos si el nombre de la canción contiene la cadena introducida por el usuario en el SearchView
            } else {
                //Iteramos por cada elemento del ArrayList donde contiene los elementos ha filtrar
                for (Artista artista : listado_artistas_filtrados) {

                    //En caso de que algún elemento del ArrayList de canciones filtradas contiene la cadena introducida por el usuario los añadirá al ArrayList de artistas filtradas
                    if (artista.getNombre().toLowerCase().contains(cadena.toLowerCase())) {
                        listado_artistas_filtrados.add(artista);
                    }
                }
            }
        }

        //Usamos este método para avisar de que se han realizado cambios y así poder mostrarlos
        notifyDataSetChanged();
    }
}
