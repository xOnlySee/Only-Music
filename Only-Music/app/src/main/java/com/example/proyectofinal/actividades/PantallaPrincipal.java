
package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.proyectofinal.R;
import com.example.proyectofinal.fragmentos.FragmentoArtista;
import com.example.proyectofinal.fragmentos.FragmentoCanciones;
import com.example.proyectofinal.fragmentos.FragmentoForo;
import com.example.proyectofinal.fragmentos.FragmentoInicio;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author Jesús Escudero Gabarre
 * Clase que representa la pantalla principal (donde se encuentrán los distintos fragmentos)
 */
public class PantallaPrincipal extends AppCompatActivity {
    //Declaramos los fragmentos necesarios
    final FragmentoInicio fragmento_inicio = new FragmentoInicio();
    final FragmentoCanciones fragmento_canciones = new FragmentoCanciones();
    final FragmentoArtista fragmento_artistas = new FragmentoArtista();
    final FragmentoForo fragmento_foro = new FragmentoForo();
    RelativeLayout layout;

    /**
     * Método para que el usuario otorgue los permisos pertinentes a la aplicación
     * @param requestCode The request code passed in requestPermissions()
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
        }
    }

    /**
     * Método donde declaramos las ancciones para cuando el usuario pulse el botón retroceder de su dispositivo
     */
    @Override
    public void onBackPressed() {
        visualizarButtonSheet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        //Creamos e identificamos el BottomNavigationView y lo identificamos
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar_pantallaPrincipal);

        //Usamos el objeto BottomNavigationView donde le añadimos funcionabilidad
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.layout_pantallaPrincipal);

        //Creamos un Bundle para obtener el email y el ID del documento de la pantalla de inicio
        Bundle bundle = getIntent().getExtras();

        //Usamos el método cargarFragmento() donde le pasamos el fragmento que queramos que se cargue de forma predeterminada
        cargarFragmento(fragmento_inicio);

        //Le añadimos el Bundle al fragmento inidicado usando el método setArguments() donde le pasamos el Bundle
        fragmento_inicio.setArguments(bundle);
        fragmento_foro.setArguments(bundle);

        //Iniciamos la transacción y hacemos un commit
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContenedor_pantallaPrincipal, fragmento_inicio)
                .commit();

        //Creamos un Array de String donde añadiremos los permisos necesarios
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};

        //Método para verificar que la aplicación tienes los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    /**
     * Creamos un método donde implementemos la funcionabilidad que va a tener cada opción del BottomNavigationView
     */
    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        //Creamos un Switch donde establecemos con el método loadFragment() el fragmento que queremos que se cargue
        switch (item.getItemId()) {
            case R.id.fragmento_inicio:
                cargarFragmento(fragmento_inicio);
                return true;

            case R.id.fragmento_canciones:
                cargarFragmento(fragmento_canciones);
                return true;

            case R.id.fragmento_artistas:
                cargarFragmento(fragmento_artistas);
                return true;

            case R.id.fragmento_foro:
                cargarFragmento(fragmento_foro);
                return true;
        }

        return false;
    };

    /**
     * Método creado para cargar un determinado fragmento
     * @param fragmento Representa un Fragment
     */
    public void cargarFragmento(Fragment fragmento) {
        //Creamos un FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //Usamos el FragmentTransaction .replace donde le indicamos el frameContainer y el fragmento
        fragmentTransaction.replace(R.id.frameContenedor_pantallaPrincipal, fragmento);
        fragmentTransaction.commit();
    }

    /**
     * Método donde mostraremos el ButtonSheetDialog y dareremos funcionabilidad a las distintas opciones
     */
    public void visualizarButtonSheet() {
        //Creamos e instanciamos el objeto de la clase Dialog donde le pasamos el contexto
        final Dialog dialog = new Dialog(PantallaPrincipal.this);

        //Usamos el objeto de la clase Dialog donde ocultaremos el titulo del Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Usamos el objeto de la clase Dialog donde le pasamos el diseño que queremos que tenga el Dialog
        dialog.setContentView(R.layout.buttom_sheet_dialog);

        //Declaramos e identificamos todos los elementos que tendrá el Dialog
        LinearLayout salir_aplicacion = dialog.findViewById(R.id.salirAplicacion_buttomSheet),
        cerrar_sesion = dialog.findViewById(R.id.cerrarSesion_buttomSheet),
        cerrar_ventana = dialog.findViewById(R.id.cerrarVentana_buttomSheet);

        //Declaramos la funcionabilidad de la opción de salir de la aplicación
        salir_aplicacion.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad para finalizar la aplicación
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent donde nos llevará a la pantalla de inicio y finalizará la aplicación
                Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        //Declaramos la funcionabilidad de la opción de cerrar sesión
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del LinearLayout de cerrar sesión. Creamos un Intent que vaya a la pantalla de inicio de sesión
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la pantalla de inicio de sesión
                Intent intent = new Intent(PantallaPrincipal.this, InicioSesion.class);
                startActivity(intent);
            }
        });

        //Declaramos la funcionabilidad de la opción de cerrar la ventana
        cerrar_ventana.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del LinearLayout de cerrar la ventana
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Mostraremos el Dialog y configuramos su tamaño
        dialog.show();
    }
}