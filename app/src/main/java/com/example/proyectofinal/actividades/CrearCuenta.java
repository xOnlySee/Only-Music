package com.example.proyectofinal.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente a la actividad de crear cuenta
 */
public class CrearCuenta extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText campo_email, campo_contrasenya, campo_confirmacion_contrasenya;
    TextInputLayout layout_campo_email, layout_campo_contrasenya, layout_campo_confirmacion_contrasenya;
    MaterialButton boton_continuar, boton_iniciar_sesion;
    MaterialCheckBox checkBox_terminos;
    ScrollView layout;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad de inicio de sesión
        Intent intent = new Intent(CrearCuenta.this, InicioSesion.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos los TextInputEditText
        campo_email = findViewById(R.id.campoEmail_pantallaCrearCuenta);
        campo_contrasenya = findViewById(R.id.campoContrasenya_pantallaCrearCuenta);
        campo_confirmacion_contrasenya = findViewById(R.id.campoConfirmacionContrasenya_pantallaCrearCuenta);

        //Identificamos los TextInputLayout
        layout_campo_email = findViewById(R.id.layoutEmail_pantallaCrearCuenta);
        layout_campo_contrasenya = findViewById(R.id.layoutContrasenya_pantallaCrearCuenta);
        layout_campo_confirmacion_contrasenya = findViewById(R.id.layoutConfirmacionContrasenya_pantallaCrearCuenta);

        //Declaramos los MaterialButton
        boton_continuar = findViewById(R.id.botonContinuar_pantallaCrearCuenta);
        boton_iniciar_sesion = findViewById(R.id.botonIniciarSesion_pantallaCrearCuenta);

        //Identificamos el MaterialCheckbox
        checkBox_terminos = findViewById(R.id.checBoxTerminos_pantallaCrearCuenta);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.layout_pantallaCrearCuenta);

        //Identificamos el objeto de la clase FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Creamos e instanciamos un objeto de la clase Query junto al método .whereEqualTo() donde le pasamos el nombre del campo y el valor que debe de tener para obtener el ID del documento
        collectionReference = firestore.collection("perfil");

        //Instanciamos el objeto de la clase AwesomeValidation donde especificaremos el tipo de validación
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Añadimos la validación a los campos del email y de la contraseña
        awesomeValidation.addValidation(this, R.id.campoEmail_pantallaCrearCuenta, Patterns.EMAIL_ADDRESS, R.string.problema_email);
        awesomeValidation.addValidation(this, R.id.campoContrasenya_pantallaCrearCuenta, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}", R.string.problema_contrasenya);

        //Declaramos la funcionabilidad del campo email
        campo_email.setOnClickListener(new View.OnClickListener() {
            /**
             * En caso de que el TextInputLayout sea pulsado, quitaremos el mensaje de error
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

        //Declaramos la funcionabilidad del campo de confirmación de la contraseña
        layout_campo_confirmacion_contrasenya.setOnClickListener(new View.OnClickListener() {
            /**
             * En caso de que el TextInputLayout sea pulsado, añadiremos el endIconMode correspondiente
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Usamos el TextInputLayout seguido del método .setEndIcondeMode para establecer el icono
                layout_campo_confirmacion_contrasenya.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }
        });

        //Declaramos la funcionabilidad del botón continuar
        boton_continuar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método donde comprobaremos el contenido de los TextInputEditText
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Comprobamos mediante un if-else la información de los campos

                //Si los campos están vacios
                if (campo_email.getText().toString().isEmpty() || campo_contrasenya.getText().toString().isEmpty() || campo_confirmacion_contrasenya.getText().toString().isEmpty()) {
                    /* Marcamos los TextInputLayout que esten vacios */
                    if (campo_email.getText().toString().isEmpty()) {
                        layout_campo_email.setError("Debes de introducir un correo electrónico");
                    }
                    if (campo_contrasenya.getText().toString().isEmpty()) {
                        layout_campo_contrasenya.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        layout_campo_contrasenya.setError("Debes de introducir la contraseña");
                    }
                    if (campo_confirmacion_contrasenya.getText().toString().isEmpty()) {
                        layout_campo_confirmacion_contrasenya.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        layout_campo_confirmacion_contrasenya.setError("Debes de confirmar la contraseña introducida en el anterior campo");
                    }

                //En caso de que los campos de la contraseñas no sean iguales
                } else if (!campo_contrasenya.getText().toString().equals(campo_confirmacion_contrasenya.getText().toString())) {
                    Snackbar snackbar = Snackbar.make(layout, "Las contraseñas deben de ser igual", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    layout_campo_contrasenya.setError("Verifica este campo");
                    layout_campo_confirmacion_contrasenya.setError("Verifica este campo");

                //En caso de que el CheckBox de los términos no este marcado
                } else if (!checkBox_terminos.isChecked()) {
                    //Creamos una Snackbar para mostrar lo sucedido al usuario
                    Snackbar snackbar = Snackbar.make(layout, "Debes aceptar los tèrminos", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Aceptar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                //En cualquier otro caso
                } else {
                    //Comprobamos que la validación de que los datos introducidos por el usuario son validos
                    if (awesomeValidation.validate()) {
                        //Creamos un MaterialAlertDialog para informar al usuario de lo sucedido
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(CrearCuenta.this);
                        materialAlertDialogBuilder.setTitle("Nueva cuenta");
                        materialAlertDialogBuilder.setMessage("¿Deseas crear la nueva cuenta?");
                        materialAlertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            /**
                             * Añadimos la funcionabilidad del botón positivo
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Llamamos al método verificarCuenta() donde le pasamos la información del campo email
                                verificarCuenta(campo_email.getText().toString());
                            }
                        });

                        materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * Añadimos la funcionabilidad del botón negativo
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                materialAlertDialogBuilder.setCancelable(true);
                                Snackbar snackbar = Snackbar.make(layout, "Creación de cuenta cancelada", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                        materialAlertDialogBuilder.show();
                    }

                }
            }
        });

        //Declaramos la funcionabilidad del botón iniciar sesión
        boton_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del botón iniciar sesión donde crearemos un Intent que vaya a la actividad de inicio de sesión
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Creamos un Intent que vaya a la pantalla de inicio de sesión
                Intent intent = new Intent(CrearCuenta.this, InicioSesion.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Método donde comprobamos si la cuenta existe. En caso de que no exista, creará la cuenta y si existe, arrojará un error por pantalla
     * @param email Variable de tipo String donde almacená el email
     */
    public void verificarCuenta(String email) {
        //Usamos e instanciamos el objeto de la clase FireBaseAuth seguido del método fetchSignInMethodsForEmail() donde le pasamos el email para comprobar si el email esta en uso o no
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            /**
             * Método donde realizaremos las acciones pertines una vez se haya podido comprobar si el email esta en uso
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //En caso de que el proceso se haya podido llevar acabo
                if (task.isSuccessful()) {
                    //Almacenamos en la variable "cuenta" el resultado
                    boolean cuenta = !task.getResult().getSignInMethods().isEmpty();

                    //En caso de que la variable "cuenta" sea true (la cuenta ya esta en uso)
                    if (cuenta) {
                        //Creamos un Snackbar para informar al usuario
                        Snackbar snackbar = Snackbar.make(layout, "La cuenta ya esta en uso", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    //En caso de que no esta en uso
                    } else if (!cuenta){
                        //Llamamos al método anyadirCuenta() donde le pasamos el email y la contraseña de sus respectivos campos
                        anyadirCuenta(campo_email.getText().toString(), campo_contrasenya.getText().toString());
                    }
                }
            }
        });
    }

    /**
     * Método utilziado para crear y registrar una nueva cuenta en la base de datos FireBase
     * @param email Variable de tipo String que almacena el correo eletrónico del usuario
     * @param contrasenya variable de tipo String que almacena la contraseña de la cuenta del usuario
     */
    public void anyadirCuenta(String email, String contrasenya) {
        //Usamos el objeto de la clase FireBaseAuth donde usamos el método .createUserWithEmailAndPassword() donde le pasamos el email y la contraseña del usuario
        firebaseAuth.createUserWithEmailAndPassword(email, contrasenya)

           //Usamos el método .addOnCompleteListener() para comprobar si se ha realizado con éxito
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //En caso de que el proceso de registro se haya completado de forma correcta
                    if (task.isSuccessful()) {
                        //Creamos una Snackbar donde informaremos al usuario de que la cuenta se ha creado de forma exitosa
                        Snackbar snackbar = Snackbar.make(layout, "Cuenta creada con éxito", Toast.LENGTH_SHORT);
                        snackbar.show();

                        //Llamaos al método crearPerfil() donde le pasamos el email
                        crearPerfil(email);

                    //En caso de que el proceso de registro no se haya completado de forma exitosa
                    } else {
                        Snackbar snackbar = Snackbar.make(layout, "Cuenta no creada", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });
    }

    /**
     * Método para crear los campos de la colección "perfil" con el email del usuario
     * @param email Variable de tipo String que almacena el correo del usuario
     */
    public void crearPerfil(String email) {
        //Creamos un Map donde añadimos los campos que queremos crear dentro de la colección perfil
        Map<String, Object> perfil = new HashMap<>();
            perfil.put("biografia", "");
            perfil.put("edad", "");
            perfil.put("email", email);
            perfil.put("imagenPerfil", "https://firebasestorage.googleapis.com/v0/b/only-music-b1515.appspot.com/o/imagenes_perfil%2Fperfil_estandar.png?alt=media&token=d786ea2f-e48e-46ee-a3ad-05c0fe5153bf");
            perfil.put("nombreApellidos", "");
            perfil.put("nombreUsuario", "");

        //Usamos el objeto de la clase FireBaseFireStore donde le indicamos el nombre de la colección y la colección que queremos añadir
        firestore.collection("perfil").add(perfil);
    }
}