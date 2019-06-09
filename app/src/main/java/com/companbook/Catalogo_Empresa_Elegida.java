package com.companbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.companbook.Adapters.AdaptadorListaCatalogo;
import com.companbook.Adapters.AdaptadorListaEmpresas;
import com.companbook.Pojos.Catalogo;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Catalogo_Empresa_Elegida extends AppCompatActivity {

    AdaptadorListaCatalogo adapter;
    ArrayList<Catalogo> datos;
    private ChildEventListener cel;
    private DatabaseReference dbr;
    RecyclerView rVCat;
    LinearLayoutManager llm;
    Catalogo m;
    Datos_Empresa c;

    ArrayList<Catalogo> todosObjetos;

    TextView nomE;
    String uid;

    String nomObjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo__empresa__elegida);

        todosObjetos = new ArrayList<>();

        c = getIntent().getParcelableExtra("CLAVECITA");

        datos = new ArrayList<Catalogo>();
        adapter = new AdaptadorListaCatalogo(datos, this);

        rVCat = findViewById(R.id.rvCatEmprElg);
        nomE = findViewById(R.id.tvEmpresaMensajeCatalogo);

        nomE.setText(c.getNombre());
        uid = c.getUid();

        llm = new LinearLayoutManager(this);

        rVCat.setLayoutManager(llm);
        rVCat.setAdapter(adapter);
        rVCat.setItemAnimator(new DefaultItemAnimator());
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

                    if (m.getUid().equals(c.getUid())) {
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


    public void filtrarCatEleg(View view) {
        final EditText etN;
        Button btnAceptar;
        Button btnCancelar;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_objeto);
        dialog.setCancelable(false);

        etN = dialog.findViewById(R.id.etNomCatBus);
        btnAceptar = dialog.findViewById(R.id.btnAcpBus);
        btnCancelar = dialog.findViewById(R.id.btnCancelBus);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nomObjeto = etN.getText().toString().toLowerCase();

                if (nomObjeto.equals("")) {

                    Toast.makeText(Catalogo_Empresa_Elegida.this, "Deben rellenarse todos los campos", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayList<Catalogo> catalogoFiltrado = new ArrayList<>();

                    for (int i = 0; i < datos.size(); i++) {
                        if (datos.get(i).getNombre().toLowerCase().equals(nomObjeto)) {
                            catalogoFiltrado.add(datos.get(i));
                        }
                    }

                    if (catalogoFiltrado.isEmpty()) {
                        Toast.makeText(Catalogo_Empresa_Elegida.this, "No existen objetos con esta palabra clave", Toast.LENGTH_SHORT).show();

                    } else {
                        todosObjetos.clear();

                        todosObjetos.addAll(datos);

                        datos.clear();

                        datos.addAll(catalogoFiltrado);

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

    public void mostrarTodasEleg(View view) {
        if(todosObjetos.isEmpty()) {
            //No se hace nada

        } else {
            datos.clear();

            datos.addAll(todosObjetos);

            adapter.notifyDataSetChanged();

        }
    }

    public void ordenarCatalogo(View view) {
        final CharSequence[] lista = {"Ordenar por Precio de mayor a menor", "Ordenar por Precio de menor a mayor"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ordenar catálogo");

        builder.setItems(lista, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ( i == 0) {
                    mayorMenor();
                } else {
                    menorMayor();
                }
            }
        });

        builder.show();
    }

    public void menorMayor() {
        System.out.println("AAAAAAAAAA");
        for (int y = 0; y < datos.size(); y++) {
            System.out.println(datos.get(y).getNombre());
        }

        for (int x = 0; x < datos.size(); x++) {
            for (int i = 0; i < datos.size()-x-1; i++) {
                if(datos.get(i).getPrecio() < datos.get(i+1).getPrecio()){
                    Catalogo tmp = datos.get(i);
                    datos.set(i+1, datos.get(i));
                    datos.set(i, tmp);
                }
            }
        }

        System.out.println("BBBBBBBBBBB");
        for (int j = 0; j < datos.size(); j++) {
            System.out.println(datos.get(j).getNombre());
        }

        adapter.notifyDataSetChanged();

    }

    public void mayorMenor() {

    }

    protected void onResume(){
        super.onResume();
        addChildEventListener();
    }
}
