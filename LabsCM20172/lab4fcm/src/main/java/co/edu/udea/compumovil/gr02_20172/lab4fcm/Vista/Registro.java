package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Validacion.Validation;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Registro extends AppCompatActivity implements View.OnClickListener{

    private ImageView foto;
    private EditText birthday;
    private EditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText direccion;
    private EditText email;
    private AutoCompleteTextView ciudad;
    private EditText password;
    private EditText username;
    private EditText rPassword;
    private LinearLayout layout_imagen;
    private int sexo;
    private Uri selectedImage;

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

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userReference = databaseReference.child("Usuario");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mAuth = FirebaseAuth.getInstance();

        inicializarComponentes();

        if(mayRequestStoragePermission())
            foto.setEnabled(true);
        else
            foto.setEnabled(false);

        ArrayAdapter adapterCiudades = new ArrayAdapter(this,android.R.layout.simple_list_item_1,ciudades);
        ciudad.setAdapter(adapterCiudades);
        ciudad.setThreshold(1);

        foto.setOnClickListener(new View.OnClickListener() {
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
            Snackbar.make(layout_imagen, "Los permisos son necesarios para poder usar la aplicación",
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrar:
                if(validarContrasenas()){
                    if(checkValidation() && validarCamposVacios()){
                        uploadPhoto();
                    }else{
                        Toast.makeText(getApplicationContext(),"Hay errores en el formulario",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.birthday:
                int dia,mes,ano;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },ano,mes,dia);
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(nombre)) ret = false;
        if (!Validation.hasText(apellido)) ret = false;
        if (!Validation.hasText(direccion)) ret = false;
        if (!Validation.hasText(ciudad)) ret = false;
        if (!Validation.isEmailAddress(email, true)) ret = false;
        if (!Validation.isPhoneNumber(telefono, false)) ret = false;

        return ret;
    }

    private boolean validarCamposVacios() {
        return true;
    }

    private boolean validarContrasenas() {
        return password.getText().toString().equals(rPassword.getText().toString());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnSexoFemenino_Registro:
                if (checked)
                    sexo = 0;
                break;
            case R.id.rbtnSexoMasculino_Registro:
                if (checked)
                    sexo = 1;
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
        ciudad  = (AutoCompleteTextView)findViewById(R.id.txtCiudad);
        username = (EditText)findViewById(R.id.txtUsername);
        password  = (EditText)findViewById(R.id.txtPassword);
        rPassword  = (EditText)findViewById(R.id.txtPasswordRepeat);
        birthday = (EditText)findViewById(R.id.birthday);
        layout_imagen = (LinearLayout)findViewById(R.id.layoutImagen);
        agregarValidaciones();
    }

    private void agregarValidaciones() {
        nombre.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(nombre);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        apellido.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(apellido);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        direccion.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(direccion);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ciudad.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(ciudad);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        telefono.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(telefono, false);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        username.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(username);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        email.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(email, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

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
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                    selectedImage = uri;
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    foto.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    selectedImage = data.getData();
                    foto.setImageURI(selectedImage);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Registro.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                foto.setEnabled(true);
            }else{
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

    public void registerUser(String urlPhto){
        registerOnFirebase(email.getText().toString(),password.getText().toString());

        String key = userReference.push().getKey();
        User u = new User();
        u.setId(key);
        u.setUsername(username.getText().toString());
        u.setPassword(password.getText().toString());
        u.setName(nombre.getText().toString());
        u.setLastname(apellido.getText().toString());
        u.setGender(new Integer(sexo));//Prueba con un Integer
        u.setBirthday(birthday.getText().toString());
        u.setPhone(telefono.getText().toString());
        u.setAddress(direccion.getText().toString());
        u.setEmail(email.getText().toString());
        u.setCity(ciudad.getText().toString());
        u.setImage(String.valueOf(selectedImage));
        u.setImage(urlPhto);
        userReference.child(key).setValue(u);

        goLogginScreen();
    }

    private void registerOnFirebase(String textEmail,String textPassword) {
        mAuth.createUserWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        } else{
                            Toast.makeText(getApplicationContext(), "Error creating user", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void uploadPhoto(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creando Usuario");
        progressDialog.show();

        imageRef = storageRef.child("Images/"+selectedImage.getLastPathSegment());

        imageRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();
                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_LONG).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        registerUser(String.valueOf(downloadUrl));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "AQUI: "+exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }

    private void goLogginScreen() {
        //Se redirigea la actividad principal para loguearse
        Intent i = new Intent(Registro.this, Loggin.class);
        startActivity(i);
        finish();
    }

}
