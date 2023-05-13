package com.example.proyectofinal.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.actividades.VisualizarArtista;
import com.example.proyectofinal.adaptadores.AdaptadorArtista;
import com.example.proyectofinal.clases.Artista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Jesús Escudero Gabarre
 * Fragmento donde visualizaremos un listado de los artistas
 */
public class FragmentoArtista extends Fragment implements SearchView.OnQueryTextListener {

    //Declaramos los elementos necesarios
    RecyclerView recyclerView;
    AdaptadorArtista adaptadorArtista;
    RecyclerView.LayoutManager layoutManager;
    View view;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    SearchView barra_busqueda;
    private String email, id_documento;

    /**
     * Método para obtener en sus respectivas variables la información que le llega
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
            id_documento = getArguments().getString("ID_documento");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instanciamos el objeto de la clase View
        view = inflater.inflate(R.layout.fragment_fragmento_artista, container, false);

        //Creamos un ArrayList de tipo Artista
        ArrayList<Artista> artistas = new ArrayList<>();

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Identificamos el SearchView
        barra_busqueda = view.findViewById(R.id.barraBusqueda_fragmentoArtista);
        barra_busqueda.setOnQueryTextListener(this);

        //Instanciamos el objeto de la clase CollectionReferences donde le pasamos la colección "artista"
        collectionReference = firestore.collection("artista");

        //Usamos el método .get() para obtener la información necesaria
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Delcaramos las acciones que se realizarán con la información obtenida
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    Artista artista = new Artista(documentSnapshot.getString("apellidos"), documentSnapshot.getString("biografia"), documentSnapshot.getString("fechaNacimiento"), documentSnapshot.getString("generoMusical") ,documentSnapshot.getString("imagen"), documentSnapshot.getString("nacionalidad"), documentSnapshot.getString("nombre"), documentSnapshot.getString("numeroAlbums"));
                    artistas.add(artista);
                }

                //Instanciamos el objeto de la clase AdaptadorArtista
                adaptadorArtista = new AdaptadorArtista(artistas, getActivity().getApplicationContext(), R.layout.listado_artistas);

                //Añadimos la funcionabilidad a los elementos del Adaptador
                adaptadorArtista.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Declaramos la funcionabilidad en el adaptador
                     * @param view The view that was clicked.
                     */
                    @Override
                    public void onClick(View view) {
                        //Creamos e instanciamos un objeto de la clase Artista donde recuperamos el objeto del ArrayList pasando la posición del elemento pulsado del Adaptador
                        Artista artista = artistas.get(recyclerView.getChildAdapterPosition(view));

                        //Creamos un Intent donde vaya a la actividad de visualizar el artista y le pasamos el objeto Artista recuperado
                        Intent intent = new Intent(getActivity().getApplicationContext(), VisualizarArtista.class);
                            intent.putExtra("Artista", artista);
                            intent.putExtra("email", email);
                            intent.putExtra("ID_documento", id_documento);
                        startActivity(intent);
                    }
                });

                //Instanciamos el objeto de la clase RecyclerView identificamos
                recyclerView = view.findViewById(R.id.listadoArtistas_fragmentoArtista);

                //Usamos el objeto RecyclerView donde usamos el método .setAdapter() donde le pasamos el adaptador
                recyclerView.setAdapter(adaptadorArtista);

                //Instanciamos el objeto LayoutManager
                layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

                //Usamos el objeto RecyclerView donde usamos el método .setLayoutManager() donde le pasamos el LayoutManager
                recyclerView.setLayoutManager(layoutManager);

            }
        });

        return view;
    }

    /**
     * Método donde permite manejar la acción de enviar una consulta de búsqueda
     * @param query the query text that is to be submitted
     *
     * @return Devuelve false
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Método que se ejecutará cuando el usuario introduzca un caracter en el SearcView
     * @param newText the new content of the query text field.
     *
     * @return Devuelve false
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        adaptadorArtista.filtrado(newText);

        return false;
    }
}
