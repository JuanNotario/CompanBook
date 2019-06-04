package com.companbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.companbook.Pojos.Datos_Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public abstract class Base_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Menu menuv;
    private NavigationView navigationView;
    private DatabaseReference dbr;
    private ArrayList<Datos_Empresa> listaEmpresas;
    private ChildEventListener cel;
    ImageView imgLogoMenu;
    TextView nomMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View container = findViewById(R.id.contenedorLayout);
        ConstraintLayout rel = (ConstraintLayout) container;
        getLayoutInflater().inflate(cargarLayout(), rel);

        /*loatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(setDrawer());
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        //Cargamos la foto y el nombre en el menú
        imgLogoMenu = hView.findViewById(R.id.logoEmresa);
        nomMenu = hView.findViewById(R.id.tvNomMenu);
        listaEmpresas = new ArrayList<>();
        dbr = FirebaseDatabase.getInstance().getReference().child("Empresa");

        if (cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Datos_Empresa datEmp = dataSnapshot.getValue(Datos_Empresa.class);
                    listaEmpresas.add(datEmp);

                    FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();

                    if (userFirebase != null) {
                        for (int i = 0; i < listaEmpresas.size(); i++) {
                            if (listaEmpresas.get(i).getUid().equals(userFirebase.getUid())) {
                                Glide.with(Base_Activity.this).load(listaEmpresas.get(i).getUrl_logo()).into(imgLogoMenu);
                                nomMenu.setText(listaEmpresas.get(i).getNombre());
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

    //Metodo abstracto que utilizamos desde la clase que extienda para cargar el layout.
    public abstract int cargarLayout();

    //Metodo abstracto para establecer la modalidad del activity (menu o back)
    public abstract boolean setDrawer();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_, menu);
        this.menuv = menu;

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent i = new Intent(this, Perfil_Empresa.class);
            startActivity(i);

        } else if (id == R.id.nav_agregarCatalogo) {
            Intent i = new Intent(this, Agregar_objeto_catalogo.class);
            startActivity(i);

        } else if (id == R.id.nav_catalogo) {
            Intent i = new Intent(this, Activity_Inicio_Empresas.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logOut) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
