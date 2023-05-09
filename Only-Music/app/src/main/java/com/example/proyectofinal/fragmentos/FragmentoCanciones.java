package com.example.proyectofinal.fragmentos;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.proyectofinal.R;
import com.example.proyectofinal.actividades.Reproduccion;
import com.example.proyectofinal.adaptadores.AdaptadorCancion;
import com.example.proyectofinal.clases.Cancion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jesús Escudero Gabarre
 * Fragmento donde mostraremos un listado de todas las canciones que tenga el usuario almacenadas en su dispositivo local
 */
public class FragmentoCanciones extends Fragment {
    //Declaramos los elementos necesarios
    AdaptadorCancion adaptadorCancion;
    TextView texto_cancion;
    LottieAnimationView animacion;
    private ArrayList<Cancion> canciones;
    private ListView listado_canciones;
    private String email, id_documento;

    /**
     * Método donde obtendremos la información que nos llegue
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Usamos el método getParentFragmentManager().setFragmentResultListener() para recuperar el email del usuario que ha iniciado sesión
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                email = result.getString("email");
                id_documento = result.getString("ID_documento");
            }
        });
    }

    /**
     * Método para que el usuario otorgue los permisos pertinentes a la aplicación
     * @param requestCode The request code passed in requestPermissions()
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_canciones, container, false);

        //Identificamos el ListView
        listado_canciones = view.findViewById(R.id.listadoCanciones_fragmentoCanciones);

        //Identificamos el TextView
        texto_cancion = view.findViewById(R.id.textoCancion_fragmentoCanciones);

        //Identificamos el LottieAnimationView
        animacion = view.findViewById(R.id.imagen_fragmentoCanciones);

        //Instanciamos el ArrayList de tipo Cancion
        canciones = new ArrayList<>();

        //Llamamos al método obtenerCancaciones() para que realize la búsqueda de la canciones en el dispositivo
        obtenerCanciones();

        if (canciones.isEmpty()) {
            animacion.setAnimation("84655-swinging-sad-emoji.json");
            animacion.loop(true);
            texto_cancion.setText("No hay canciones");
        } else if (!canciones.isEmpty()) {
            //Usamos el método sort() para ordenar el ArrayList de las canciones para que aparezcan de forma ordenada alfabéticamente
            canciones.sort(new Comparator<Cancion>() {
                /**
                 * Usamos el método compare() para establecer la comparación y así poder ordenar las canciones
                 * @param cancion1 Objeto de tipo Cancion
                 * @param cancion2 Objeto de tipo Cancion
                 * @return Devuelve un objeto de tipo Cancion
                 */
                @Override
                public int compare(Cancion cancion1, Cancion cancion2) {
                    return cancion1.getTitulo().compareTo(cancion2.getTitulo());
                }
            });

            //Creamos e instanciamos el objeto de la clase AdaptadorCancion donde le pasamos los argumentos necesarios
            adaptadorCancion = new AdaptadorCancion(getContext().getApplicationContext(), canciones);

            //Usamos el método .setAdapter() del ListView donde le pasamos el adaptador
            listado_canciones.setAdapter(adaptadorCancion);

            //Añadimos el método .setOnItemClickListener() para darle funcionabilidad a los elementos de la lista
            listado_canciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Almacenamos en la variable int la posición de la canción que haya pulsado el usuario
                    int posicion = (Integer) view.getTag();

                    //Creamos un Intent que vaya desde el fragmento de canciones a la actividad de reproductor de música y enviamos la posición, el ArrayList de las canciones y el email del usuario
                    Intent intent = new Intent(getContext(), Reproduccion.class);
                    intent.putExtra("posicion", posicion);
                    intent.putExtra("canciones", canciones);
                    intent.putExtra("email", email);
                    intent.putExtra("ID_documento", id_documento);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    /**
     * Método donde realizará una búsqueda de todas las canciones que tenemos almacenadas de forma local en el dispositivo y las añadirá a la colección de tipo Cancion
     */
    public void obtenerCanciones() {
        //Objeto de la clase ContentResolver utilizado para interactuar con los datos almacenados en el dispositivo
        ContentResolver contentResolver = getActivity().getContentResolver();

        //Objeto de la clase Uri utilizado para manejar los recursos en Android (en este caso para tener acceso a los datos)
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //Objeto de la clase Cursor utilizado para realizar una "query" para obtener la información de la música almacenada en el dispositivo
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //Comprobamos mediante un if el contenido del Cursor
        if (cursor != null && cursor.moveToFirst()) {

            //Almacenamos en variables de tipo int las columnas de los datos que queremos obtener de cada canción
            int columna_titulo = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE),
                    columna_id = cursor.getColumnIndex(MediaStore.Audio.Media._ID),
                    columna_artista = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST),
                    columna_data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA),
                    columna_duracion = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION),
                    columna_album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            //Mientras el Cursor tenga datos que leer...
            do {
                //Almacenamos en los tipos de variables el contenido de las columnas
                long id = cursor.getLong(columna_id);

                String titulo = cursor.getString(columna_titulo),
                        artista = cursor.getString(columna_artista),
                        data = cursor.getString(columna_data),
                        duracion = cursor.getString(columna_duracion),
                        nombre_album = cursor.getString(columna_album);

                Uri uri_imagen = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), columna_album);

                //Comprobaamos que el nombre de la canción no tenga la cadena "AUD" (para envitar añadir audios de WhatsApp)
                if (!titulo.contains("AUD")) {
                    Cancion cancion = new Cancion(uri_imagen, id, titulo, artista, data, duracion, nombre_album);
                    canciones.add(cancion);
                }
            } while (cursor.moveToNext());
        }

        //En caso de que el Cursor sea distino de NULL
        if (cursor != null) {
            //Cerramos el Cursor
            cursor.close();
        }
    }
}