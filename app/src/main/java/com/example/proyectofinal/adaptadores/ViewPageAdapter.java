package com.example.proyectofinal.adaptadores;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesús Escudero Gabarre
 * Clase adaptador para manejar los fragmentos del TabLayout
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    //ArraList donde contiene los distintos fragmentos que contendrá el TabLayout
    private final List<Fragment> listado_fragmentos = new ArrayList<>();

    //ArrayList de tipo String donde almacenerá el nombre de los distintos Items del TabLayout
    private final List<String> listado_nombre_items = new ArrayList<>();

    /**
     * Constructor del ViewPageAdapter
     * @param fm Variable de tipo FragmentManager
     * @param behavior Variable de tipo int
     */
    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    /**
     * Método donde devolverá el fragmento correspondiente del TabLayout
     * @param position Variable de tipo int que representa la posición
     * @return Devuelve el fragmeto dependiendo de la posición
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listado_fragmentos.get(position);
    }

    /**
     * Método donde devolverá el tamaño del ArrayList que contiene los fragmentos
     * @return Variable de tipo int que almacena el tamaño del ArrayList que contiene los fragmentos del TabLayout
     */
    @Override
    public int getCount() {
        return listado_fragmentos.size();
    }

    /**
     * Método donde obtendremos y establecerá el nombre de los Item del TabLayout
     * @param position The position of the title requested
     * @return Devuelve el nombre de TabItem del TabLayout
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return listado_nombre_items.get(position);
    }

    /**
     * Método donde añadiremos el fragmento, el nombre del Item en los respectivos ArrayList, el ID del documento y el email de quien ha iniciado sesión
     * @param fragment Objeto de la clase Fragment
     * @param title String que representa el nombre del Item
     * @param id_documento String que almacena el ID del documento
     * @param email String que representa el email de quien ha iniciado sesión
     */
    public void anyadirFragmento(Fragment fragment, String title, String id_documento, String email) {
        //Creamos un Bundle donde añadiremos la información necesaria
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("ID_documento", id_documento);

        //Usamos el objeto de la clase Fragment donde añadimos los argumentos que almacena el Bundle
        fragment.setArguments(bundle);

        //Añadimos al ArrayList de tipo Fragment el fragmento
        listado_fragmentos.add(fragment);

        //Añadimos al ArrayList de tipo String el nombre del Item
        listado_nombre_items.add(title);
    }
}

