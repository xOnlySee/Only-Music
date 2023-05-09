package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.adaptadores.AdaptadorForoVisualizar;
import com.example.proyectofinal.clases.Foro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente a la actividad de visualizar los post realizados por el usuario
 */
public class VisualizarPost extends AppCompatActivity {
    //Declaramos los elementos necesarios
    FirebaseFirestore firestore;
    LottieAnimationView animacion;
    TextView texto_post;
    ConstraintLayout layout;
    CollectionReference collectionReference;
    RecyclerView recyclerView;
    AdaptadorForoVisualizar adaptadorForoVisualizar;
    RecyclerView.LayoutManager layoutManager;
    private String email, id_documento, id_post;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
        Intent intent = new Intent(VisualizarPost.this, PantallaPrincipal.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_post);

        //Creamos un ArrayList de tipo Foro
        ArrayList<Foro> foros = new ArrayList<>();

        //Declaramos la animacion
        animacion = findViewById(R.id.imagen_visualizarPost);

        texto_post = findViewById(R.id.textoCancion_fragmentoCanciones);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase CollectionRefenre donde le pasamos la coleccion "post"
        collectionReference = firestore.collection("post");

        //Identificamos el ConstraintLayout
        layout = findViewById(R.id.layout_visualizarPost);

        //Creamos un Bundle donde obtendremos la información que nos llega
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");

        //Creamos e instanciamos el objeto de la clase Query donde buscamos los "posts" donde la categoria de estos sean "General"
        Query query = collectionReference.whereEqualTo("email", email);

