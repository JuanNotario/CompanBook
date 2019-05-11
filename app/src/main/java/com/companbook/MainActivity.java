package com.companbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    Button logIn;
    Button registrarse;
    Button accesoUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.btnLogIn);
        registrarse = findViewById(R.id.btnRegistrarse);
        accesoUsr = findViewById(R.id.btnAccesoUsr);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Activity_registro.class);
                startActivity(intent);

            }
        });
    }
}
