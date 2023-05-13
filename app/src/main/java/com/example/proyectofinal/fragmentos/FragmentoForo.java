package com.example.proyectofinal.fragmentos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.R;
import com.example.proyectofinal.actividades.AnyadirPost;
import com.example.proyectofinal.actividades.VisualizarPost;
import com.example.proyectofinal.adaptadores.ViewPageAdapter;
import com.example.proyectofinal.fragmentos.foro.FragmentoForoAlbumes;
import com.example.proyectofinal.fragmentos.foro.FragmentoForoArtistas;
import com.example.proyectofinal.fragmentos.foro.FragmentoForoGeneral;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * @author Jesús Escudero Gabarre
 * Fragmento donde añadiremos los items necesarios en el TableLayout y donde realizaremos la gestión de los mismos
 */
public class FragmentoForo extends Fragment {
    //Declaramos los elementos necesarios
    FloatingActionButton floatingActionButton_editar, floatingActionButton_anyadir;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instanciamos el objeto de la clase View
        View view = inflater.inflate(R.layout.fragment_fragmento_foro, container, false);

        //Identificamos los FloatingActionButton
        floatingActionButton_editar = view.findViewById(R.id.botonEditarPost_fragmentoForo);
        floatingActionButton_anyadir = view.findViewById(R.id.botonAnyadirPost_fragmentoForo);

        //Identificamos el TabLayout
        tabLayout = view.findViewById(R.id.tabLayout_fragmentoForo);

        //Identificamos el ViewPager
        viewPager = view.findViewById(R.id.view_pager);

        //Instanciamos el objeto ViewAdapter y le pasamos los parametros necesarios (objeto de la clase fragmento correspondiente, el nombre del Item, el ID del documento y el email
        viewPageAdapter = new ViewPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            //Usamos el método .anyadirFragmento() donde le pasamos el fragmento y el nombre del Tab
            viewPageAdapter.anyadirFragmento(new FragmentoForoGeneral(), "General", id_documento, email);
            viewPageAdapter.anyadirFragmento(new FragmentoForoAlbumes(), "Álbumes", id_documento, email);
            viewPageAdapter.anyadirFragmento(new FragmentoForoArtistas(), "Artistas", id_documento, email);

        //Usamos el objeto ViewPager con el método .setAdapter() donde le pasamos el objeto ViewPageAdapter
        viewPager.setAdapter(viewPageAdapter);

        //Usamos el objeto TabLayout donde le pasamos el ViewPager
        tabLayout.setupWithViewPager(viewPager);

        //Declacaramos la funcionabilidad del FloatingActionButton
        floatingActionButton_editar.setOnClickListener(new View.OnClickListener() {
            /**
             * Creamos un Intent donde. Creamos un Intent que vaya a la pantalla de visualizar los post del usuario
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la pantalla de visualizar post y le pasamos el email del usuario y el ID del documento
                Intent intent = new Intent(getActivity().getApplicationContext(), VisualizarPost.class);
                    intent.putExtra("email", email);
                    intent.putExtra("ID_documento", id_documento);
                startActivity(intent);
            }
        });

        //Declaramos la funcionabilidad del FloatingActionButton
        floatingActionButton_anyadir.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos las acciones del FloatingActionButton. Creamos un Intent que vaya a la atividad de añadir un nuevo post
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Creamos un Intent que vaya a la pantalla de añadir post y le pasamos el email del usuario y el ID del documento
                Intent intent = new Intent(getActivity().getApplicationContext(), AnyadirPost.class);
                    intent.putExtra("email", email);
                    intent.putExtra("ID_documento", id_documento);
                startActivity(intent);
            }
        });

        return view;
    }
}