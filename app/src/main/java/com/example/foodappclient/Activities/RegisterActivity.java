package com.example.foodappclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodappclient.Model.UserModel;
import com.example.foodappclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG="EmailPassword";

    EditText email,password,conPassword,etPhone,etUsername;
    Button register,link_to_login;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);

        reference = FirebaseDatabase.getInstance().getReference().child("Seller");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.cPassword);
        etPhone = findViewById(R.id.phone);
        etUsername = findViewById(R.id.name);


        register = findViewById(R.id.btn_reg);
        link_to_login = findViewById(R.id.btn_login);

        link_to_login.setOnClickListener(view -> onBackPressed());

        register.setOnClickListener(view -> signup());

    }

    private void signup(){
        Log.d(TAG, "signUp"+email);
        if(!validateForm()){
            return;
        }
        String em=email.getText().toString();
        String pw=password.getText().toString();
        String phone = etPhone.getText().toString();
        String username = etUsername.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Creating New Account");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(em, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                            UserModel model = new UserModel();
                            model.setUsername(username);
                            model.setPhone(phone);
                            model.setImage("");
                            model.setUid(user.getUid());
                            model.setEmil(em);

                            reference.child(user.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Failed: "+
                                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                        }
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signUpWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(){
        boolean valid = true;

        String em=email.getText().toString();
        if(TextUtils.isEmpty(em)){
            email.setError("Required.");
            valid=false;
        }
        else {
            email.setError(null);
        }

        String pw = password.getText().toString();
        if (TextUtils.isEmpty(pw)) {
            password.setError("Required.");
            valid=false;
        } else {
            password.setError(null);
        }

        String cpw = conPassword.getText().toString();
        if (TextUtils.isEmpty(cpw)) {
            conPassword.setError("Required.");
            valid=false;
        } else {
            conPassword.setError(null);
        }

        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Required.");
            valid=false;
        } else {
            etPhone.setError(null);
        }

        if (!(cpw.equals(pw))) {
            conPassword.setError("Re Enter Password!");
            valid=false;
        } else {
            conPassword.setError(null);
        }

        return valid;
    }

}