package com.example.firebasesocialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail,txtUN,txtPass;
    private Button btnSignUp;
    private Button btnSignIn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        txtEmail=findViewById(R.id.txtEmail);
        txtUN=findViewById(R.id.txtUN);
        txtPass=findViewById(R.id.txtPass);
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignUp=findViewById(R.id.btnSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            //transition to next activity
            transitionToSocialMediaActivity();
        }

    }

    private void signUp(){

        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(),txtPass.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"SignUp successful",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("my_users")
                            .child(task.getResult().getUser().getUid())
                            .child("userName").setValue(txtUN.getText().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtUN.getText().toString())
                            .build();

                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Display name updated",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //gets the user name into the firebase database my_users->uid->username value(txtUN)
                    transitionToSocialMediaActivity();

                }else{
                    Toast.makeText(getApplicationContext(),"SignUp not successful",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void signIn(){


        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(),txtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"sign In successful",Toast.LENGTH_SHORT).show();
                    transitionToSocialMediaActivity();

                }else{
                    Toast.makeText(getApplicationContext(),"please enter correct email and password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void transitionToSocialMediaActivity(){

        Intent intent=new Intent(this,SocialMediaActivity.class);
        startActivity(intent);
    }
}
