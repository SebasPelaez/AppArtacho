package co.edu.udea.compumovil.gr02_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private Singleton_InformacionPersonal informacion;
    private TextView txtFecha;
    private ImageView foto;
    private EditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText direccion;
    private EditText email;
    private EditText ciudad;
    private EditText password;
    private EditText rPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializarComponentes();
        informacion = Singleton_InformacionPersonal.getInformacion();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrar:
                if(validarContrasenas()){
                    recogerInformacion();
                    Intent i = new Intent(Registro.this, Loggin.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnFecha_Registro:
                int dia,mes,ano;
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },dia,mes,ano);
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }

    private boolean validarContrasenas() {
        return password.getText().toString().equals(rPassword.getText().toString());
    }

    private void recogerInformacion() {
        informacion.getData().setNombre(nombre.getText().toString());
        informacion.getData().setApellidos(apellido.getText().toString());
        informacion.getData().setTelefono(telefono.getText().toString());
        informacion.getData().setDireccion(direccion.getText().toString());
        informacion.getData().setEmail(email.getText().toString());
        informacion.getData().setContrasena(password.getText().toString());
        informacion.getData().setCiudad(ciudad.getText().toString());
        informacion.getData().setFecha_Nacimiento(txtFecha.getText().toString());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnSexoFemenino_Registro:
                if (checked)
                    informacion.getData().setGenero("Femenino");
                    break;
            case R.id.rbtnSexoMasculino_Registro:
                if (checked)
                    informacion.getData().setGenero("Masculino");
                    break;
        }
    }

    public void inicializarComponentes(){
        foto  = (ImageView)findViewById(R.id.imgFoto);
        nombre  = (EditText)findViewById(R.id.txtNombre_Registro);
        apellido  = (EditText)findViewById(R.id.txtApellido_Registro);
        telefono  = (EditText)findViewById(R.id.txtTelefono_Registro);
        direccion  = (EditText)findViewById(R.id.txtDireccion_Registro);
        email  = (EditText)findViewById(R.id.txtEmail);
        ciudad  = (EditText)findViewById(R.id.txtCiudad);
        password  = (EditText)findViewById(R.id.txtPassword);
        rPassword  = (EditText)findViewById(R.id.txtPasswordRepeat);
        txtFecha = (TextView)findViewById(R.id.lblFechaNacimiento_Registro);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == 100){
            Uri imageUri = data.getData();
            foto.setImageURI(imageUri);
        }
    }

}
