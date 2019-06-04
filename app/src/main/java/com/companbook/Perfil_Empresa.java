package com.companbook;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.companbook.Pojos.Datos_Empresa;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Perfil_Empresa extends Base_Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 150;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    ImageView logo;
    EditText nombre;
    EditText CIF;
    EditText direccion;
    EditText descripción;
    TextView tvEmail;

    String email;
    String uid;
    String txtNombre;
    String txtCIF;
    String txtDireccion;
    String txtDescripcion;
    String URL;
    int codigo = 0;
    ArrayList<Datos_Empresa> datos_empresas;
    info_CompProfile infoComp;

    FirebaseUser user;
    Uri uri;
    StorageReference destino;
    StorageReference storage;
    DatabaseReference dbr;
    ChildEventListener cel;

    Bitmap imageBitmap;

    private Dialog miniProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_perfil__empresa);

        user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();

        } else {
            //Aqui ira algo
        }

        storage = FirebaseStorage.getInstance().getReference();


        logo = findViewById(R.id.ivLogo);
        nombre = findViewById(R.id.etNombre);
        CIF = findViewById(R.id.etCIF);
        direccion = findViewById(R.id.etDireccion);
        descripción = findViewById(R.id.etDescripcion);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Hacer foto", "Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(Perfil_Empresa.this);
                builder.setTitle("Elige una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Hacer foto")) {
                            hacerFoto();
                        } else if (options[which].equals("Galería")) {
                            cargarImagen();
                        } else if (options[which].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        dbr = FirebaseDatabase.getInstance().getReference().child("Empresa");

        if (cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    datos_empresas = new ArrayList<Datos_Empresa>();

                    Datos_Empresa datos_empresa = dataSnapshot.getValue(Datos_Empresa.class);
                    datos_empresas.add(datos_empresa);

                    FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();

                    if (userFirebase != null) {
                        for (Datos_Empresa aux : datos_empresas) {
                            Log.e("usuari", aux.getUid());

                            if (aux.getUid().equals(userFirebase.getUid())) {
                                Glide.with(Perfil_Empresa.this).load(aux.getUrl_logo()).into(logo);
                                nombre.setText(aux.getNombre());
                                CIF.setText(aux.getCif());
                                direccion.setText(aux.getDirección());
                                descripción.setText(aux.getDescripcion());

                                URL = aux.getUrl_logo();
                                dbr.removeEventListener(cel);
                            }
                        }
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };
            dbr.addChildEventListener(cel);
        }
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_perfil__empresa;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void aceptar(View view) {

        //mostrarMiniProgressBar();
        txtNombre = nombre.getText().toString();
        txtCIF = CIF.getText().toString();
        txtDireccion = direccion.getText().toString();
        txtDescripcion = descripción.getText().toString();

        if (txtNombre.trim().isEmpty() || txtCIF.trim().isEmpty() || txtDireccion.trim().isEmpty() || txtDescripcion.trim().isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar rellenos", Toast.LENGTH_SHORT).show();

        } else {
            if (codigo == 1) {
                subirLogo(getImageUri(this, imageBitmap));

            } else if (codigo == 2) {
                subirLogo(uri);

            } else if (codigo == 0) {

                Datos_Empresa usuario = new Datos_Empresa(uid, URL, txtNombre, txtDescripcion, txtCIF, txtDireccion);

                dbr.child(uid).setValue(usuario);
                Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Perfil_Empresa.this, Activity_Inicio_Empresas.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                /*if (miniProgressBar.isShowing()) {
                    miniProgressBar.cancel();
                }*/
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            logo.setImageBitmap(imageBitmap);
            codigo = 1;

            infoComp = new info_CompProfile(Perfil_Empresa.this);
            infoComp.show();


        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            uri = data.getData();
            Glide.with(Perfil_Empresa.this).load(uri).into(logo);
            codigo = 2;

            infoComp = new info_CompProfile(Perfil_Empresa.this);
            infoComp.show();
        }
    }

    public void hacerFoto() {
        if (checkPermissionWRITE_EXTERNAL_STORAGE(this)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicacion"), 10);
    }

    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }

    }

    public void subirLogo(Uri uri) {
        destino = storage.child("logoEmpresa").child(String.valueOf(uri.getLastPathSegment()));


        UploadTask uploadTask = destino.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                return destino.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    URL = downloadUri.toString();  //URL DE DESCARGA

                    txtNombre = nombre.getText().toString();
                    txtCIF = CIF.getText().toString();
                    txtDireccion = direccion.getText().toString();
                    txtDescripcion = descripción.getText().toString();

                    Datos_Empresa datComp = new Datos_Empresa(uid, URL, txtNombre, txtDescripcion, txtCIF, txtDireccion);

                    dbr.child(uid).setValue(datComp);
                    Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Perfil_Empresa.this, Activity_Inicio_Empresas.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    /*if (miniProgressBar.isShowing()) {
                        miniProgressBar.cancel();
                    }*/


                } else {
                    //Posibles errores
                }
            }
        });
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mostrarMiniProgressBar() {
        miniProgressBar = new Dialog(this);
        miniProgressBar.setContentView(R.layout.progbar_mini);
        miniProgressBar.setCancelable(false);
        miniProgressBar.show();
        //Branch Change
    }
}
