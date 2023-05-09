package com.example.proyectofinal.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Foro;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente al adaptador del RecyclerView que contendrá los distintos posts de los foros
 */
public class AdaptadorForo extends RecyclerView.Adapter<AdaptadorForo.ViewHolder> {
    //ArrayList que representan los artistas almacenados
    private final ArrayList<?> listado_foros;
    private View.OnClickListener onClickListener;

    /**
     * Constructor del onClicListener
     * @param onClickListener Objeto de la clase OnClicListener
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
    public AdaptadorForo(ArrayList<?> listado_foros, Context context, int R_layout_id) {
        this.listado_foros = listado_foros;
    }

    /**
     * Método donde podremos inflar la voista y representar los elementos de la lista
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_foro, parent,false);

        view.setOnClickListener(onClickListener);

        //Devolvemos el ViewHolder con la vista
        return new ViewHolder(view);
    }

    /**
     * Método para obtemner el objeto Foro del ArrayList
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foro foro = (Foro) listado_foros.get(position);
        holder.representacionElemento(foro);
    }

    /**
     * Método para obtener el tamaño del ArrayList de tipo Foro
     * @return Variable de tipo int que devuelve el tamaño del ArrayList de tipo "Foro"
     */
    @Override
    public int getItemCount() {
        return listado_foros.size();
    }

    //Clase estática correspondiente al Adaptador
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre_usuario, fecha_publicacion, titulo_mensaje, mensaje;

        /**
         * Constructor de la clase ViewHolder utilizada para identificar a los elementos
         * @param itemView Variable de tipo View
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imagen = itemView.findViewById(R.id.imagen_elementoForo);
            this.nombre_usuario = itemView.findViewById(R.id.categoria_elementoForoVisualizar);
            this.fecha_publicacion = itemView.findViewById(R.id.fechaPublicacion_elementoForoVisualizar);
            this.titulo_mensaje = itemView.findViewById(R.id.tituloMensaje_elementoForoVisualizar);
            this.mensaje = itemView.findViewById(R.id.mensaje_elementoForoVisualizar);
        }

        /**
         * Método para añadir información a los elementos del RecyclerView
         * @param foro Objeto de clase Foro
         */
        public void representacionElemento(Foro foro) {
            if (!foro.getImagen_perfil().isEmpty()) {
                Uri uri = Uri.parse(foro.getImagen_perfil());
                FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(uri)).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        //Variable de tipo long donde almacenaremos la fecha de la ultima modificación en milisegundos
                        long ultima_modificacion = storageMetadata.getUpdatedTimeMillis();

                        //Usamos Glide donde le indicamos la imagen que queremos cargar, la fecha de última modificacion (para indicar que la imágen ha cambiado) y el elemento donde lo queremos mostrar
                        Glide.with(itemView)
                                .load(uri)
                                .signature(new ObjectKey(ultima_modificacion))
                                .into(imagen);
                    }
                });
            }

            nombre_usuario.setText(foro.getNombre_usuario());
            fecha_publicacion.setText(foro.getFecha_publicacion());
            titulo_mensaje.setText(foro.getTitulo_mensaje());
            mensaje.setText(foro.getMensaje());
        }
    }
}
