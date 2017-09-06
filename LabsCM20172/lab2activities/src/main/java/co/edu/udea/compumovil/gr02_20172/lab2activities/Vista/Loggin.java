package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.SQLiteConnectionHelper;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.User;

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
                loginUser();
                break;
            case R.id.txtRegistrar:
                i = new Intent(Loggin.this, Registro.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }


    private void loginUser(){
        Intent i;
        String username = txtUsuario.getText().toString();
        String password = txtClave.getText().toString();
        User user = User.getInstance();
        if(username != "" && password != ""){
            SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(this,"db_lab",null,1);
            SQLiteDatabase db = connectionDb.getReadableDatabase();
            String[] params = {username,password};
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? and password=?",params);
            if (cursor.moveToFirst()) {
                do {
                    user.setId(cursor.getInt(0));
                    user.setUsername(cursor.getString(1));
                    user.setName(cursor.getString(3));
                    user.setLastname(cursor.getString(4));
                    user.setGender(cursor.getInt(5));
                    user.setBirthday(cursor.getString(6));
                    user.setPhone(cursor.getString(7));
                    user.setAddress(cursor.getString(8));
                    user.setEmail(cursor.getString(9));
                    user.setCity(cursor.getString(10));
                    user.setImage(cursor.getString(11));
                } while(cursor.moveToNext());
                i = new Intent(Loggin.this, Principal.class);
                startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Usuario y contraseña requeridos",Toast.LENGTH_SHORT).show();
            }
    }
}
