package com.tripshots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tripshots.Data.sharedPref;

public class ActivityLogin extends AppCompatActivity {

    EditText email, password;
    Button btn_login;
    com.tripshots.Data.sharedPref sharedPref;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = new sharedPref(ActivityLogin.this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setBackground(getDrawable(R.drawable.rounded_button_after));
                progressBar.setVisibility(View.VISIBLE);

                if(email.getText().length()!=0 && password.getText().length()!=0){
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        sharedPref.createLoginSession(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        progressBar.setVisibility(View.GONE);

                                        Intent i = new Intent(ActivityLogin.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                    else
                                        Toast.makeText(ActivityLogin.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                else
                    Toast.makeText(ActivityLogin.this, "Please enter correct Email and Password", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
