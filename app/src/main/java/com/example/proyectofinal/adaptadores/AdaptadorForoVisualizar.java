package com.example.proyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Foro;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Adaptador correspondiente a la visualización de los foros del usuario que haya iniciado sesión
 */
public class AdaptadorForoVisualizar extends RecyclerView.Adapter<AdaptadorForoVisualizar.ViewHolder> {
    //ArrayList que representan los artistas almacenados
    private final ArrayList<?> listado_foros;

    //OnClicListener donde añadiremos funcionabilidad al RecyclerView
    private View.OnClickListener onClickListener;

    /**
     * Constructor del setOnClicListener
     * @param onClickListener Objeto de la calse View.OnClicListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Constructor de la clase Adaptador
     * @param listado_foros ArrayList donde contiene los datos
     * @param context Contexto
     * @param R_layout_id ID del layout
     */
    public AdaptadorForoVisualizar(ArrayList<?> listado_foros, Context context, int R_layout_id) {
        this.listado_foros = listado_foros;
    }

    /**
     * Método donde especificaremos obtendremos el objeto de la clase View y se la pasaremos al ViewHolder
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return Devolvemos el ViewHolder con la vista
     */
    @NonNull
    @Override
    public AdaptadorForoVisualizar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflamos la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_foro_visualizar, parent,false);

        view.setOnClickListener(onClickListener);

        //Devolvemos el ViewHolder con la vista
        return new AdaptadorForoVisualizar.ViewHolder(view);
    }

    /**
     * Método donde obtendremos el objeto Foro usando el ArrayList y la variable int "position"
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AdaptadorForoVisualizar.ViewHolder holder, int position) {
        Foro foro = (Foro) listado_foros.get(position);
        holder.representacionElemento(foro);
    }

    /**
     * Método donde obtendremos el tamaño del ArrayList
     * @return Devuelve el tamaño del ArrayList
     */
    @Override
    public int getItemCount() {
        return listado_foros.size();
    }

    /**
     * @author Jesús Escudero Gabarre
     * Clase donde especificaremos los elementos del RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView categoria;
        final TextView fecha_publicacion;
        final TextView titulo_mensaje;
        final TextView mensaje;

        /**
         * Constructor de la clase ViewHolder utilizada para identificar a los elementos
         * @param itemView Variable de tipo View
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoria = itemView.findViewById(R.id.categoria_elementoForoVisualizar);
            this.fecha_publicacion = itemView.findViewById(R.id.fechaPublicacion_elementoForoVisualizar);
            this.titulo_mensaje = itemView.findViewById(R.id.tituloMensaje_elementoForoVisualizar);
            this.mensaje = itemView.findViewById(R.id.mensaje_elementoForoVisualizar);
        }

        /**
         * Método para añadir información a los elementos del RecyclerView
         * @param foro Objeto de clase Foro
         */
        public void representacionElemento(Foro foro) {
            categoria.setText(foro.getCategoria());
            fecha_publicacion.setText(foro.getFecha_publicacion());
            titulo_mensaje.setText(foro.getTitulo_mensaje());
            mensaje.setText(foro.getMensaje());
        }
    }
}
