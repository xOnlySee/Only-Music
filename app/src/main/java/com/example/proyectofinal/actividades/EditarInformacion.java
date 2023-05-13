package com.example.proyectofinal.actividades;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.proyectofinal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jesús Escudero Gabarre
 * Clase correspondiente a la pantalla de editar la información del usuario
 */
public class EditarInformacion extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText campo_nombre_apellidos, campo_nombre_usuario, campo_edad, campo_biografia;
    TextInputLayout layout_nombre_apellidos, layout_nombre_usuario, layout_edad, layout_biografia;
    MaterialButton boton_continuar;
    FloatingActionButton boton_editar_imagen_perfil;
    ImageView imagen;
    ScrollView layout;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    StorageReference storageReference;
    String email;
    String id_documento;
    final String ruta = "imagenes_perfil/";
    String imagen_perfil;
    private static final int COD_SEL_IMAGE = 300;

    /**
     * Declaramos la funcionabilidad para cuando el usuario pulse el botón de retroceso
     */
    @Override
    public void onBackPressed() {
        //Creamos un Intent que vaya a la actividad donde contiene todos los fragmentos y le enviamos el email, el ID del documento y la URL de la imagen
        Intent intent = new Intent(EditarInformacion.this, PantallaPrincipal.class);
            intent.putExtra("email", email);
            intent.putExtra("ID_documento", id_documento);
            intent.putExtra("imagen_perfil", imagen_perfil);
        startActivity(intent);
    }

    /**
     * Método donde gestionaremos la imagen
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprobamos que se ha seleccionado una imagen de la galería
        if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK && data != null) {
            // Obtenemos la Uri de la imagen seleccionada
            Uri uri = data.getData();

            // Actualizamos la imagen del ImageView
            Glide.with(this).load(uri).override(150, 150).into(imagen);

            //Llamamos al método .seleccionarFoto() donde le pasamos el objeto de la clase Uri
            seleccionarFoto(uri);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_informacion);

        //Método donde indicaremos la animación de entrada y la animación de salida
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);

        //Identificamos los TextInputEditText
        campo_nombre_apellidos = findViewById(R.id.campoNombreApellidos_pantallaEditarInformacionUsuario);
        campo_nombre_usuario = findViewById(R.id.campoNombreUsuario_pantallaEditarInformacionUsuario);
        campo_edad = findViewById(R.id.campEdad_pantallaEditarInformacionUsuario);
        campo_biografia = findViewById(R.id.campoBiografia_pantallaEditarInformacionUsuario);

        //Identificamos los TextInputLayout
        layout_nombre_apellidos = findViewById(R.id.layoutNombreApellidos_pantallaEditarInformacionUsuario);
        layout_nombre_usuario = findViewById(R.id.layoutNombreUsuario_pantallaEditarInformacionUsuario);
        layout_edad = findViewById(R.id.layoutEdad_pantallaEditarInformacionUsuario);
        layout_biografia = findViewById(R.id.layoutBiografia_pantallaEditarInformacionUsuario);

        //Identificamos los MaterialButton
        boton_continuar = findViewById(R.id.botonContinuar_pantallaEditarInformacionUsuario);

        //Identificamos el FloatingActionButton
        boton_editar_imagen_perfil = findViewById(R.id.botonEditarImagenPerfil_pantallaEditarInformacionUsuario);

        //Identificamos el ImageView
        imagen = findViewById(R.id.imagen_pantallaEditar_pantallaEditarInformacionUsuario);

        //Identificamos el ScrollView
        layout = findViewById(R.id.layout_pantallaEditarInformacionUsuario);

        //Instanciamos el objeto FireBaseFireStore obteniendo la instancia
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto CollectionReferences usando el método .collection() donde le pasamos el nombre de la colección
        collectionReference = firestore.collection("perfil");

        //Instanciamos el objeto de la clase StorageReference
        storageReference = FirebaseStorage.getInstance().getReference();

        //Creamos un Bundle para obtener los datos necesarios
        Bundle bundle = getIntent().getExtras();
        email = (String) bundle.get("email");
        id_documento = (String) bundle.get("ID_documento");
        imagen_perfil = (String) bundle.get("imagen_perfil");

        //Creamos e instanciamos una Snackbar para informar al usuario que solo podrá añadir 250 caracteres en el campo de la biografia
        Snackbar snackbar_informacion = Snackbar.make(layout, "Solo podrás añadir 250 caracteres en el apartado de biografia", Snackbar.LENGTH_INDEFINITE);
        snackbar_informacion.setAction("Entendido", new View.OnClickListener() {
                /**
                 * Declaramos la funcionabilidad del botón del Snackbar
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    snackbar_informacion.dismiss();
                }
            });
        snackbar_informacion.show();

        //Añadimos la funcionabilidad al campo del nombre y apellidos
        campo_nombre_apellidos.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo del nombre y apellidos, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_nombre_apellidos.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_nombre_apellidos.setError(null);
            }
        });

        //Añadimos la funcionabilidad al campo del nombre de usuario
        campo_nombre_usuario.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo del nombre de usuario, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_nombre_usuario.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_nombre_usuario.setError(null);
            }
        });

        //Añadimos la funcionabilidad al campo de la edad
        campo_edad.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo de la edad, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_edad.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_edad.setError(null);
            }
        });

        //Añadimos la funcionabilidad al campo de la biografia
        campo_biografia.setOnClickListener(new View.OnClickListener() {
            /**
             * Hacemos que cuando el usuario pulse sobre el campo de la biografia, que el TextInputLayout añada el icono de limpiar el texto y eliminamos el error
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                layout_biografia.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                layout_biografia.setError(null);
            }
        });

        //Instanciamos el objeto DocumentReferences donde indicamos el nombre de la colección el ID del documento
        documentReference = firestore.collection("perfil").document(id_documento);

        //Usamos el objeto DocumentReference donde añadiremos la información previa en sus respectivos campos
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * Método donde recuperaremos la información de FireBase
             * @param documentSnapshot Objeto de la clase DocumentSnapshot
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Añadimos la información en sus respectivos campos
                campo_nombre_apellidos.setText(documentSnapshot.getString("nombreApellidos"));
                campo_nombre_usuario.setText(documentSnapshot.getString("nombreUsuario"));
                campo_edad.setText(documentSnapshot.getString("edad"));
                campo_biografia.setText(documentSnapshot.getString("biografia"));

                //Creamos un objeto de la clase Uri donde le pasamos la información del campo "imagenPerfil"
                Uri uri = Uri.parse(documentSnapshot.getString("imagenPerfil"));

                //Usamos FirebaseStorage donde obtenemos la instancia, el objeto de la clae Uri y los metadatos de la imagen obtenida
                FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(uri)).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    /**
                     * Método donde realizaremos las acciones en caso de que el proceso se haya realizado de forma exitosa
                     * @param storageMetadata Objeto de la clase StorageMetadata
                     */
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        //Variable de tipo long donde almacenaremos la fecha de la ultima modificación en milisegundos
                        long ultima_modificacion = storageMetadata.getUpdatedTimeMillis();

                        //Usamos Glide donde le indicamos la imagen que queremos cargar, la fecha de última modificacion (para indicar que la imágen ha cambiado) y el elemento donde lo queremos mostrar
                        Glide.with(EditarInformacion.this)
                                .load(uri)
                                .signature(new ObjectKey(ultima_modificacion))
                                .into(imagen);
                    }
                });
            }
        });

        //Declaramos la funcionabilidad del botón continuar
        boton_continuar.setOnClickListener(view -> {
            //Comprobamos mediante un if-else la información de los campos

            //En caso de que alguno de los campos esten vacios
            if (campo_nombre_apellidos.getText().toString().isEmpty() || campo_nombre_usuario.getText().toString().isEmpty() || campo_edad.getText().toString().isEmpty() || campo_biografia.getText().toString().isEmpty()) {
                if (campo_nombre_apellidos.getText().toString().isEmpty()) { layout_nombre_apellidos.setError("Rellene el campo"); }
                if (campo_nombre_usuario.getText().toString().isEmpty()) { layout_nombre_usuario.setError("Rellene el campo"); }
                if (campo_edad.getText().toString().isEmpty()) { layout_edad.setError("Rellene el campo"); }
                if (campo_biografia.getText().toString().isEmpty()) { layout_biografia.setError("Rellene el campo"); }

                //En cualquier otro caso
            } else {
                //Creamos un MaterialAlertDialog donde le servirá al usuario de confirmación de datos
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(EditarInformacion.this);
                materialAlertDialogBuilder.setTitle("Editar información personal de " + email);
                materialAlertDialogBuilder.setMessage("¿Deseas actualizar tu perfil?");
                materialAlertDialogBuilder.setPositiveButton("Si", (dialogInterface, i) -> {
                    //Creamos e instanciamos un objeto DocumentReference donde le pasamos el nombre de la colección y el ID del documento
                    DocumentReference documentReference = firestore.collection("perfil").document(id_documento);

                    //Creamos un Map donde le pasamos el nombre del campo y el valor que queremos que se guarde
                    Map<String, Object> map = new HashMap<>();
                        map.put("nombreApellidos", campo_nombre_apellidos.getText().toString());
                        map.put("nombreUsuario", campo_nombre_usuario.getText().toString());
                        map.put("edad", campo_edad.getText().toString());
                        map.put("biografia", campo_biografia.getText().toString());

                    //Usamos el objeto DocumentRefences seguido del método .update() donde le pasamos el Map con todos los valores a actualizar
                    documentReference.update(map);

                    //Creams un Intent a la actividad anterior donde le pasamos el email del usuario y el ID del documento
                    Intent intent = new Intent(EditarInformacion.this, PantallaPrincipal.class);
                        intent.putExtra("email", email);
                        intent.putExtra("ID_documento", id_documento);
                    startActivity(intent);
                });

                materialAlertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {
                    //Declaramos la funcionabilidad del botón "No"
                    materialAlertDialogBuilder.setCancelable(true);
                    Snackbar snackbar = Snackbar.make(layout, "No se ha actualizado el perfil", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Aceptar", view1 -> snackbar.dismiss());

                    snackbar.show();
                });

                materialAlertDialogBuilder.show();
            }
        });

        //Añadimos la funcionabilidad del botón flotante
        boton_editar_imagen_perfil.setOnClickListener(new View.OnClickListener() {
            /**
             * Método donde abrirá la galeria para que el usuario seleccione una imagen de perfil
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la sección de recursos almacenados en el dispositivos
                Intent intent = new Intent(Intent.ACTION_PICK);

                //En este caso indicamos que sea imagenes de cualquier tipo
                intent.setType("image/*");

                startActivityForResult(intent, COD_SEL_IMAGE);
            }
        });
    }

    /**
     * Método donde el usuario haya elegido una imagen de su galeria, se hara la gestión necesaria para subirla a FireBase
     * @param uri Variable de tipo Uri que representa la imagen seleccionada por el usuario
     */
    private void seleccionarFoto(Uri uri) {
        //Creamos e instanciamos otro objeto de la clase StorageReference donde especificamos la URL que queremos que tenga imagen
        StorageReference storageReference_imagen = storageReference.child(ruta + "imagen_perfil" + id_documento);

        //Usamos el método .put() donde le indicamos el Uri y el método
        storageReference_imagen.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            /**
             * Método realizaremos las acciones donde actualizará la imagen de perfil
             * @param taskSnapshot Objeto de la clase TaskSnapShot
             */
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Realizamos las acciones pertinentes para guardar el link de la imagen (almacenada en el módulo "Storage") en el módulo "FireStore"
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String, Object> map = new HashMap<>();
                                    map.put("imagenPerfil", uri);
                                    firestore.collection("perfil").document(id_documento).update(map);
                            }
                        });
                    }
                }
            }
        });
    }
}