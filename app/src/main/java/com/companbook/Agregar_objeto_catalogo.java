package com.companbook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.companbook.Pojos.Catalogo;
import com.companbook.Pojos.Datos_Empresa;
import com.companbook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Agregar_objeto_catalogo extends Base_Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 150;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    ImageView imagen;
    EditText txtNombre;
    EditText txtReferencia;
    EditText txtDesc;
    EditText txtPrecio;
    EditText txtPalabraClave;
    EditText txtPotencia;
    EditText txtTamaño;
    ImageView infoClave;

    Recomendacion_clave redClave;

    ArrayList<Catalogo> datos_catalogo;
    info_CompProfile infoCat;

    String uid;
    String nombre;
    String referencia;
    String desc;
    Double precio;
    String precio2;
    String url_foto;
    String palabraClave;
    String potencia;
    String tamaño;
    int codigo = 0;

    FirebaseUser user;
    Uri uri;
    StorageReference destino;
    StorageReference storage;
    DatabaseReference dbr;
    ChildEventListener cel;

    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_agregar_objeto_catalogo);

        user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            uid = user.getUid();

        } else {
            //Aqui ira algo
        }

        storage = FirebaseStorage.getInstance().getReference();

        imagen = findViewById(R.id.ivFotoProd);
        txtNombre = findViewById(R.id.etNomProd);
        txtReferencia = findViewById(R.id.etReferencia);
        txtDesc = findViewById(R.id.etDescripcion);
        txtPrecio = findViewById(R.id.etPrecio);
        txtPalabraClave = findViewById(R.id.etPalabraClave);
        txtPotencia = findViewById(R.id.etPotencia);
        txtTamaño = findViewById(R.id.etTamaño);
        infoClave = findViewById(R.id.ivInfoClave);

        infoClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redClave = new Recomendacion_clave(Agregar_objeto_catalogo.this);
                redClave.show();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Hacer foto", "Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(Agregar_objeto_catalogo.this);
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

        dbr = FirebaseDatabase.getInstance().getReference().child("Catalogo");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Guardar(View view) {
        //mostrarMiniProgressBar();
        nombre = txtNombre.getText().toString();
        referencia = txtReferencia.getText().toString();
        precio2 = txtPrecio.getText().toString();
        desc = txtDesc.getText().toString();
        palabraClave = txtPalabraClave.getText().toString();
        potencia = txtPotencia.getText().toString();
        tamaño = txtTamaño.getText().toString();

        if (nombre.trim().isEmpty() || referencia.trim().isEmpty() || precio2.trim().isEmpty() || desc.trim().isEmpty() || palabraClave.trim().isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar rellenos", Toast.LENGTH_SHORT).show();

        } else {
            precio = Double.parseDouble(precio2);

            if (codigo == 1) {
                subirLogo(getImageUri(this, imageBitmap));

            } else if (codigo == 2) {
                subirLogo(uri);

            } else if (codigo == 0) {

                int valorDado = (int) Math.floor(Math.random()*99999999 + 1);

                Catalogo datCatalogo = new Catalogo(valorDado, uid, nombre, referencia, desc, precio, url_foto, palabraClave, potencia, tamaño);

                dbr.child(valorDado + "").setValue(datCatalogo);
                Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Agregar_objeto_catalogo.this, Activity_Inicio_Empresas.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                /*if (miniProgressBar.isShowing()) {
                    miniProgressBar.cancel();
                }*/
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
            codigo = 1;

            infoCat = new info_CompProfile(Agregar_objeto_catalogo.this);
            infoCat.show();


        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            uri = data.getData();
            Glide.with(Agregar_objeto_catalogo.this).load(uri).into(imagen);
            codigo = 2;

            infoCat = new info_CompProfile(Agregar_objeto_catalogo.this);
            infoCat.show();
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
                    url_foto = downloadUri.toString();  //URL DE DESCARGA

                    nombre = txtNombre.getText().toString();
                    referencia = txtReferencia.getText().toString();
                    precio = Double.parseDouble(txtPrecio.getText().toString());
                    desc = txtDesc.getText().toString();
                    palabraClave = txtPalabraClave.getText().toString();
                    potencia = txtPotencia.getText().toString();
                    tamaño = txtTamaño.getText().toString();

                    int valorDado = (int) Math.floor(Math.random()*99999999 + 1);

                    Catalogo datCat = new Catalogo(valorDado, uid, nombre, referencia, desc, precio, url_foto, palabraClave, potencia, tamaño);

                    dbr.child(valorDado + "").setValue(datCat);
                    Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Agregar_objeto_catalogo.this, Activity_Inicio_Empresas.class);
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

    @Override
    public int cargarLayout() {
        return R.layout.activity_agregar_objeto_catalogo;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
