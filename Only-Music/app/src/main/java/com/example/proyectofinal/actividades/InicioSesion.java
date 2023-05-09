package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.example.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * @author Jesús Escuderro Gabarre
 * Clase que representa la actividad de iniciar sesión
 */
public class InicioSesion extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    //Declaramos los elementos
    TextInputEditText campo_email, campo_contrasenya;
    TextInputLayout layout_campo_email, layout_campo_contrasenya;
    MaterialButton boton_iniciar_sesion, boton_crear_cuenta, boton_restaurar_contrasenya;
    ScrollView layout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    String id_documento, imagen_perfil;

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
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Método donde finalizará la aplicación
        finishAffinity();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Creamos un Array de String donde añadiremos los permisos necesarios
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};

        //Método para verificar que la aplicación tienes los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        //Identificamos los TextInputEditText
        campo_email = findViewById(R.id.campoEmail_pantallaInicioSesion);
        campo_contrasenya = findViewById(R.id.campoContrasenya_pantallaInicioSesion);

        //Identificamos los TextInputLayout
        layout_campo_email = findViewById(R.id.layoutEmail_pantallaInicioSesion);
        layout_campo_contrasenya = findViewById(R.id.layoutContrasenya_pantallaInicioSesion);

        //Identificamos los MaterialButton
        boton_iniciar_sesion = findViewById(R.id.botonIniciarSesion_pantallaIniciarSesion);
        boton_crear_cuenta = findViewById(R.id.botonCrearCuenta_pantallaInicio);
        boton_restaurar_contrasenya = findViewById(R.id.botonRestaurarContrasenya_pantallaInicio);

        //Identificamos el ConstainsLayout
        layout = findViewById(R.id.layout_pantallaInicioSesion);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Creamos e instanciamos un objeto de la clase Query junto al método .whereEqualTo() donde le pasamos el nombre del campo y el valor que debe de tener para obtener el ID del documento
        collectionReference = firestore.collection("perfil");

        //Instanciamos el objeto de la clase FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Declaramos la funcionabilidad del campo email
        campo_email.setOnClickListener(new View.OnClickListener() {
            /**
             * En caso de que el TextInputLayout sea pulsado, quitaremos el  mensaje de error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Usamos el TextInputLayout del email para eliminar el mensaje de error
                layout_campo_email.setError(null);
            }
        });

        //Declaramos la funcionabilidad del campo de la contraseña
        layout_campo_contrasenya.setOnClickListener(new View.OnClickListener() {
            /**
             * En caso de que el TextInputLayout sea pulsado, añadiremos el endIconMode correspondiente
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Usamos el TextInputLayout seguido del método .setEndIcondeMode para establecer el icono
                layout_campo_contrasenya.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }
        });

        //Le añadimos la funcionabilidad al botón de iniciar sesión
        boton_iniciar_sesion.setOnClickListener(view -> {
            //Comprobamos mediante un if-else la información de los campos correspondientes

            //En caso de que los campos esten vacios
            if (campo_email.getText().toString().isEmpty() || campo_contrasenya.getText().toString().isEmpty()) {
                /* Marcamos los TextInputLayout que esten vacios */
                if (campo_email.getText().toString().isEmpty()) {
                    layout_campo_email.setError("Debes de introducir un correo electrónico");
                }

                if (campo_contrasenya.getText().toString().isEmpty()) {
                    layout_campo_contrasenya.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    layout_campo_contrasenya.setError("Debes de introcuir la contraseña");
                }

            //En cualquier otro caso
            } else {
                //Llamamos al método iniciarSesion() donde le pasamos el email y la contraseña para que realice la consulta a FireBase
                iniciarSesion(campo_email.getText().toString().trim(), campo_contrasenya.getText().toString().trim());
            }
        });

        //Declaramos la funcionabilidad del botón crear cuenta
        boton_crear_cuenta.setOnClickListener(view -> {
            //Creamos un Intent que vaya a la pantalla de crear cuenta
            Intent intent = new Intent(InicioSesion.this, CrearCuenta.class);
            startActivity(intent);
        });

        //Declaramos la funcionabilidad del botón restablecer contraseña
        boton_restaurar_contrasenya.setOnClickListener(new View.OnClickListener() {
            /**
             * Método donde crearemos un Intent que vaya a la actividad de restaurar la contraseña
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la pantalla de restaurar contraseña
                Intent intent = new Intent(InicioSesion.this, RestaurarContrasenya.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Método donde realizará la consulta a FireBase
     * @param email Variable de tipo String donde almacena el email del usuario
     * @param contrasenya Variable de tipo Stribng donde almacena la contraseña del usuario
     */
    public void iniciarSesion(String email, String contrasenya) {
        //Usamos el objeto FireBaseAuth para llamar al método signInWithEmailAndPassword() donde le pasamoe el correo eletrónico y la contraseña
        firebaseAuth.signInWithEmailAndPassword(email, contrasenya)
            //Usamos el método addOnCompleteListener() donde le pasamos el contexto
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                /**
                 * Método donde comprobaremos si las operaciones se han realizado de forma exitosa
                 * @param task Objeto de la clase Task
                 */
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Verificamos si la acción de inicio de sesión se ha llevado a cabo mediante un if-else

                    //En caso de que se haya realizado
                    if (task.isSuccessful()) {
                        //Usamos el objeto Query donde le pasamos el método .get() y el método .addOnCompleteListener() donde usamos el objeto Tastk para obtener el ID del documento
                        Query query = collectionReference.whereEqualTo("email", campo_email.getText().toString());

                        //Usamos el método .get() para obtener el ID del documento
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            /**
                             * Método donde declaramos las acciones que realizaremos para que el usuario incie sesion en la aplicación
                             * @param task Objeto de la clase Task
                             */
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //En caso de que las acciones se haya realizado de forma exitosa
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        //Almacenamos en la variable String el ID del documento
                                        id_documento = documentSnapshot.getId();
                                        imagen_perfil = documentSnapshot.getString("imagenPerfil");

                                        //Creamos un Intent donde que vaya al fragmento de la pantalla principal (donde contiene todos los fragmentos) y le enviamos el email, el ID del documento y el link de la imagen
                                        Intent intent = new Intent(InicioSesion.this, PantallaPrincipal.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("ID_documento", id_documento);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                    //En cualquier otro caso
                    } else {
                        //Creamos una Snackbar para informar al usuario de que no se ha podido iniciar sesión
                        Snackbar snackbar = Snackbar.make(layout, "Ha ocurrido un problema, revise los datos", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Aceptar", new View.OnClickListener() {
                                /**
                                 * Declaramos las acciones del botón "Aceptar" de la Snackbar
                                 * @param view The view that was clicked.
                                 */
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });

                        snackbar.show();
                    }
                }
            });
    }
}