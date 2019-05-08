package com.companbook;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class DialogLogin {

    Button registro;
    Button cancelar;

    EditText email;
    EditText contraseña;
    EditText contraseñaConfirm;

    String txtEmail;
    String txtPass;
    String txtConfirmPass;

    public void dialogLogin(Context context) {
        final Dialog iniSes = new Dialog(context);
        iniSes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        iniSes.setCancelable(false);
        iniSes.setContentView(R.layout.dialog_registro);

        registro = iniSes.findViewById(R.id.btnRegistro);
        cancelar = iniSes.findViewById(R.id.btnCancelar);
        email = iniSes.findViewById(R.id.etEmail);
        contraseña = iniSes.findViewById(R.id.etContrasenia);
        contraseñaConfirm = iniSes.findViewById(R.id.etConfirmContrasenia);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = String.valueOf(email.getText());
                txtPass = String.valueOf(contraseña.getText());
                txtConfirmPass = String.valueOf(contraseñaConfirm.getText());

                if (txtEmail.equals("") || txtEmail.equals(null)) {
                    if (txtPass.equals(txtConfirmPass)) {
                        iniSes.dismiss();
                    } else {
                        //Nada aun
                    }
                } else {

                    //Nada aun
                }


            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniSes.dismiss();
            }
        });

        iniSes.show();
    }
}
