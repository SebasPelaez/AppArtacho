package co.edu.udea.compumovil.gr02_20172.lab2activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Loggin extends AppCompatActivity implements View.OnClickListener{

    private EditText txtUsuario;
    private EditText txtClave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtClave = (EditText)findViewById(R.id.txtPassword);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnIngesar:
                i = new Intent(Loggin.this, Principal.class);
                startActivity(i);
                break;
            case R.id.txtRegistrar:
                i = new Intent(Loggin.this, Registro.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
