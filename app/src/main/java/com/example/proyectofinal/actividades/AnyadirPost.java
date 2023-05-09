package com.example.proyectofinal.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.fragmentos.FragmentoCanciones;
import com.example.proyectofinal.fragmentos.FragmentoForo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la actividad de añadir un nuevo post
 */
public class AnyadirPost extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText campo_titulo, campo_mensaje;
    TextInputLayout layout_titulo, layout_mensaje;
    MaterialButton boton_continuar;
    ScrollView layout;
    FirebaseFirestore firestore;
    LottieAnimationView lottieAnimationView;
    private String email, id_documento;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
        Intent intent = new Intent(AnyadirPost.this, PantallaPrincipal.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_post);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos los TextInputEditText
        campo_titulo = findViewById(R.id.campoTitulo_pantallaAnyadirPost);
        campo_mensaje = findViewById(R.id.campoMensaje_pantallaAnyadirPost);

        //Identificamos los TextInputLayout
        layout_titulo = findViewById(R.id.layoutTitulo_pantallaAnyadirPost);
        layout_mensaje = findViewById(R.id.layoutMensaje_pantallaAnyadirPost);

        //Identificamos el MaterialButton
        boton_continuar = findViewById(R.id.botonConfirmar_pantallaAnyadirPost);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.layout_pantallaAnyadirPost);

        //Identificamos el objeto LottieAnimationView
        lottieAnimationView = findViewById(R.id.imagen_pantallaAnyadirPost);

        //Usamos el objeto LottieAnimationView donde indicamos que el búcle de la animación quede desactivado
        lottieAnimationView.loop(false);

        //Creamos un Bundle para obtener el email del usuario que ha iniciado sesión y el ID del documento
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Creamos e instanciamos una Snackbar para avisar de que solo se podrán añadir 300 caracteres en el mensaje del post
        Snackbar snackbar = Snackbar.make(layout, "Solo podrás añadir 300 caracteres al mensaje del post", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Entendido", new View.OnClickListener() {
            /**
             * Añadimos funcionabilidad del botón de añadir post
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

        //Declaramos la funcionabilidad del botón confirmar
        boton_continuar.setOnClickListener(new View.OnClickListener() {
            /**
             * Comprobamos la información de los campos mediante un if-else. En caso de que tenga información realizaremos la inserción en FireBase
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Comprobamos mediante un if-else la información de los campos

                //En caso de que los campos esten vacios
                if (campo_titulo.getText().toString().isEmpty() || campo_mensaje.getText().toString().isEmpty()) {
                    //Añadimos los mensajes de error correspondiente
                    if (campo_titulo.getText().toString().isEmpty()) { layout_titulo.setError("Debes de introducir el titulo del mensaje"); }
                    if (campo_mensaje.getText().toString().isEmpty()) { layout_mensaje.setError("Debes de introducir el mensaje"); }

                //En cualquier otro caso
                } else {
                    final String contenido = "General";

                    //Creamos un Array de tipo String con las distimntas categorias donde va a añadir el post
                    String[] opciones = {"General", "Albumes", "Artistas"};

                    final String[] contenido_seleccionado = {contenido};

                    //Creamos un MaterialAlertDialog y añadimos la configuración necesaria
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AnyadirPost.this);
                        materialAlertDialogBuilder.setTitle("Seleccione unas de las categorias donde quieras añadir el post");

                        materialAlertDialogBuilder.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() { //Método para añadir las opciones que el usuario tendrá disponible
                            /**
                             * Declaramos la funcionabilidad del usuario dependiendo de que opción pulse el usuario
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               contenido_seleccionado[0] = opciones[i];
                            }
                        });

                        materialAlertDialogBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            /**
                             * Añadimos la funcionabilidad del botón positivo
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String categoria = contenido_seleccionado[0];

                                anyadirPost(campo_titulo.getText().toString(), campo_mensaje.getText().toString(), email, categoria);
                            }
                        });
                        materialAlertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            /**
                             * Añadimos la funcionabilidad del botón negativo
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                materialAlertDialogBuilder.setCancelable(true);
                            }
                        });
                        materialAlertDialogBuilder.show();
                }
            }
        });
    }

    /**
     * Método para añadir un nuevo post a la colección correspondiente
     * @param titulo Variable de tipo String donde almacena el titulo del post
     * @param mensaje Variable de tipo String donde almacena el mensaje del post
     * @param email Variable de tipo String donde almacena el email de quien publica el post
     * @param categoria Variable de tipo String donde almacena la categoria del post
     */
    public void anyadirPost(String titulo, String mensaje, String email, String categoria) {
        //Creamos un Mao donde añadimos los campos que queremos crear denmtro de la colección "post"
        Map<String, Object> map = new HashMap<>();
            map.put("categoria", categoria);
            map.put("email", email);
            map.put("fechaPublicacion", String.valueOf(LocalDate.now()));
            map.put("mensaje", mensaje);
            map.put("titulo", titulo);

        //Usamos el objeto de la clase FireBaseFireStore donde le indicamos el nombre de la colección y la colección que queremos añadir
        firestore.collection("post").add(map);
    }
}