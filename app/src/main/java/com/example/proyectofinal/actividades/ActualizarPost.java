package com.example.proyectofinal.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.clases.Foro;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente a la pantalla de actualizar el post
 */
public class ActualizarPost extends AppCompatActivity {
    //Declarams los elementos necesarios
    LottieAnimationView lottieAnimationView;
    TextInputEditText campo_titulo, campo_mensaje, campo_categoria;
    TextInputLayout layout_titulo, layout_mensaje, layout_categoria;
    MaterialButton boton_eliminar_post;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    ScrollView layout;
    private String email, id_documento, id_post;

    /**
     * Declaramos la funcionabilidad del botón de retroceso del dispositivo
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde se visualizará los post realizados por el usuario y enviamos la información necesaria
        Intent intent= new Intent(ActualizarPost.this, VisualizarPost.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_post);

        //Identificamos la animación
        lottieAnimationView = findViewById(R.id.imagen_actualizarPost);

        //Identificamos los TextInputEditText
        campo_titulo = findViewById(R.id.campoTitulo_actualizarPost);
        campo_mensaje = findViewById(R.id.campoMensaje_actualizarPost);
        campo_categoria = findViewById(R.id.campoCategoria_actualizarPost);

        //Identificamos los TextInputLayout
        layout_titulo = findViewById(R.id.layoutTitulo_actualizarPost);
        layout_mensaje = findViewById(R.id.layoutMensaje_actualizarPost);
        layout_categoria = findViewById(R.id.layoutCategoria_actualizarPost);

        //Identificamos el MaterialButton
        boton_eliminar_post = findViewById(R.id.botonEliminar_actualizarPost);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase CollectionRefence donde le pasamos el nombre de la colección "post"
        collectionReference = firestore.collection("post");

        //Identificamos el ScrollView
        layout = findViewById(R.id.layout_actualizarPost);

        //Creamos un Bundle para obtener la información que nos llega
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String)  bundle.get("ID_documento");
        id_post = (String) bundle.get("ID_post");

        //Creamos e instanciamos el objeto de la clase Foro donde guardaremos la información del foro que nos llega
        Foro foro = (Foro) getIntent().getSerializableExtra("Foro");

        //Añadimos la información a sus respectivos campos
        campo_titulo.setText(foro.getTitulo_mensaje());
        campo_mensaje.setText(foro.getMensaje());
        campo_categoria.setText(foro.getCategoria());

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

        //Añadimos la funcionabilidad al campo del titulo
        campo_titulo.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo del titulo, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_titulo.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_titulo.setError(null);
            }
        });

        //Añadimos la funcionabilidad al campo del mensaje
        campo_mensaje.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo del mensaje, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_mensaje.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_mensaje.setError(null);
            }
        });

        //Declaramos la funcionabilidad del botón de eliminar post
        boton_eliminar_post.setOnClickListener(new View.OnClickListener() {
            /**
             * Creamos un MaterialAlertDialog donde realizaremos las acciones pertinentes
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos e instanciamos un MaterialAlertDialog y le pasamos la información necesaria
                MaterialAlertDialogBuilder materialAlertDialogBuilder_confirmarcion = new MaterialAlertDialogBuilder(ActualizarPost.this);
                    materialAlertDialogBuilder_confirmarcion.setTitle("¿Quieres actualizar el post del apartado de foros?");
                    materialAlertDialogBuilder_confirmarcion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        /**
                         * Declaramos la funcionabilidad del botón positivo
                         * @param dialog the dialog that received the click
                         * @param which the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (campo_titulo.getText().toString().isEmpty() || campo_mensaje.getText().toString().isEmpty()) {
                                layout_titulo.setError("Debes de añadir un titulo");
                                layout_mensaje.setError("Debes de añadir un mensaje");
                            } else if (!campo_titulo.getText().toString().isEmpty() || !campo_mensaje.getText().toString().isEmpty()) {
                                //Creamos un Array de tipo String con las distintas categorias donde va a añadir el post
                                final String contenido = "General";
                                String[] opciones = {"General", "Albumes", "Artistas"};
                                final String[] contenido_seleccionado = {contenido};

                                //Creamos e instanciamos un MaterialAlertDialog y le pasamos la información necesaria
                                MaterialAlertDialogBuilder materialAlertDialogBuilder_seleccion_categoria = new MaterialAlertDialogBuilder(ActualizarPost.this);
                                    materialAlertDialogBuilder_seleccion_categoria.setTitle("Seleccione de una las tres categorias");
                                    materialAlertDialogBuilder_seleccion_categoria.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
                                        /**
                                         * Declaramos la funcionabilidad de cada opción del MaterialAlertDialog
                                         * @param dialog the dialog that received the click
                                         * @param which the button that was clicked (ex.
                                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                         *              of the item clicked
                                         */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            contenido_seleccionado[0] = opciones[which];
                                        }
                                    });

                                    materialAlertDialogBuilder_seleccion_categoria.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        /**
                                         * Declaramos la funcionabilidad del botón positivo
                                         * @param dialog the dialog that received the click
                                         * @param which the button that was clicked (ex.
                                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                         *              of the item clicked
                                         */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Almacenamos en la variable String "categoria" la opción seleccionada por el usuario
                                            String categoria = contenido_seleccionado[0];

                                            //Llamamos al método actualizarPost() donde le pasamos la información necesaria
                                            actualizarPost(campo_titulo.getText().toString(), campo_mensaje.getText().toString(), categoria);
                                        }
                                    });

                                    materialAlertDialogBuilder_seleccion_categoria.show();
                            }
                        }
                    });
                    materialAlertDialogBuilder_confirmarcion.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        /**
                         * Declaramos la funcionabilidad del botón negativo
                         * @param dialog the dialog that received the click
                         * @param which the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            materialAlertDialogBuilder_confirmarcion.setCancelable(true);
                            Snackbar snackbar_informacion = Snackbar.make(layout, "Post no actualizado", Snackbar.LENGTH_SHORT);
                            snackbar_informacion.show();
                        }
                    });

                    materialAlertDialogBuilder_confirmarcion.show();
            }
        });
    }

    /**
     * Método donde realizaremos la actualización del post
     * @param titulo Variable de tipo String donde almacena el titulo del post
     * @param mensaje Variable de tipo String donde almacena el mensaje del post
     * @param categoria Variable de tipo String donde almacena la categoria del post
     */
    public void actualizarPost(String titulo, String mensaje, String categoria) {
        //Creamos e instanciamos el objeto de la clase DocumentReference donde usamos el objeto de la clase FirebaseFireStore donde le indicamos el nombre de la colección y el ID del documento (ID del post a actualizar)
        DocumentReference documentReference = firestore.collection("post").document(id_post);

        //Creamos un Map donde le pasamos los datos actualizados
        Map<String, Object> map = new HashMap<>();
            map.put("titulo", titulo);
            map.put("mensaje", mensaje);
            map.put("categoria", categoria);

        //Usamos el objeto de la clase DocumentReferences seguido del método .update() donde le pasamos el Map con los datos
        documentReference.update(map);

        //Creamos un Intent que vaya a la actividad de visualizar los post donde le pasamos el email y el ID del documento del usuario
        Intent intent = new Intent(ActualizarPost.this, VisualizarPost.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }
}