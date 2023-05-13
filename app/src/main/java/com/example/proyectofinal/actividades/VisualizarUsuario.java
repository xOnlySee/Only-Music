package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.adaptadores.AdaptadorForoVisualizar;
import com.example.proyectofinal.clases.Foro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la visualización de los usuarios
 */
public class VisualizarUsuario extends AppCompatActivity {
    //Declaramos los elementos necesarios
    ImageView imagen;
    FloatingActionButton boton_email;
    TextView texto_perfil, texto_post;
    TextInputEditText campo_nombre_apellidos, campo_edad, campo_biografia;
    FirebaseFirestore firestore;
    CollectionReference collectionReference_perfil, collectionReference_post;
    RecyclerView recyclerView;
    AdaptadorForoVisualizar adaptadorForoVisualizar;
    RecyclerView.LayoutManager layoutManager;
    private String email_inicio_sesion, email_visualizar_usuario, id_documento, url_imagen;

    /**
     * Delcaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde contiene los fragmentos y enviamos el email y el ID del documento
        Intent intent = new Intent(VisualizarUsuario.this, PantallaPrincipal.class);
        Log.i("ID documento", id_documento);
            intent.putExtra("email", email_inicio_sesion);
            intent.putExtra("ID_documento", id_documento);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_usuario);

        //Identificamos el ImageView
        imagen = findViewById(R.id.imagen_visualizarUsuario);

        //Identificamos el FloatingActionButton
        boton_email = findViewById(R.id.botonEmail_visualizarUsuario);

        //Identificamos los TextInputEditText
        campo_nombre_apellidos = findViewById(R.id.campoNombreApellido_visualizarUsuario);
        campo_edad = findViewById(R.id.campoEdad_visualizarUsuario);
        campo_biografia = findViewById(R.id.campoBiografia_visualizarUsuario);

        //Identificamos el RecyclerView
        recyclerView = findViewById(R.id.listadoForo_visualizarUsuario);

        //Identificamos los TextView
        texto_perfil = findViewById(R.id.textoPerfil_visualizarUsuario);
        texto_post = findViewById(R.id.textoPost_visualizarUsuario);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos los objetos de la clase CollectionRefence
        collectionReference_perfil = firestore.collection("perfil");
        collectionReference_post = firestore.collection("post");

        //Creamos un Bundle donde obtendremos la información que nos llega
        Bundle bundle = getIntent().getExtras();
        email_inicio_sesion = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");
        email_visualizar_usuario = (String) bundle.get("email_usuario");

        //Añadimos la funcionabilidad del botón del email
        boton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos un Intent implicito donde añadiremos el email del destinatario con el asunto "Only Music"
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email_visualizar_usuario});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Only Music");
                startActivity(intent);
            }
        });

        //Creamos e instanciamos el ArrayList de tipo Foro
        ArrayList<Foro> foros = new ArrayList<>();

        //Creamos una Query que apunte a la colección "perfil" donde le indicamos que el campo "email" (de la base de datos) sea igual que el email de quien ha realizado la publicación
        Query query_perfil = collectionReference_perfil.whereEqualTo("email", email_visualizar_usuario);

        //Usamos el método .get() para obtener los datos
        query_perfil.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Añadimos la funcionabilidad en caso de que se haya llevado de forma correcta
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Creamos un bucle for donde recorreremos los datos del documento y añadimos dicha información a los campos correspondientes
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    campo_nombre_apellidos.setText(documentSnapshot.getString("nombreApellidos"));
                    campo_edad.setText(documentSnapshot.getString("edad"));
                    campo_biografia.setText(documentSnapshot.getString("biografia"));

                    texto_perfil.setText("Perfil de " + documentSnapshot.getString("nombreUsuario"));
                    url_imagen = documentSnapshot.getString("imagenPerfil");
                }

                //Creamos un Query que apunte a la colección "post" donde le indicamos que el campo "email" (de la base de datos) sea igual de quien ha realizado la publicación del post
                Query query_post = collectionReference_post.whereEqualTo("email", email_visualizar_usuario);

                //Usamos el método .get() para obtener los datos
                query_post.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * Declaramos la funcionabilidad en caso de que se haya llevado de forma correcta
                     * @param task Objeto de la clase Task
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Creamos un bucle for donde recorremos los datos del documento y añadimos dicha información al ArrayList de tipo Foro
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Foro foro = new Foro(documentSnapshot.getString("fechaPublicacion"), documentSnapshot.getString("titulo"), documentSnapshot.getString("mensaje"), documentSnapshot.getString("categoria"));
                            foros.add(foro);
                        }

                        //Usamos la implementación Glide donde le indicamos el contexto, la URL de la imagen y donde queremos mostrar la imagen
                        Glide.with(VisualizarUsuario.this).load(url_imagen).into(imagen);

                        //Instanciamos el Adaptador donde le pasamos el ArrayList, el contexto y donde queremos que se muestren
                        adaptadorForoVisualizar = new AdaptadorForoVisualizar(foros, VisualizarUsuario.this, R.layout.listado_foro_visualizar);

                        //Usamos el objeto RecyclerView donde usamos el métod .setAdapter() donde le pasamos el adaptador instanciado
                        recyclerView.setAdapter(adaptadorForoVisualizar);

                        //Instanciamos el objeto de la clase LayoutManager donde le pasamos el contexto
                        layoutManager = new LinearLayoutManager(VisualizarUsuario.this);

                        //Usamos el RecyclerView seguido del método .setLayoutManager() donde le pasamos el objeto de la clase LayoutManager instanciada anteriormente
                        recyclerView.setLayoutManager(layoutManager);
                    }
                });
            }
        });
    }
}