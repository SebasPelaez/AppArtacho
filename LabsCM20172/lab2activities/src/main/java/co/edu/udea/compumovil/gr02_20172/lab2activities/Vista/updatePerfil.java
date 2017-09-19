package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.SQLiteConnectionHelper;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.User;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class updatePerfil extends AppCompatActivity implements View.OnClickListener{

    private EditText editName;
    private EditText editLastName;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editEmail;
    private EditText editPassword;
    private EditText confirmPassword;
    private int editGender;
    private int userId;
    private RadioButton editMasc;
    private RadioButton editFem;
    private LinearLayout editInfoContainer;
    private String imagePath;
    private CircularImageView editImage;
    private AutoCompleteTextView editCity;
    /**
     * Para la foto
     */
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath;

    private String[] ciudades={"Bogotá","Santa Marta","Medellín","Villavicencio","Cali","Bello","Barranquilla",
            "Valledupar","Cartagena de Indias","Pereira","Soledad","Buenaventura","Cúcuta","Pasto","Ibagué",
            "Manizales","Soacha","Montería","Bucaramanga"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_perfil);
        initComponents();
        setInitValues();
        if(mayRequestStoragePermission())
            editImage.setEnabled(true);
        else
            editImage.setEnabled(false);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
    }

    private boolean mayRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(editInfoContainer, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(updatePerfil.this);
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mPath = savedInstanceState.getString("file_path");
        //foto.setImageURI(Uri.parse(informacion.getData().getRuta_foto()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    imagePath = mPath;
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                    //informacion.getData().setRuta_foto(path);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    editImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:

                    Uri path = data.getData();
                    imagePath = path.toString();
                    editImage.setImageURI(path);
                    //informacion.getData().setRuta_foto(path.toString());
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(updatePerfil.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                editImage.setEnabled(true);
            }else{
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(updatePerfil.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    private void initComponents(){
        editImage  = (CircularImageView)findViewById(R.id.editImage);
        editName  = (EditText) findViewById(R.id.editName);
        editLastName  = (EditText)findViewById(R.id.editLastName);
        editPhone  = (EditText)findViewById(R.id.editPhone);
        editAddress  = (EditText)findViewById(R.id.editAddress);
        editEmail  = (EditText)findViewById(R.id.editEmail);
        editCity  = (AutoCompleteTextView)findViewById(R.id.editCity);
        editPassword  = (EditText)findViewById(R.id.editPassword);
        confirmPassword  = (EditText)findViewById(R.id.confirmPassword);
        editMasc = (RadioButton)findViewById(R.id.editMasc);
        editFem = (RadioButton)findViewById(R.id.editFem);
        editInfoContainer = (LinearLayout)findViewById(R.id.editInfoContainer);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.editMasc:
                if (checked)
                    editGender = 1;
                break;
            case R.id.editFem:
                if (checked)
                    editGender = 0;
                break;
        }
    }

    private void setInitValues(){
        User user = User.getInstance();
        int positionCity;
        userId = user.getId();
        editName.setText(user.getName());
        editLastName.setText(user.getLastname());
        editPhone.setText(user.getPhone());
        editAddress.setText(user.getAddress());
        editEmail.setText(user.getEmail());
        if (user.getGender() == 1) {
            editMasc.setChecked(true);
        } else {
            editFem.setChecked(true);
        }
        String userI = user.getImage();
        if(userI !=null &&!userI.equals("")){
            editImage.setImageURI(Uri.parse(user.getImage()));
        }
        ArrayAdapter adapterCiudades = new ArrayAdapter(this,android.R.layout.simple_list_item_1,ciudades);
        editCity.setAdapter(adapterCiudades);
        positionCity = adapterCiudades.getPosition(user.getCity());
        editCity.setText(ciudades[positionCity]);
        editCity.setThreshold(1);
    }

    private void updateUserInformation(){
        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(this,"db_lab",null,1);
        SQLiteDatabase db = connectionDb.getWritableDatabase();
        String[] params = {Integer.toString(userId)};
        ContentValues values = new ContentValues();
        if(!editPassword.getText().toString().equals("")){
            if(editPassword.getText().toString().equals(confirmPassword.getText().toString())){
                values.put("password",editPassword.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(), "Error: contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        values.put("name",editName.getText().toString());
        values.put("last_name",editLastName.getText().toString());
        values.put("gender",editGender);
        values.put("phone",editPhone.getText().toString());
        values.put("address",editAddress.getText().toString());
        values.put("email",editEmail.getText().toString());
        values.put("city",editCity.getText().toString());
        if(imagePath != null){
            values.put("image",imagePath);
        }
        int updated = db.update("user",values,"id=?",params);
        if(updated ==1){
            User user = User.getInstance();
            user.setName(editName.getText().toString());
            user.setLastname(editLastName.getText().toString());
            user.setGender(editGender);
            user.setPhone(editPhone.getText().toString());
            user.setAddress(editAddress.getText().toString());
            user.setEmail(editEmail.getText().toString());
            user.setCity(editCity.getText().toString());
            if(!editPassword.getText().toString().equals("")){
                user.setPassword(editCity.getText().toString());
            }
            if(imagePath != null){
                user.setImage(imagePath);
            }
            Toast.makeText(getApplicationContext(),"Actualizado"+updated,Toast.LENGTH_SHORT).show();
            Intent i = new Intent(updatePerfil.this,Principal.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Error"+updated,Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        updateUserInformation();
    }
}