        //Usamos el objeto Query donde usamos el método .get() para obtener los datos del documento
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Método donde realizaremos las acciones pertinenes en caso de que el proceso se haya completado de forma exitosa
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Recorremos los datos obtenidos y por cada dato, obtenemos el nombre del usuario
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Foro foro = new Foro(documentSnapshot.getString("fechaPublicacion"), documentSnapshot.getString("titulo"), documentSnapshot.getString("mensaje"), documentSnapshot.getString("categoria"));
                    foros.add(foro);
                }

                //Comprobamos el contenido del ArrayList

                //En caso de que el ArrayList este vacio (el usuario no ha realizado ningun post)
                if (foros.isEmpty()) {
                    //Añadimos el recurso a la animación
                    animacion.setAnimation("126314-empty-box-by-partho.json");

                    //Iniciamos la animación
                    animacion.playAnimation();

                    //Indicamos por medio del TextView que el usuario no ha realizado ningun post
                    texto_post.setText("No has realizado ningun post");

                //En caso de que el ArrayList no esta vacio (el usuario ha realizado post)
                } else if (!foros.isEmpty()) {
                    //Instanciamos el objeto de la clase AdaptadorForo
                    adaptadorForoVisualizar = new AdaptadorForoVisualizar(foros, VisualizarPost.this, R.layout.listado_foro_visualizar);

                    //Añadimos la funcionabilidad de cada item del adaptador
                    adaptadorForoVisualizar.setOnClickListener(new View.OnClickListener() {
                        /**
                         * Añadimos la funcionabilidad por cada item del RecyclerView
                         * @param v The view that was clicked.
                         */
                        @Override
                        public void onClick(View v) {
                            //Cremos e instanciamos un objeto "Foro" donde almacenamos la información del item que haya pulsado del RecyclerView
                            Foro foro = foros.get(recyclerView.getChildAdapterPosition(v));

                            //Creamos un Array de tipo String con las distintas categorias donde va a añadir el post
                            String[] opciones = {"Eliminar", "Actualizar"};
                            String contenido = "";

                            final String[] contenido_seleccionado = {contenido};

                            //Creamos un MaterialAlertDialog donde mostraremos al usuario las distintas opciones
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(VisualizarPost.this);
                            materialAlertDialogBuilder.setTitle("Seleccione una de las opciones");

                            materialAlertDialogBuilder.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
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

                            materialAlertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                /**
                                 * Declaramos la funcionabilidad del botón positivo
                                 * @param dialog the dialog that received the click
                                 * @param which the button that was clicked (ex.
                                 *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                 *              of the item clicked
                                 */
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String opcion = contenido_seleccionado[0];

                                    obtenerIdPost(opcion, foro);
                                }
                            });

                            materialAlertDialogBuilder.show();
                        }
                    });

                    //Instanciamos el objeto RecyclerView  y lo identificamos
                    recyclerView = findViewById(R.id.listadoForo_visualizarPost);

                    //Usamos el objeto RecyclerView donde usamos el método .setAdapter() donde le pasamos el adaptador
                    recyclerView.setAdapter(adaptadorForoVisualizar);

                    //Instanciamos el objeto LayoutManager
                    layoutManager = new LinearLayoutManager(VisualizarPost.this);

                    //Usamos el objeto RecyclerView donde usamos el método .setLayoutManager() donde le pasamos el LaytoutManager
                    recyclerView.setLayoutManager(layoutManager);
                }
            }
        });
    }

    /**
     * Método donde obtendremos el ID del post que el usuario haya pulsado
     * @param opcion Variable de tipo String donde almacená la opción elegida por el usuario
     * @param foro Objeto de la clase Foro
     */
    public void obtenerIdPost(String opcion, Foro foro) {
        //Creamos e instanciamos un objeto de la clase Query donde indicamos todos los datos posibles para filtrar y obtener el ID del post en cuestión
        Query query = collectionReference.whereEqualTo("email", email).whereEqualTo("titulo", foro.getTitulo_mensaje()).whereEqualTo("mensaje", foro.getMensaje()).whereEqualTo("fechaPublicacion", foro.getFecha_publicacion());

        //Usamos el objeto de la clase Query donde usamos el método .get() para obtener el ID del post
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Delcaramos la funcionabilidad en caso de que haya podido obtener el ID del post
             * @param task Objeto de la clase task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    //Almacenamos en la variable String el ID del post obtenido
                    id_post = documentSnapshot.getId();
                }

                //Hacemos un Switch sobre la variable "opcion"
                switch (opcion) {
                    //En caso de que el usuario haya elegido la opción "Eliminar"
                    case "Eliminar":
                        //Usamos el objeto de la clase CollectionReference donnde le indicamos el ID del post y el método .delete() para eliminar el post
                        collectionReference.document(id_post).delete();

                        //Creamos e instanciamos una Snackbar para informar al usuario de que el post ha sido eliminado
                        Snackbar snackbar = Snackbar.make(layout, "Se ha eliminado el post", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
                        Intent intent_visualizar_post = new Intent(VisualizarPost.this, VisualizarPost.class);
                            intent_visualizar_post.putExtra("email", email);
                            intent_visualizar_post.putExtra("ID_documento", id_documento);
                        startActivity(intent_visualizar_post);
                        break;

                    //En caso de que el usuario haya elegido la opcion "Actualizar"
                    case "Actualizar":
                        //Creamos un Intent que vaya a la pantalla de actualizar post y le enviamos la información necesaria
                        Intent intent_actualizar_post = new Intent(VisualizarPost.this, ActualizarPost.class);
                            intent_actualizar_post.putExtra("email", email);
                            intent_actualizar_post.putExtra("ID_documento", id_documento);
                            intent_actualizar_post.putExtra("ID_post", id_post);
                            intent_actualizar_post.putExtra("Foro", foro);
                        startActivity(intent_actualizar_post);
                        break;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            /**
             * Declaramos la funcionabilidad en caso de que no se haya podido recuperar el ID del post
             * @param e Objeto de la clase Exception
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                //Creamos e instanciamos una Snackbar donde informaremos al usuario
                Snackbar snackbar = Snackbar.make(layout, "Se ha producido un error", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}