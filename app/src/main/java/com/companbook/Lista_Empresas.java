package com.companbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.companbook.Adapters.AdaptadorListaEmpresas;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Lista_Empresas extends Base_Activity {

    AdaptadorListaEmpresas adapter;
    ArrayList<Datos_Empresa> datos;
    private ChildEventListener cel;
    private DatabaseReference dbr;
    RecyclerView rVEmpresitas;
    LinearLayoutManager llm;
    Datos_Empresa m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_lista__empresas);

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

    protected void onResume(){
        super.onResume();
        addChildEventListener();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_lista__empresas;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
