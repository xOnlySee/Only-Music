package com.example.proyectofinal.actividades;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.proyectofinal.R;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la Splash Screen
 */
public class PantallaBienvenida extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_bienvenida);

        //Método para pasar a una actividad pasada una cantidad de tiempo determinada
        new Handler().postDelayed(() -> {
            //Creamos un Intent que vaya a la pantalla de inicio de sesión
            Intent intent = new Intent(PantallaBienvenida.this, InicioSesion.class);
            startActivity(intent);
        }, 4000);
    }
}