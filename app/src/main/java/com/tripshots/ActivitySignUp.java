package com.tripshots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tripshots.Data.sharedPref;
import com.tripshots.model.User;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {

    EditText email, password;
    Button btn_register;
    sharedPref sharedPref;

    ProgressBar progressBar;

    EditText name, location;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPref = new sharedPref(ActivitySignUp.this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);

        btn_register = findViewById(R.id.btn_register);

        progressBar = findViewById(R.id.progress_bar);


        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_register.setBackground(getDrawable(R.drawable.rounded_button_after));

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(ActivitySignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        sharedPref.createLoginSession(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        User user = new User(name.getText().toString(), email.getText().toString(),
                                                 location.getText().toString());

                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    Toast.makeText(ActivitySignUp.this,
                                                            "Registration Success", Toast.LENGTH_SHORT).show();

                                                    progressBar.setVisibility(View.GONE);

                                                    Intent i = new Intent(ActivitySignUp.this, MainActivity.class);
                                                    startActivity(i);
                                                }else{
                                                    Toast.makeText(ActivitySignUp.this,
                                                            "Registration Not Success", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });


                                    } else {
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
