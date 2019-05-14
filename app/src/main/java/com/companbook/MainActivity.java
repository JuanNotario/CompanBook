package com.companbook;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button logIn;
    Button registrarse;
    Button accesoUsr;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.btnLogIn);
        registrarse = findViewById(R.id.btnRegistrarse);
        accesoUsr = findViewById(R.id.btnAccesoUsr);


        mAuth = FirebaseAuth.getInstance();

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_registro.class);
                startActivity(intent);

            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dlog;
                Button btnLogin;
                final TextView tvEmail;
                final TextView tvPass;

                DialogLogin dl = new DialogLogin();
                dlog = dl.login(MainActivity.this, mAuth);

                btnLogin = dlog.findViewById(R.id.btnInicioSesion);
                tvEmail = dlog.findViewById(R.id.etEmail);
                tvPass = dlog.findViewById(R.id.etContrasenia);

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txtEmail = tvEmail.getText().toString();
                        String txtPass = tvPass.getText().toString();

                        mAuth.signInWithEmailAndPassword(txtEmail, txtPass)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(MainActivity.this, Activity_Inicio_Empresas.class);
                                            startActivity(intent);

                                            System.out.println("EMAIL");
                                            System.out.println(mAuth.getCurrentUser().getEmail());

                                        } else {
                                            Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }
}
