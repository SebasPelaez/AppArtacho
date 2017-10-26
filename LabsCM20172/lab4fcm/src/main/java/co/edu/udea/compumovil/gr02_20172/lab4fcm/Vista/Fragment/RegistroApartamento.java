package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Adapter.UploadPhotosAdapter;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Validacion.Validation;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Principal;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;
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
    private EditText txtDescripcionApartamento;
    private EditText txtValor;
    private EditText txtArea;
    private EditText txtCuartos;
    private EditText txtUbicacion;
    private Button btnRegistrar;
    private FloatingActionButton fab;
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

    private RecyclerView recyclerView;
    private UploadPhotosAdapter adapter;
    private List<Uri> photosList;

    private String[] tipoInmuebles={"Casa","Apartamento","Finca","PenHouse","Hacienda","Cuchitril","Apartaestudio",
            "Apartacho","Garaje "};

    private DatabaseReference databaseReference;
    private DatabaseReference apartamentosReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;
    private List<Uri> urlImagenes;

    public RegistroApartamento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registro_apartamento, container, false);
        inicializarComponentes(rootView);

        /**
         * CONEXIONES A LA BASE DE DATOS DE FIRBEASE
         */
        databaseReference = FirebaseDatabase.getInstance().getReference();
        apartamentosReference = databaseReference.child("Apartamentos");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        urlImagenes = new ArrayList<>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_viewUpload);
        recyclerView.setHasFixedSize(true);

        photosList = new ArrayList<>();
        adapter = new UploadPhotosAdapter(rootView.getContext(), photosList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        if(mayRequestStoragePermission())
            fab.setEnabled(true);
        else
            fab.setEnabled(false);

        ArrayAdapter adapterTipos = new ArrayAdapter(rootView.getContext(),android.R.layout.simple_list_item_1,tipoInmuebles);
        txtTipoInmueble.setAdapter(adapterTipos);
        txtTipoInmueble.setThreshold(1);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerImg_Apartment();
            }
        });

        return rootView;
    }


    public void inicializarComponentes(View rootView){
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
                Validation.isPhoneNumber(txtValor, false);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        txtArea.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(txtArea, false);
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
        String key = apartamentosReference.push().getKey();
        Apartament apartament = new Apartament();
        apartament.setId(key);
        apartament.setName(txtNombreInmueble.getText().toString());
        apartament.setType(txtTipoInmueble.getText().toString());
        apartament.setValue(Integer.parseInt(txtValor.getText().toString()));
        apartament.setIdUSer(User_Singleton.getInstance().getId());
        apartament.setArea(Double.parseDouble(txtArea.getText().toString()));
        apartament.setDescription(txtDescripcionApartamento.getText().toString());
        apartament.setLocation(txtUbicacion.getText().toString());
        apartament.setNumRooms(Integer.parseInt(txtCuartos.getText().toString()));
        apartament.setResourece(String.valueOf(urlImagenes.get(0)));

        apartamentosReference.child(key).setValue(apartament);

        Toast.makeText(rootView.getContext(),"Apartamento guardado:",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getContext(),Principal.class);
        startActivity(i);
    }

    public void registerImg_Apartment(){
        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Creando Apartamento");
        progressDialog.show();

        imageRef = storageRef.child("Apartamentos/"+photosList.get(0).getLastPathSegment());

        imageRef.putFile(photosList.get(0))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();
                        //displaying success toast
                        Toast.makeText(rootView.getContext(), "Imagen guardada", Toast.LENGTH_LONG).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        urlImagenes.add(downloadUrl);
                        registerApartment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(rootView.getContext(), "AQUI: "+exception.getMessage(), Toast.LENGTH_LONG).show();
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
                                    photosList.add(uri);
                                }
                            });
                    adapter.notifyDataSetChanged();
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    imagePath = path.toString();
                    photosList.add(path);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }


}
