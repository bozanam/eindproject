package com.example.bozana.eindproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignup;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mSignup = findViewById(R.id.btnSignUp);
        mSignup.setOnClickListener(this);
        mLogin = findViewById(R.id.btnLogin);
        mLogin.setOnClickListener(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // user is signed in
                    Log.d("logged in ", "onAuthStateChanged: " + user.getUid());
                    Intent intent = new Intent(getApplicationContext(), second_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d("signed out", "onAuthStateChanged: ");
                }
            }
        };

    }

    public void onClick(View view){ //making sure that when a title is clicked, something happens
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        if (view.getId() == R.id.tvTitle){
            startActivity(new Intent(view.getContext(), second_Activity.class));
        } else {
            if (email.length() > 0 && password.length() > 0) {
                if (view.getId() == R.id.btnLogin) {
                    logIn();
                } else if (view.getId() == R.id.btnSignUp) {
                    createUser();}
            } else {
                Toast.makeText(view.getContext(), "Email or password is empty.", Toast.LENGTH_LONG).show();}
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createUser(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d("exist_email", "onComplete: exist_email");
                                Toast.makeText(MainActivity.this, email+ " already exists",
                                        Toast.LENGTH_SHORT).show();}// if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d("bad password", "onComplete: not long enough password");
                                Toast.makeText(MainActivity.this, "Weak password! Password needs to be at least 6 characters  long!",
                                        Toast.LENGTH_LONG).show();}// if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d("wrong email", "onComplete: wrong email");
                                Toast.makeText(MainActivity.this,  "email doesn't exist!",
                                        Toast.LENGTH_SHORT).show();
                            }  catch (Exception e) {
                                Log.d("Error", "onComplete: " + e.getMessage());}
                        }else{
                            logIn();
                        }

                        // ...
                    }
                });
    }

    public void logIn(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {// Sign in success, update UI with the signed-in user's information
                            Log.d("sign in succesfully", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "User " +email+ " signed in!",Toast.LENGTH_SHORT).show();
                            nextPage();
                        } else {// If sign in fails, display a message to the user.
                            Log.w("Failed to login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();}
                    }
                });
    }


    public void nextPage() {
        // When called, makes the user go to the next activity
        Intent intent = new Intent(getApplicationContext(), second_Activity.class);
        startActivity(intent);
    }



    public void goHome(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

}
