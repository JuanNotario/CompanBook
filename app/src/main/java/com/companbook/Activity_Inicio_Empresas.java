package com.companbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_Inicio_Empresas extends Base_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__inicio__empresas);
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
