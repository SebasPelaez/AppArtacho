package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment {


    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recuperarInformacion();
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    private void recuperarInformacion() {
        /*
        TextView nombre  = (TextView)findViewById(R.id.txtNombre_Informacion);
        TextView apellido  = (TextView)findViewById(R.id.txtApellido_Informacion);
        TextView telefono  = (TextView)findViewById(R.id.txtTelefono_Informacion);
        TextView direccion  = (TextView)findViewById(R.id.txtDireccion_Informacion);
        TextView email  = (TextView)findViewById(R.id.txtEmail_Informacion);
        TextView ciudad  = (TextView)findViewById(R.id.txtCiudad_Informacion);
        RadioButton genMasculino  = (RadioButton)findViewById(R.id.rbtnSexoMasculino_Informacion);
        RadioButton genFemenino  = (RadioButton)findViewById(R.id.rbtnSexoFemenino_Informacion);
        ImageView foto = (ImageView)findViewById(R.id.imgFoto_Informacion);

        nombre.setText(nombre.getText().toString()+": "+informacion.getData().getNombre());
        apellido.setText(apellido.getText().toString()+": "+informacion.getData().getApellidos());
        telefono.setText(telefono.getText().toString()+": "+informacion.getData().getTelefono());
        direccion.setText(direccion.getText().toString()+": "+informacion.getData().getDireccion());
        email.setText(email.getText().toString()+": "+informacion.getData().getEmail());
        ciudad.setText(ciudad.getText().toString()+": "+informacion.getData().getCiudad());

        if(informacion.getData().getGenero().equals("Masculino")){
            genMasculino.setChecked(true);
        }else{
            genFemenino.setChecked(true);
        }

        foto.setImageURI(Uri.parse(informacion.getData().getRuta_foto()));
        */
    }
}
