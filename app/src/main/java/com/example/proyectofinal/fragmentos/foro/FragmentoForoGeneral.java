package com.example.proyectofinal.fragmentos.foro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.R;
import com.example.proyectofinal.actividades.VisualizarUsuario;
import com.example.proyectofinal.adaptadores.AdaptadorForo;
import com.example.proyectofinal.clases.Foro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Fragmento que representa el apartado del foro "General"
 */
public class FragmentoForoGeneral extends Fragment {
    //Declaramos los elementos necesarios
    FirebaseFirestore firestore;
    CollectionReference collectionReference_foro, collectionReference_perfil;
    RecyclerView recyclerView;
    AdaptadorForo adaptadorForo;
    RecyclerView.LayoutManager layoutManager;
    View view;
    private String email, id_documento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instanciamos el objeto de la clase View
        view = inflater.inflate(R.layout.fragment_fragmento_foro_general, container, false);

        //Creamos un Bundle donde almacenaremos en sus respectivas variables, el ID del documento y el email
        Bundle bundle = getArguments();
        id_documento = bundle.getString("ID_documento");
        email = bundle.getString("email");

        //Creamos un ArrayList de tipo Foro
        ArrayList<Foro> foros = new ArrayList<>();

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase CollectionReference donde le pasamos la colección "post"
        collectionReference_foro = firestore.collection("post");

        //Instanciamos el objeto de la clase CollectionReference donde le pasamos la colección "perfil"
        collectionReference_perfil = firestore.collection("perfil");

        //Creamos e instanciamos el objeto de la clase Query donde buscamos los "posts" donde la categoria de estos sean "General"
        Query query_foro = collectionReference_foro.whereEqualTo("categoria", "General");

        //Usamos el objeto Query donde usamos el método .get() para obtener los datos del documento
        query_foro.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Método donde realizaremos las acciones pertinenes en caso de que el proceso se haya completado de forma exitosa
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Recorremos los datos obtenidos y por cada dato, obtenemos el nombre del usuario
                for (QueryDocumentSnapshot documentSnapshot_foro : task.getResult()) {
                    //Creamos e instanciamos la clase Query donde buscamos la información del usuario donde el campo "email" sea igual que los "email" de la colección "Post"
                    Query query_perfil = collectionReference_perfil.whereEqualTo("email", documentSnapshot_foro.getString("email"));

                    //Usamos el objeto Query donde usamos el método .get() para obtener los datos del documento
                    query_perfil.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        /**
                         * Métododo donde realizaremos las acciones pertinentes en caso de que el proceso se haya completado de forma exitosa
                         * @param task Objeto de la clase Task
                         */
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            //Recorremos los datos obtenidos, creamos un nuevo objeto de la clase "Foro" y los añadimos al ArrayList de foros
                            for (DocumentSnapshot documentSnapshot_perfil : task.getResult()) {
                                Foro foro = new Foro(documentSnapshot_perfil.getString("imagenPerfil"), documentSnapshot_perfil.getString("nombreUsuario"), documentSnapshot_foro.getString("fechaPublicacion"), documentSnapshot_foro.getString("titulo"), documentSnapshot_foro.getString("mensaje"), documentSnapshot_foro.getString("categoria"), documentSnapshot_perfil.getString("email"));
                                foros.add(foro);
                            }

                            //Instanciamos el objeto de la clase AdaptadorForo
                            adaptadorForo = new AdaptadorForo(foros, getActivity().getApplicationContext(), R.layout.listado_foro);

                            //Añadimos la funcionabilidad a los items del RecyclerView
                            adaptadorForo.setOnClickListener(new View.OnClickListener() {
                                /**
                                 * Creamos un Intent donde enviamos la información necesaria para que pueda visualizarse la información del usuario que ha realizado el post
                                 * @param v The view that was clicked.
                                 */
                                @Override
                                public void onClick(View v) {
                                    //Creamos un Intent que vaya a la actividad de visualizar usuario donde le enviamos el email del usuario que ha realizado el post, el email de quien ha iniciado sesión y el ID del documento
                                    Intent intent = new Intent(getActivity().getApplicationContext(), VisualizarUsuario.class);
                                        intent.putExtra("email_usuario", foros.get(recyclerView.getChildAdapterPosition(v)).getEmail());
                                        intent.putExtra("email", email);
                                        intent.putExtra("ID_documento", id_documento);
                                    startActivity(intent);
                                }
                            });

                            //Instanciamos el objeto RecyclerView  y lo identificamos
                            recyclerView = view.findViewById(R.id.listadoForoGeneral);

                            //Usamos el objeto RecyclerView donde usamos el método .setAdapter() donde le pasamos el adaptador
                            recyclerView.setAdapter(adaptadorForo);

                            //Instanciamos el objeto LayoutManager
                            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

                            //Usamos el objeto RecyclerView donde usamos el método .setLayoutManager() donde le pasamos el LaytoutManager
                            recyclerView.setLayoutManager(layoutManager);
                        }
                    });
                }
            }
        });

        return view;
    }
}