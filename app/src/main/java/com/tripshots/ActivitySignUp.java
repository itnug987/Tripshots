package com.tripshots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripshots.Data.sharedPref;

public class ActivitySignUp extends AppCompatActivity {

    EditText email, password;
    Button btn_register;
    sharedPref sharedPref;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPref = new sharedPref(ActivitySignUp.this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                       .addOnCompleteListener(ActivitySignUp.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   sharedPref.createLoginSession();
                                   Intent i = new Intent(ActivitySignUp.this, MainActivity.class);
                                   startActivity(i);
                               } else{
                                   Toast.makeText(ActivitySignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


    }


}
