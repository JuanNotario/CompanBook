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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Editar_objeto extends Base_Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 150;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;


    Catalogo c;

    ImageView foto;
    EditText nom;
    EditText ref;
    EditText prec;
    EditText palClave;
    EditText desc;
    EditText tam;
    EditText pot;

    String url_foto;
    String nomE;
    String refE;
    Double precE;
    String claveE;
    String descE;
    String tamE;
    String potE;
    String precio;
    String uid;
    int valorDado;
    int codigo = 0;

    FirebaseUser user;
    Uri uri;
    StorageReference destino;
    StorageReference storage;
    DatabaseReference dbr;
    ChildEventListener cel;
    info_CompProfile infoComp;

    Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_editar_objeto);

        storage = FirebaseStorage.getInstance().getReference();

        c = getIntent().getParcelableExtra("CLAVE_EDIT");

        foto = findViewById(R.id.imgFotoEdit);
        nom = findViewById(R.id.etNomEdit);
        ref = findViewById(R.id.etRefEdit);
        prec = findViewById(R.id.etPrecEdit);
        palClave = findViewById(R.id.etClaveEdit);
        desc = findViewById(R.id.etDescEdit);
        tam = findViewById(R.id.etTamEdit);
        pot = findViewById(R.id.etPotEdit);

        Glide.with(Editar_objeto.this).load(c.getUrl_foto()).into(foto);
        url_foto = c.getUrl_foto();
        nom.setText(c.getNombre());
        ref.setText(c.getReferencia());
        prec.setText(c.getPrecio() + "");
        palClave.setText(c.getPalabraClave());
        desc.setText(c.getDesc());
        tam.setText(c.getTamaño());
        pot.setText(c.getPotencia());
        valorDado = c.getRandom();
        uid = c.getUid();

        System.out.println("AAAAAAAA");
        System.out.println(valorDado);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Hacer foto", "Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(Editar_objeto.this);
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
    public void aceptar(View view) {

        //mostrarMiniProgressBar();

        nomE  = nom.getText().toString();
        refE  = ref.getText().toString();
        precio = prec.getText().toString();
        claveE = palClave.getText().toString();
        descE = desc.getText().toString();
        tamE = tam.getText().toString();
        potE = pot.getText().toString();

        if (nomE.trim().isEmpty() || refE.trim().isEmpty() || claveE.trim().isEmpty() || descE.trim().isEmpty() || precio.trim().isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar rellenos", Toast.LENGTH_SHORT).show();

        } else {
            precE = Double.parseDouble(precio);

            if (codigo == 1) {
                subirLogo(getImageUri(this, imageBitmap));

            } else if (codigo == 2) {
                subirLogo(uri);

            } else if (codigo == 0) {

                Catalogo datCat = new Catalogo(valorDado, uid, nomE, refE, descE, precE, url_foto, claveE, potE, tamE);

                System.out.println("AAAAAAAA");
                System.out.println(valorDado);

                dbr.child(valorDado + "").setValue(datCat);
                Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Editar_objeto.this, Activity_Inicio_Empresas.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                /*if (miniProgressBar.isShowing()) {
                    miniProgressBar.cancel();
                }*/
            }
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

                    nomE  = nom.getText().toString();
                    refE  = ref.getText().toString();
                    precio = prec.getText().toString();
                    claveE = palClave.getText().toString();
                    descE = desc.getText().toString();
                    tamE = tam.getText().toString();
                    potE = pot.getText().toString();

                    Catalogo datCat2 = new Catalogo(valorDado, uid, nomE, refE, descE, precE, url_foto, claveE, potE, tamE);

                    dbr.child(valorDado + "").setValue(datCat2);
                    Toast.makeText(getApplicationContext(), "Los datos se han guardado correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Editar_objeto.this, Activity_Inicio_Empresas.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
            codigo = 1;

            infoComp = new info_CompProfile(Editar_objeto.this);
            infoComp.show();


        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            uri = data.getData();
            Glide.with(Editar_objeto.this).load(uri).into(foto);
            codigo = 2;

            infoComp = new info_CompProfile(Editar_objeto.this);
            infoComp.show();
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
        return R.layout.activity_editar_objeto;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
