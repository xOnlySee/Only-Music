package com.example.proyectofinal.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.proyectofinal.R;

/**
 * @author Jesús Escudero Gabarre
 * Clase que corresponde a la Splash Screen
 */
public class PantallaBienvenida extends AppCompatActivity {
    //Declaramos los elementos necesarios
    ImageView imagen;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_bienvenida);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos el ImageView
        imagen = findViewById(R.id.imagenLogo_pantallaBienvenida);

        //Almacenamos en la variable int el modo del tema que este aplicado en el dispositivo
        int tema = PantallaBienvenida.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //Creamos un Siwtch donde usemos el modo del tema del dispositivo
        switch (tema) {
            //En caso de que el modo osucro este habilitado
            case Configuration.UI_MODE_NIGHT_YES:
                //Añadimos la imagen
                imagen.setImageResource(R.drawable.dontthink_oscuro);
                break;

            //En caso de que el modo claro este habilitado
            case Configuration.UI_MODE_NIGHT_NO:
                //Añadimos la imagen
                imagen.setImageResource(R.drawable.dontthink_claro);
                break;
        }


        //Método para pasar a una actividad pasada una cantidad de tiempo determinada
        new Handler().postDelayed(() -> {
            //Creamos un Intent que vaya a la pantalla de inicio de sesión
            Intent intent = new Intent(PantallaBienvenida.this, InicioSesion.class);
            startActivity(intent);
        }, 4000);
    }
}