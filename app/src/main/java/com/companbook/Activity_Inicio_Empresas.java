package com.companbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_Inicio_Empresas extends Base_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__inicio__empresas);
    }

    public void pruebaPerfil(View view) {
        Intent i = new Intent(this,Perfil_Empresa.class);
        startActivity(i);
    }

    public void pruebaPerfilCatalogo(View view) {
        Intent i = new Intent(this,Agregar_objeto_catalogo.class);
        startActivity(i);
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
