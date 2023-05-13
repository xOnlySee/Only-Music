package com.example.proyectofinal.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Cancion;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde con la reproducción de una canción
 */
public class Reproduccion extends AppCompatActivity {
    //Declaramos los elementos
    TextView titulo_cancion, artista_cancion;
    LottieAnimationView animacion, boton_anterior, boton_play, boton_siguiente;
    SeekBar barra;
    MediaPlayer mediaPlayer;
    ConstraintLayout layout;
    ArrayList<Cancion> canciones;
    Cancion cancion;
    final Handler handler = new Handler();
    private String email, id_documento;
    private int posicion;

    /**
     * Añadimos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Paramos la canción que se este reproducciendo
        mediaPlayer.stop();

        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
        Intent intent = new Intent(Reproduccion.this, PantallaPrincipal.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduccion);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos el ImageView
        animacion = findViewById(R.id.imagenCancion_reproduccion);

        //Identificamos los TextView
        titulo_cancion = findViewById(R.id.tituloCancion_reproduccion);
        artista_cancion = findViewById(R.id.artistaCancion_reproduccion);

        //Identificamos los Button
        boton_anterior = findViewById(R.id.botonAnterior_reproduccion);
        boton_play = findViewById(R.id.botonPlay_reproduccion);
        boton_siguiente = findViewById(R.id.botonSiguiente_reproduccion);

        //Identificamos el Seekbar
        barra = findViewById(R.id.barra_reproduccion);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.layout_reproduccion);

        //Creamos un Bundle para obtener el email de quien ha iniciado sesión
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");

        //Almacenamos en la variable int la posición del elemento que el usuario ha pulsado en la lista
        posicion = getIntent().getIntExtra("posicion", 0);

        //Almacenamos en el ArrayList el listado de las canciones
        canciones = getIntent().getParcelableArrayListExtra("canciones");

        //Instanciamos el objeto de la clase Cancion con la canción del ArrayList según la posición
        cancion = canciones.get(posicion);

        //Instanciamos el objeto de la clase MediaPlayer
        mediaPlayer = new MediaPlayer();

        //Añadimos la información a los TextView
        titulo_cancion.setText(cancion.getTitulo());
        artista_cancion.setText(cancion.getArtista());

        //Dentro del try-catch inicializamos la canción usando los métododos de la clase MediaPlayer
        try {
            //Indicamos la ruta del archivo
            mediaPlayer.setDataSource(cancion.getRuta());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Usamos el método .setMax() (SeekBar) donde le pasamos la duración de la canción
        barra.setMax(mediaPlayer.getDuration());

        //Deshabilitamos el funcionamiento del SeekBar
        barra.setEnabled(false);

        //Creamos e instanciamos el objeto de la clase que hemos creado dentro del archivo .java
        BarraMusica barraMusica = new BarraMusica();

        //Usamos el handler donde le pasamos la SeekBar
        handler.post(barraMusica);

        //Declaramos la funcinabilidad al botón play
        boton_play.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del botón del play. Cuando el botón sea pulsado, se reproducirá la canción
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //En caso de que la canción se este reproduciendo
                if (mediaPlayer.isPlaying()) {
                    //Usamos el método .setMinAndMaxFrame() donde le pasamos el frame inicial y el frame final de la animación e iniciamos la animación del botón play
                    boton_play.setMinAndMaxFrame(38, 67);
                    boton_play.playAnimation();

                    //Iniciamos el frame de la imagen a 0, inciamos la animación (para que se reinicie) y cuando la animación este en el primer frame, cancelamos la animación 
                    animacion.setMinFrame(0);
                    animacion.playAnimation();
                    animacion.cancelAnimation();

                    //Ponemos en pausa la canción
                    mediaPlayer.pause();

                //En cualquier otro caso
                } else {
                    //Usamos el método .setMinAndMaxFrame() donde le pasamos el frame inicial y el frame final de la animación e iniciamos la animación del botón play
                    boton_play.setMinAndMaxFrame(0, 33);
                    boton_play.playAnimation();

                    //Usamos la imagen donde indicaremos que mienestras la aplicación este reproducciendo, la animación estará en búcle e iniciaremos la animación
                    animacion.loop(true);
                    animacion.playAnimation();

                    //Reproducimos la canción
                    mediaPlayer.start();
                }
            }
        });

        //Declaramos la funcionabilidad del botón siguiente
        boton_siguiente.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del botón siguiente. Cuando el botón sea pulsado, reproduciremos la siguiente canción
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Comprobamos el valor de la variable "posicion"

                //En caso de que el siguiente valor sea igual que el tamaño del ArrayList donde contiene las canciones
                if (posicion+1 == canciones.size()) {
                    //Creamos e instanciamos una Snackbar para informar al usuario de que no hay más canciones
                    Snackbar snackbar = Snackbar.make(layout, "No hay más canciones", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                //En caso de que el siguiente valor sea menor que el tamaño del ArrayList de canciones
                } else if (posicion +1 < canciones.size()) {
                    //Aumentamos en uno el valor de la variable "posicion"
                    posicion ++;

                    //Paramos la canción
                    mediaPlayer.stop();

                    //Reproduccimos la animación del botón siguiente
                    boton_siguiente.playAnimation();

                    //Volvemos a instanciar el objeto de la clase MediaPlayer (ya que cuando cambiamos la canción el objeto se libera)
                    mediaPlayer = new MediaPlayer();

                    //Dentro del try-catch realizamos las operaciones pertinentes
                    try {
                        //Instanciamos el objeto de la clase Cancion con la nueva canción
                        cancion = canciones.get(posicion);

                        //Añadimos la información actualizada a los TextView
                        titulo_cancion.setText(cancion.getTitulo());
                        artista_cancion.setText(cancion.getArtista());

                        //Usamos los métodos de la clase MediaPlayer para configurar la nueva canción
                        mediaPlayer.setDataSource(cancion.getRuta());
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (Exception ignored) {}
                }
            }
        });

        //Declaramos la funcionabilidad del botón anterior
        boton_anterior.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del botón anterior. Cuando el botón sea pulsado, reproduciremos la canción anterior
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Reducimos la posicón en -1 y paramos la canción
                posicion --;
                mediaPlayer.stop();

                //Reproduccimos la animación del botón anterior
                boton_anterior.playAnimation();

                //Volvemos a instar el objeto de la clase MediaPlayer (ya que cuando cambiamos de canción, el objeto se libera)
                mediaPlayer = new MediaPlayer();

                //Dentro del try-cath realizamos las operaciones pertinentes
                try {
                    //Instanciamos el objeto de la clase Cancion con la nueva canción
                    cancion = canciones.get(posicion);

                    //Añadimos la información actualizada a los TextView
                    titulo_cancion.setText(cancion.getTitulo());
                    artista_cancion.setText(cancion.getArtista());

                    //Usamos los métodos de la clase MediaPlayer para configurar la nueva canción
                    mediaPlayer.setDataSource(cancion.getRuta());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    //En caso de que no haya más canciones en el ArrayList, mostrará una Snackbar para informar al usuario
                    Snackbar snackbar = Snackbar.make(layout, "No hay más canciones", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    /**
     * Clase donde podremos dar funcionalidad a la barra de música (SeekBar) para que avance la barra a medida que avance la música
     */
    public class BarraMusica implements Runnable {
        /**
         * Declaramos la funciones que realizará el hilo cuando sea lanzado
         */
        @Override
        public void run() {
            //Usamos el SeekBar para establecer en todo momento la duración de la canción actual
            barra.setProgress(mediaPlayer.getCurrentPosition());

            //Usamos el objeto de la clase Handler donde le pasamos el contexto y la cantidad que va a ir aumentando
            handler.postDelayed(this, 100);

            //Comprobamos mediante un if si la canción ha finalizado. En caso de que haya finalizado reproducirá de forma automática la siguiente canción
            if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {
                //Aumentamos en +1 la posición y paramos la canción
                posicion ++;
                mediaPlayer.stop();

                //Reproduccimos la animación del botón siguiente
                boton_siguiente.playAnimation();

                //Volvemos a instanciar el objeto de la clase MediaPlayer (ya que cuando cambiamos la canción el objeto se libera)
                mediaPlayer = new MediaPlayer();

                //Dentro del try-catch realizamos las operaciones pertinentes
                try {
                    //Instanciamos el objeto de la clase Cancion con la nueva canción
                    cancion = canciones.get(posicion);

                    //Añadimos la información actualizada a los TextView
                    titulo_cancion.setText(cancion.getTitulo());
                    artista_cancion.setText(cancion.getArtista());

                    //Usamos los métodos de la clase MediaPlayer para configurar la nueva canción
                    mediaPlayer.setDataSource(cancion.getRuta());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    //En caso de que no haya más canciones en el ArrayList, mostrará una Snackbar para informar al usuario
                    Snackbar snackbar = Snackbar.make(layout, "No hay más canciones", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }
    }
}