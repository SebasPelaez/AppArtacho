package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista;

import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;
import retrofit2.Call;

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
    private String userId;
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

    private DatabaseReference databaseReference;
    private DatabaseReference userReference;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_perfil);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userReference = databaseReference.child("Usuario");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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
                                    selectedImage = uri;
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    editImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    selectedImage = data.getData();
                    imagePath = selectedImage.toString();
                    editImage.setImageURI(selectedImage);
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
        imagePath = User_Singleton.getInstance().getImage();
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
        User user = User_Singleton.getInstance();
        userId = user.getId();
        editName.setText(user.getName());
        editLastName.setText(user.getLastname());
        editPhone.setText(user.getPhone());
        editAddress.setText(user.getAddress());
        editEmail.setText(user.getEmail());
        if (user.getGender() == 1) {
            editMasc.setChecked(true);
            editGender=1;
        } else {
            editFem.setChecked(true);
            editGender=0;
        }
        String userI = user.getImage();
        if(userI !=null &&!userI.equals("")){
            Glide.with(updatePerfil.this)
                    .load(Uri.parse(user.getImage()))
                    .into(editImage);
        }
        editCity.setText(user.getCity());
    }

    private void updateUserInformation(String urlPhto){
        String key = User_Singleton.getInstance().getId();
        User editUser = new User();

        editUser.setId(key);
        editUser.setUsername(User_Singleton.getInstance().getUsername());
        editUser.setBirthday(User_Singleton.getInstance().getBirthday());

        editUser.setName(editName.getText().toString());
        editUser.setLastname(editLastName.getText().toString());
        editUser.setGender(editGender);
        editUser.setPhone(editPhone.getText().toString());
        editUser.setAddress(editAddress.getText().toString());
        editUser.setEmail(editEmail.getText().toString());
        editUser.setCity(editCity.getText().toString());
        if(!confirmPassword.getText().toString().equals(User_Singleton.getInstance().getPassword())
                && confirmPassword.length()>=6){
            editUser.setPassword(confirmPassword.getText().toString());
        }
        editUser.setImage(urlPhto);

        userReference.child(key).setValue(editUser);

        User_Singleton.destroyInstance();
        User_Singleton.getInstance(editUser);

        Toast.makeText(this, "Informacion editada", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(updatePerfil.this,Principal.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateInfo:
                if(!imagePath.equals(User_Singleton.getInstance().getImage())){
                    uploadPhoto();
                }else{
                    updateUserInformation(imagePath);
                }
                break;
            default:
                break;
        }
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
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        updateUserInformation(String.valueOf(downloadUrl));
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

}
