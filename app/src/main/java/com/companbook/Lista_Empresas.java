package com.companbook;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.companbook.Adapters.AdaptadorListaEmpresas;
import com.companbook.Pojos.Catalogo;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Lista_Empresas extends AppCompatActivity {

    AdaptadorListaEmpresas adapter;
    ArrayList<Datos_Empresa> datos;
    ArrayList<Datos_Empresa> todasEmpresas;
    private ChildEventListener cel;
    private DatabaseReference dbr;
    RecyclerView rVEmpresitas;
    LinearLayoutManager llm;
    Datos_Empresa m;

    String nomEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__empresas);

        todasEmpresas = new ArrayList<>();
        datos = new ArrayList<Datos_Empresa>();
        adapter = new AdaptadorListaEmpresas(datos, this);
        rVEmpresitas = findViewById(R.id.rvEmpresas);

        llm = new LinearLayoutManager(this);

        rVEmpresitas.setLayoutManager(llm);
        rVEmpresitas.setAdapter(adapter);
        rVEmpresitas.setItemAnimator(new DefaultItemAnimator());
        dbr = FirebaseDatabase.getInstance().getReference().child("Empresa");

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datos_Empresa u = datos.get(rVEmpresitas.getChildAdapterPosition(v));
                Intent intent = new Intent(Lista_Empresas.this, Informacion_Empresa.class);
                intent.putExtra("CLAVE", u);
                startActivity(intent);
            }
        });
    }

    private void addChildEventListener() {
        if(cel == null){
            cel= new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    m = (Datos_Empresa) dataSnapshot.getValue(Datos_Empresa.class);
                    datos.add(m);
                    adapter.notifyItemInserted(datos.size()-1);
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

    public void mostrarTodas(View view) {
        if(todasEmpresas.isEmpty()) {
            //No se hace nada

        } else {
            datos.clear();

            datos.addAll(todasEmpresas);

            adapter.notifyDataSetChanged();

        }
    }

    public void filtrarEmpresas(View view) {
        final EditText etNombre;
        Button btnAceptar;
        Button btnCancelar;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_empresa);
        dialog.setCancelable(false);

        etNombre = dialog.findViewById(R.id.etNomEmpresa);
        btnAceptar = dialog.findViewById(R.id.btnAcpNomEmp);
        btnCancelar = dialog.findViewById(R.id.btnCancNomEmpr);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nomEmpresa = etNombre.getText().toString().toLowerCase();

                if (nomEmpresa.equals("")) {

                    Toast.makeText(Lista_Empresas.this, "Deben rellenarse todos los campos", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayList<Datos_Empresa> empresasFiltrado = new ArrayList<>();

                    for (int i = 0; i < datos.size(); i++) {
                        if (datos.get(i).getNombre().toLowerCase().equals(nomEmpresa)) {
                            empresasFiltrado.add(datos.get(i));
                        }
                    }

                    if (empresasFiltrado.isEmpty()) {
                        Toast.makeText(Lista_Empresas.this, "No existen objetos con esta palabra clave", Toast.LENGTH_SHORT).show();

                    } else {
                        todasEmpresas.clear();

                        todasEmpresas.addAll(datos);

                        datos.clear();

                        datos.addAll(empresasFiltrado);

                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void onResume(){
        super.onResume();
        addChildEventListener();
    }
}
