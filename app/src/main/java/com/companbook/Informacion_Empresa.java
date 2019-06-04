package com.companbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.companbook.Adapters.AdaptadorListaEmpresas;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Informacion_Empresa extends Base_Activity {

    ImageView ivLogo;
    TextView nombre;
    TextView descripcion;
    TextView direccion;

    Datos_Empresa u;
    private DatabaseReference dbr;
    AdaptadorListaEmpresas adapter;
    ArrayList<Datos_Empresa> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_informacion__empresa);

        adapter = new AdaptadorListaEmpresas(datos, this);
        u = getIntent().getParcelableExtra("CLAVE");

        ivLogo = findViewById(R.id.muestraLogo);
        nombre = findViewById(R.id.tvNomInf);
        descripcion = findViewById(R.id.tvDescInf);
        direccion = findViewById(R.id.tvDireccInf);

        Glide.with(this).load(u.getUrl_logo()).into(ivLogo);
        nombre.setText(u.getNombre());
        direccion.setText(u.getDirección());
        descripcion.setText(u.getDescripcion());

        dbr = FirebaseDatabase.getInstance().getReference().child("Empresa");
    }

    public void catEmpresa(View view) {
        System.out.println("1");
        Intent i = new Intent(this, Catalogo_Empresa_Elegida.class);
        System.out.println("2");
        i.putExtra("CLAVECITA", u);
        System.out.println("3");
        startActivity(i);
        System.out.println("4");

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_informacion__empresa;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
