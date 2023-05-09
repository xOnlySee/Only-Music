package com.example.proyectofinal.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Artista;
import com.google.android.material.textfield.TextInputEditText;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la actividad de visualizar la presentación de un determinado artista
 */
public class Presentacion extends AppCompatActivity {
    //Declaramos los elementos
    VideoView videoView;
    MediaController mediaController;
    TextInputEditText campo_letra_cancion;
    LottieAnimationView animacion;
    Artista artista;
    private String email, id_documento, presentacion, letra_cancion;
    private boolean reproduccion = false;

    /**
     * Método donde añadiremos la funcionabilidad del botón de retroceso
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Creamos un Intent que vaya a la actividad de visualización del artista donde le enviamos el objeto "Artista" y el ID del documento
        Intent intent = new Intent(Presentacion.this, VisualizarArtista.class);
            intent.putExtra("Artista", artista);
            intent.putExtra("ID_documento", id_documento);
            intent.putExtra("email", email);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamsos el TextView
        campo_letra_cancion = findViewById(R.id.campoLetra_presentacion);

        //Identificamos el VideoView
        videoView = findViewById(R.id.videoView_presentacion);

        //Identidicamos el LottieAnimationView
        animacion = findViewById(R.id.imagen_presentacion);

        //Creamos un objeto de la clase Bundle donde recuperaremos la información necesaria
        Bundle bundle = getIntent().getExtras();
        id_documento = (String) bundle.get("ID_documento");
        email = (String) bundle.get("email");
        presentacion = (String) bundle.get("presentacion");
        letra_cancion = (String) bundle.get("letra_cancion");

        //Almacenamos en el objeto de la clase "Artista" la información que nos llega
        artista = (Artista) getIntent().getSerializableExtra("Artista");

        //Instanciamos el objeto de la clase MediaController donde le pasamos el contexto
        mediaController = new MediaController(Presentacion.this);

        //Usamos el objeto de la clase VideoView donde usamos el método.setMediaController() donde le pasamos el objeto de la clase instanciada MediaController
        videoView.setMediaController(mediaController);

        //Usamos el objeto de la clase MediaController para añadir el objeto de la clase VideoView
        mediaController.setAnchorView(videoView);

        //Creamos e instanciamos el objeto de la clase Uri donde lo instanciamos usando la variable String que contiene la URL del vídeo
        Uri uri = Uri.parse(presentacion);

        //Usamos el objeto de la clase VideoView donde le indicamos el objeto de la clase Uri
        videoView.setVideoURI(uri);

        //Usamos el objeto de la clase VideoView para iniciar el vídeo
        videoView.start();

        //Añadimos la información necesaria al TextView
        campo_letra_cancion.setText(letra_cancion);
    }
}