package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Artista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la actividad de vializar la información del artista dependiendo de a que elemento haya pulsado el usuario en el listado de artista
 */
public class VisualizarArtista extends AppCompatActivity {
    //Declaramos los elementos
    ImageView imagen;
    MaterialButton boton_visualizacion_presentacion;
    TextView nombre_apellidos, informacion_basica;
    TextInputEditText biografia;
    String email, id_documento;
    ViewFlipper viewFlipper_artista, viewFlipper_album;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    private String presentacion, letra_cancion;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
        Intent intent = new Intent(VisualizarArtista.this, PantallaPrincipal.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_artista);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos el ImageView
        imagen = findViewById(R.id.imagenArtista_visualizarArtista);

        //Identificamos el MaterialButton
        boton_visualizacion_presentacion = findViewById(R.id.botonVisualizacionPresentacion_visualizarArtista);

        //Identificamos los TextInputLayout
        nombre_apellidos = findViewById(R.id.nombreApellidosArtista_visualizarArtista);
        informacion_basica = findViewById(R.id.informacionBasicaArtista_visualizarArtista);
        biografia = findViewById(R.id.campoBiografia_visualizarArtista);

        //Identificamos los ViewFlipper
        viewFlipper_artista = findViewById(R.id.viewFlipper_artista);
        viewFlipper_album = findViewById(R.id.viewFlipper_album);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el CollectionReference donde le pasamos la colección "artista"
        collectionReference = firestore.collection("artista");

        //Declaramos e instanciamos un objeto de la clase Bundle para obtener el email del usuario quer ha iniciado sesión
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");

        //Creamos un ArrayList de tipo String donde contendrá las URL de las imagenes del artista y otro ArrayList donde contendrá la URL de la imagenes de los albums
        ArrayList<String> url_imagenes_artista = new ArrayList<>(), url_imagenes_album = new ArrayList<>();

        //Creamos un objeto de la clase Artista y recuperamos el objeto que nos llegar del fragento "artista"
        Artista artista = (Artista) getIntent().getSerializableExtra("Artista");

        //Creamos e instanciamos el objeto de la clase Query donde indicamos que recupere el ID del documento cuyo nombre del artista sea el indicado
        Query query = collectionReference.whereEqualTo("nombre", artista.getNombre());

        //Usamos el método .get() para obtener la información necesaria de dicho documento
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Método donde haremos la gestión necesaria para obtener las imagenes del artista
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Recorremos la información necesaria y la añadimos en su respectivo ArrayList y obtenemos el link de la presentación en la variable indicada
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    url_imagenes_artista.add(documentSnapshot.getString("imagenArtista1"));
                    url_imagenes_artista.add(documentSnapshot.getString("imagenArtista2"));
                    url_imagenes_artista.add(documentSnapshot.getString("imagenArtista3"));
                    url_imagenes_artista.add(documentSnapshot.getString("imagenArtista4"));

                    url_imagenes_album.add(documentSnapshot.getString("imagenAlbum1"));
                    url_imagenes_album.add(documentSnapshot.getString("imagenAlbum2"));
                    url_imagenes_album.add(documentSnapshot.getString("imagenAlbum3"));
                    url_imagenes_album.add(documentSnapshot.getString("imagenAlbum4"));

                    presentacion = documentSnapshot.getString("presentacion");
                    letra_cancion = documentSnapshot.getString("letraCancion");
                }

                //Recorremos el ArrayList de imagenes del artista y llamamos al método donde realizará la gestión de dichas imágenes
                for (String imagen : url_imagenes_artista) {
                    //Llamamos al método para gestionar las imagenes del artista donde le pasamos la variable que contiene la URL de la imagen
                    flipperImage(imagen, true);
                }

                //Recorremos el ArrayList de imagenes del album y llamamos al método donde realizará la gestión de dichas imágenes
                for (String imagen : url_imagenes_album) {
                    //Llamamos al método para gestionar las imagenes del artista donde le pasamos la variable que contiene la URL de la imagen
                    flipperImage(imagen, false);
                }

                //Declaramos la funcionabilidad del botón de visualizar la presentación
                boton_visualizacion_presentacion.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Creamos un Intent que vaya a la pantalla de visualización de la presentación
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        //Creamos un Intent que vaya a la actividad de visualización de la presentación donde le enviamos la información necesaria
                        Intent intent = new Intent(VisualizarArtista.this, Presentacion.class);
                            intent.putExtra("presentacion", presentacion);
                            intent.putExtra("letra_cancion", letra_cancion);
                            intent.putExtra("Artista", artista);
                            intent.putExtra("ID_documento", id_documento);
                        startActivity(intent);
                    }
                });
            }
        });

        //Añadimos la información correspondiente a los campos
        Glide.with(VisualizarArtista.this).load(artista.getImagen()).into(imagen);
        nombre_apellidos.setText(artista.getNombre() + " " + artista.getApellidos());
        informacion_basica.setText(artista.getNumero_albums() + " Álbumes ● " + artista.getFecha_nacimiento() + " ● " + artista.getNacionalidad() + " ● " + artista.getGenero_musical());
        biografia.setText(artista.getBiografia());
    }

    /**
     * Método donde añadiremos la imagenes a su respectivo carrusel
     * @param imagen Variable de tipo String donde almacenerá la URL de la imagen a mostrar
     */
    public void flipperImage(String imagen, boolean artista) {
        //Creamos e instanciamos el ImageView donde le pasamos el contexto
        ImageView imageView = new ImageView(VisualizarArtista.this);

        //Usamos la implementación Glide donde le indicamos el texto, la URL de la imagen y donde la tiene que mostrar
        Glide.with(VisualizarArtista.this).load(imagen).into(imageView);

        if (artista == true) {
            //Usamos el objeto de la clase ViewFlipper donde indicamos que elemento es el que tiene que mostrar
            viewFlipper_artista.addView(imageView);

            //Usamos el objeto de la clase ViewFlipper donde indicamos la duración entre las distintas imagenes del carrusel
            viewFlipper_artista.setFlipInterval(2000);

        } else if (artista == false) {
            //Usamos el objeto de la clase ViewFlipper donde indicamos que elemento es el que tiene que mostrar
            viewFlipper_album.addView(imageView);

            //Usamos el objeto de la clase ViewFlipper donde indicamos la duración entre las distintas imagenes del carrusel
            viewFlipper_album.setFlipInterval(2000);
        }
    }
}