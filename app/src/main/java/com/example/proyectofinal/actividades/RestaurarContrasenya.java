package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.example.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente para restablecer la contraseña
 */
public class RestaurarContrasenya extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText campo_email;
    TextInputLayout layout_campo_email;
    MaterialButton boton_validar;
    ScrollView layout;
    FirebaseAuth firebaseAuth;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent para que vaya a la actividad de inicio de sesión
        Intent intent = new Intent(RestaurarContrasenya.this, InicioSesion.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurar_contrasenya);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos el TextInputEditText
        campo_email = findViewById(R.id.campoEmail_pantallaRestablecerContrasenya);

        //Identiciamos el TextInputLayout
        layout_campo_email = findViewById(R.id.layoutEmail_pantallaRestablecerContrasenya);

        //Identificamos el MaterialButton
        boton_validar = findViewById(R.id.botonValidarEmail_pantallaRestablecerContrasenya);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.layout_pantallaRestablecerContrasenya);

        //Instanciamos el objeto de la clase FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Declaramos la funcionabilidad al campo email
        campo_email.setOnClickListener(new View.OnClickListener() {
            /**
             * En caso de que el TextInputLayout sea pulsado, quitaremos el mensaje de error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_campo_email.setError(null);
            }
        });

        //Declaramos la funcionabilidad del botón valida y restablecer contraseña
        boton_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos la información del campo mediante if-else

                //En caso de que el campo este vacio
                if (campo_email.getText().toString().isEmpty()) {
                    //Mostraremos una Snackbar para informar al usuario
                    Snackbar snackbar = Snackbar.make(layout, "Debes de introducir tu email", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    layout_campo_email.setError("Rellena el campo");

                //En cualquier otro caso
                } else {
                    //Llamamos al método para restablecer la contraseña del usuario donde le pasamos el email
                    restablecerContrasenya(campo_email.getText().toString());
                }
            }
        });
    }

    /**
     * Método donde realizaremos la gestión para que el usuario le llegue un correo donde pueda cambiar la contraseña
     * @param email Variable de tipo String donde almacena el correo elétronico del usuario
     */
    public void restablecerContrasenya(String email) {
        //Usamos el objeto FireBaseAuth donde indicamos el idioma del correo
        firebaseAuth.setLanguageCode("es");

        //Usamos el objeto FireBaseAuth donde usamos el método .sendPasswordResetEmail() donde le indicamos el email
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //En caso de que el proceso haya salido de forma exitosa
                if (task.isSuccessful()) {
                    //Informaremos al usuario mediante una Snackbar
                    Snackbar snackbar = Snackbar.make(layout, "Comprueba la bandeja de tu correo", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Aceptar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                //En cualquier otro caso
                } else {
                    //Informaremos al usuario mediante una Snackbar
                    Snackbar snackbar = Snackbar.make(layout, "Verifica que los datos sean correctos", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    campo_email.setError("Verifica el email");
                }
            }
        });
    }
}