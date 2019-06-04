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
import android.widget.TextView;

import com.companbook.Adapters.AdaptadorInicioEmpresas;
import com.companbook.Adapters.AdaptadorListaCatalogo;
import com.companbook.Pojos.Catalogo;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Activity_Inicio_Empresas extends Base_Activity {

    AdaptadorInicioEmpresas adapter;
    ArrayList<Catalogo> datos;
    private ChildEventListener cel;
    private DatabaseReference dbr;
    RecyclerView rVIniCat;
    LinearLayoutManager llm;
    Catalogo m;

    FirebaseUser user;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity__inicio__empresas);

        datos = new ArrayList<Catalogo>();
        adapter = new AdaptadorInicioEmpresas(datos, this);

        rVIniCat = findViewById(R.id.rvCatalogo);

        user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            uid = user.getUid();

        }

        llm = new LinearLayoutManager(this);

        rVIniCat.setLayoutManager(llm);
        rVIniCat.setAdapter(adapter);
        rVIniCat.setItemAnimator(new DefaultItemAnimator());
        dbr = FirebaseDatabase.getInstance().getReference().child("Catalogo");

        /*adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datos_Empresa u = datos.get(rVEmpresitas.getChildAdapterPosition(v));
                Intent intent = new Intent(Lista_Empresas.this, Informacion_Empresa.class);
                intent.putExtra("CLAVE", u);
                startActivity(intent);
            }
        });*/
    }

    private void addChildEventListener() {
        if(cel == null){
            cel= new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    m = (Catalogo) dataSnapshot.getValue(Catalogo.class);

                    if (m.getUid().equals(uid)) {
                        datos.add(m);
                    }

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
        return R.layout.activity__inicio__empresas;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
