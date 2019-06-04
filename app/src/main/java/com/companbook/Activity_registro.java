package com.companbook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Activity_registro extends AppCompatActivity {

    Button registro;
    Button cancelar;

    EditText email;
    EditText contraseña;
    EditText contraseñaConfirm;

    String txtEmail;
    String txtPass;
    String txtConfirmPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registro = findViewById(R.id.btnRegistro);
        cancelar = findViewById(R.id.btnCancelar);
        email = findViewById(R.id.etEmail);
        contraseña = findViewById(R.id.etContrasenia);
        contraseñaConfirm = findViewById(R.id.etConfirmContrasenia);

        mAuth = FirebaseAuth.getInstance();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = email.getText().toString();
                txtPass = contraseña.getText().toString();
                txtConfirmPass = contraseñaConfirm.getText().toString();

                //FALTA HACER UN IF QUE COMPRUEBE QUE LA CONTRASEÑA TIENE COMO MINIMO 6 CARACTERES

                if (!txtPass.equals(txtConfirmPass)) {
                    Toast.makeText(Activity_registro.this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(Activity_registro.this, "El email debe tener un formato adecuado", Toast.LENGTH_SHORT).show();

                } else if (txtEmail.equals("") || txtPass.equals("") || txtConfirmPass.equals("")) {
                    Toast.makeText(Activity_registro.this, "Todos los campos deben de estar rellenos", Toast.LENGTH_SHORT).show();

                } else {

                    mAuth.createUserWithEmailAndPassword(txtEmail, txtPass)
                            .addOnCompleteListener(Activity_registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Activity_registro.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Activity_registro.this, "Registro fallidos", Toast.LENGTH_SHORT).show();

                                        System.out.println(task.getException());
                                    }
                                }
                            });
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_registro.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            }
        });
    }
}
