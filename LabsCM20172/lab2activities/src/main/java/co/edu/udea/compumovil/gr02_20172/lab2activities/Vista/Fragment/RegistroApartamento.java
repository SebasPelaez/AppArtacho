package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.SQLiteConnectionHelper;
import co.edu.udea.compumovil.gr02_20172.lab2activities.Validacion.Validation;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.User;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroApartamento extends Fragment{

    private View rootView;

    private AutoCompleteTextView txtTipoInmueble;
    private EditText txtNombreInmueble;
    private ImageView imgFotosApartamento;
    private EditText txtDescripcionApartamento;
    private EditText txtValor;
    private EditText txtArea;
    private EditText txtCuartos;
    private EditText txtUbicacion;
    private Button btnRegistrar;
    private LinearLayout layout_imagenApartamento;

    /**
     * Para la foto
     */
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath;
    private String imagePath;

    public RegistroApartamento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registro_apartamento, container, false);
        inicializarComponentes(rootView);

        if(mayRequestStoragePermission())
            imgFotosApartamento.setEnabled(true);
        else
            imgFotosApartamento.setEnabled(false);

        imgFotosApartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerApartment();
            }
        });

        return rootView;
    }


    public void inicializarComponentes(View rootView){
        imgFotosApartamento  = (ImageView)rootView.findViewById(R.id.imgFotosApartamento);
        txtNombreInmueble  = (EditText)rootView.findViewById(R.id.txtNombreInmueble);
        txtTipoInmueble  = (AutoCompleteTextView)rootView.findViewById(R.id.txtTipoInmueble);
        txtValor  = (EditText)rootView.findViewById(R.id.txtValor);
        txtArea  = (EditText)rootView.findViewById(R.id.txtArea);
        txtCuartos  = (EditText)rootView.findViewById(R.id.txtCuartos);
        txtUbicacion  = (EditText)rootView.findViewById(R.id.txtUbicacion);
        txtDescripcionApartamento = (EditText)rootView.findViewById(R.id.txtDescripcionApartamento);
        layout_imagenApartamento = (LinearLayout)rootView.findViewById(R.id.layout_imagenApartamento);
        btnRegistrar = (Button)rootView.findViewById(R.id.btnRegistrarApartamento);
        agregarValidaciones();
    }

    private boolean mayRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if((rootView.getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (rootView.getContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(layout_imagenApartamento, "Los permisos son necesarios para poder usar la aplicación",
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
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

    private void agregarValidaciones() {
        txtTipoInmueble.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtTipoInmueble);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtNombreInmueble.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtNombreInmueble);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtDescripcionApartamento.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtDescripcionApartamento);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtValor.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtValor);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtArea.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtArea);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtCuartos.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(txtCuartos);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtUbicacion.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Validation.hasText(txtUbicacion);}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

    }

    public void registerApartment(){
        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(rootView.getContext(),"db_lab",null,1);
        SQLiteDatabase db = connectionDb.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("apartamentname",txtNombreInmueble.getText().toString());
        values.put("type",txtTipoInmueble.getText().toString());
        values.put("value",Integer.parseInt(txtValor.getText().toString()));
        values.put("id_user", User.getInstance().getId());
        values.put("area",Double.parseDouble(txtArea.getText().toString()));
        values.put("description",txtDescripcionApartamento.getText().toString());
        values.put("num_rooms",Integer.parseInt(txtCuartos.getText().toString()));
        values.put("location",txtUbicacion.getText().toString());
        Long registered = db.insert("apartament",null,values);
        Toast.makeText(rootView.getContext(),"Saved:"+registered,Toast.LENGTH_SHORT).show();
        registerImg_Apartment();

    }

    public void registerImg_Apartment(){
        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(rootView.getContext(),"db_lab",null,1);
        SQLiteDatabase db = connectionDb.getWritableDatabase();
        ContentValues values = new ContentValues();

        String[] params = {txtNombreInmueble.getText().toString()};
        Cursor cursor = db.rawQuery("SELECT id FROM apartament WHERE apartamentname=?",params);
        if (cursor.moveToFirst()) {
            int a = cursor.getInt(0);
            values.put("id_apartament",a);
            values.put("image",imagePath);
            Long registered = db.insert("resource",null,values);
            Toast.makeText(rootView.getContext(),"Saved:"+registered,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    imagePath = mPath;
                    MediaScannerConnection.scanFile(rootView.getContext(),
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
                    imgFotosApartamento.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    imagePath = path.toString();
                    imgFotosApartamento.setImageURI(path);
                    //informacion.getData().setRuta_foto(path.toString());
                    break;

            }
        }
    }

}
