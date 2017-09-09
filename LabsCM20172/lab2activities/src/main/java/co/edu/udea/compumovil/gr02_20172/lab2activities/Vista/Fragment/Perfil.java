package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment {

    private TextView nombre;
    private TextView apellido;
    private TextView telefono;
    private TextView direccion;
    private TextView email;
    private TextView ciudad;
    private RadioButton genMasculino;
    private RadioButton genFemenino;
    private ImageView foto;


    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        nombre = (TextView) view.findViewById(R.id.txtNombre_Informacion);
        apellido = (TextView) view.findViewById(R.id.txtApellido_Informacion);
        telefono = (TextView) view.findViewById(R.id.txtTelefono_Informacion);
        direccion = (TextView) view.findViewById(R.id.txtDireccion_Informacion);
        email = (TextView) view.findViewById(R.id.txtEmail_Informacion);
        ciudad = (TextView) view.findViewById(R.id.txtCiudad_Informacion);
        genMasculino = (RadioButton) view.findViewById(R.id.rbtnSexoMasculino_Informacion);
        genFemenino = (RadioButton) view.findViewById(R.id.rbtnSexoFemenino_Informacion);
        foto = (ImageView) view.findViewById(R.id.imgFoto_Informacion);
        recuperarInformacion();
        return view;
    }

    public void recuperarInformacion() {
        User user = User.getInstance();
        nombre.setText(nombre.getText().toString() + ": " + user.getName());
        apellido.setText(apellido.getText().toString() + ": " + user.getLastname());
        telefono.setText(telefono.getText().toString() + ": " + user.getPhone());
        direccion.setText(direccion.getText().toString() + ": " + user.getAddress());
        email.setText(email.getText().toString() + ": " + user.getEmail());
        ciudad.setText(ciudad.getText().toString() + ": " + user.getCity());
        if (user.getGender() == 1) {
            genMasculino.setChecked(true);
        } else {
            genFemenino.setChecked(true);
        }
        if(user.getImage() != null){
            foto.setImageURI(Uri.parse(user.getImage()));
        }
    }
}
