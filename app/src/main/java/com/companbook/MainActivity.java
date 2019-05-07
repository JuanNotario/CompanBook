package com.companbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button logIn;
    Button registrarse;
    Button accesoUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn.findViewById(R.id.btnLogIn);
        registrarse.findViewById(R.id.btnRegistrarse);
        accesoUsr.findViewById(R.id.btnAccesoUsr);
    }
}
