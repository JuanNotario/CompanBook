package com.companbook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
    TextView texto;
    ImageView imagen;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.btnLogIn);
        registrarse = findViewById(R.id.btnRegistrarse);
        accesoUsr = findViewById(R.id.btnAccesoUsr);
        texto=findViewById(R.id.textView2);
        imagen=findViewById(R.id.imageView2);


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
                Button btnCancelar;
                final TextView tvEmail;
                final TextView tvPass;

                DialogLogin dl = new DialogLogin();
                dlog = dl.login(MainActivity.this, mAuth);

                btnLogin = dlog.findViewById(R.id.btnInicioSesion);
                btnCancelar = dlog.findViewById(R.id.btnCancel);
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

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlog.dismiss();
                    }
                });
            }
        });

        accesoUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Lista_Empresas.class);
                startActivity(i);
            }
        });

        Animation myanim1= AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.anim_splash_izquierda);
        texto.startAnimation(myanim2);
        imagen.startAnimation(myanim1);

    }




}
