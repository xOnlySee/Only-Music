package com.example.proyectofinal.fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.proyectofinal.R;
import com.example.proyectofinal.actividades.EditarInformacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;

/**
 * @author Jesús Escudero Gabarre
 * Fragmento donde el usuario visualizará la información personal y dispondrá de más opciones
 */
public class FragmentoInicio extends Fragment {

    //Declaramos los elementos necesarios
    TextInputEditText campo_nombre_apellidos, campo_nombre_usuario, campo_edad, campo_biografia;
    TextInputLayout layout_campo_nombre_apellidos, layout_campo_nombre_usuario, layout_edad, layout_campo_biografia;
    MaterialButton boton_editar_informacion;
    ImageView imagen;
    TextView textView_bienvenida;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    View view;
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
        view = inflater.inflate(R.layout.fragment_fragmento_inicio, container, false);

        //Identificamos los TextInputEditText
        campo_nombre_apellidos = view.findViewById(R.id.campoNombreApellido_fragmentoPantallaInicio);
        campo_nombre_usuario = view.findViewById(R.id.campoNombreUsuario_fragmentoPantallaInicio);
        campo_edad = view.findViewById(R.id.campoEdad_fragmentoPantallaInicio);
        campo_biografia = view.findViewById(R.id.campoBiografia_fragmentoPantallaInicio);

        //Identificamos los TextInputLayout
        layout_campo_nombre_apellidos = view.findViewById(R.id.layoutNombreApellido_fragmentoPantallaInicio);
        layout_campo_nombre_usuario = view.findViewById(R.id.layoutNombreUsuario_fragmentoPantallaInicio);
        layout_edad = view.findViewById(R.id.layoutEdad_fragmentoPantallaInicio);
        layout_campo_biografia = view.findViewById(R.id.layoutBiografia_fragmentoPantallaInicio);

        //Identificamos el MaterialButton
        boton_editar_informacion = view.findViewById(R.id.botonEditarInformacion_fragmentoPantallaInicio);

        //Identificamos el ImageView
        imagen = view.findViewById(R.id.imagen_fragmentoPantallaInicio);

        //Identificamos el TextView
        textView_bienvenida = view.findViewById(R.id.textoBienvenida_fragmentoPantallaInicio);

        //Instanciamos el objeto de la clase FireBaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Bundle donde enviaremos el email del usuario que ha iniciado sesión al resto de fragmentos
        Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("ID_documento", id_documento);
        getParentFragmentManager().setFragmentResult("key", bundle);

        //Instanciamos el objeto de la clase DocumentReferences donde usamos el objeto de la clase FireBaseFireStore donde le pasamos el nombre de la colección y el ID del mismo documento
        documentReference = firestore.collection("perfil").document(id_documento);

        //Usamos el objeto de la clase DocumentReference para obtener los datos del documento "perfil"
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * Realizaremos las acciones necesarias en caso de que el proceso se haya completado de forma exitosa
             * @param documentSnapshot Objeto de la clase DocumentSnapshot
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //En caso de que algunos de los campos esten vacios, mostraremos un error para que el usuario entienda que debe de actualizar los datos de su perfil
                if (documentSnapshot.getString("nombreApellidos").isEmpty() || documentSnapshot.getString("nombreUsuario").isEmpty() || documentSnapshot.getString("edad").isEmpty() || documentSnapshot.getString("biografia").isEmpty()) {
                    boton_editar_informacion.setError("Debes de rellenar los datos");

                    Glide.with(getActivity().getApplicationContext()).load(documentSnapshot.getString("imagenPerfil")).into(imagen);

                    textView_bienvenida.setText("Bienvenido...");

                //En cualquier otro caso
                } else {
                    campo_nombre_apellidos.setText(documentSnapshot.getString("nombreApellidos"));
                    campo_nombre_usuario.setText(documentSnapshot.getString("nombreUsuario"));
                    campo_biografia.setText(documentSnapshot.getString("biografia"));
                    campo_edad.setText(documentSnapshot.getString("edad"));

                    textView_bienvenida.setText("Bienvenido " + documentSnapshot.getString("nombreUsuario"));

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
                            Glide.with(view)
                                    .load(uri)
                                    .signature(new ObjectKey(ultima_modificacion))
                                    .into(imagen);
                        }
                    });
                }
            }
        });

        //Declaramos la funcionabilidad del botón editar información
        boton_editar_informacion.setOnClickListener(view -> {
            //Creamos un Intent que vaya a la pantalla de editar la información
            Intent intent = new Intent(getActivity(), EditarInformacion.class);
                intent.putExtra("email", email);
                intent.putExtra("ID_documento", id_documento);
            startActivity(intent);
        });

        return view;
    }
}