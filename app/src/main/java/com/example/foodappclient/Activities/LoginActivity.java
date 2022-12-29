package com.example.foodappclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodappclient.ChoiceActivity;
import com.example.foodappclient.PreferenceManager;
import com.example.foodappclient.R;
import com.example.foodappclient.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    EditText email, password;
    Button link_to_reg, login;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, ChoiceActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        link_to_reg = findViewById(R.id.btn_reg);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(view -> signIn());

        link_to_reg.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void signIn() {
        Log.d(TAG, "signIn" + email);
        if (!validateForm()) {
            return;
        }

        String em = email.getText().toString();
        String pw = password.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(em, pw)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, ChoiceActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Sign In Failed! " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String em = email.getText().toString();
        if (TextUtils.isEmpty(em)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String pw = password.getText().toString();
        if (TextUtils.isEmpty(pw)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}

