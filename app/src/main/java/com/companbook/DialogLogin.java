package com.companbook;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DialogLogin {

    Button btnLogin;

    String txtEmail;
    String txtPass;

    Context fContext;

    public Dialog login(Context context, final FirebaseAuth mAuth) {

        fContext = context;

        Dialog login = new Dialog(fContext);

        login.setCancelable(false);
        login.setContentView(R.layout.dialog_login);

        login.show();



        return login;
    }
}
