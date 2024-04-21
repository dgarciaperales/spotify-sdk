package com.example.spotify_sdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button backButton;

    private EditText email;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.back_button_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
                finish();
            } 
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString();
                String password_txt = password.getText().toString();
                loginUser(email_txt, password_txt);
            }
        });
    }

    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                //checkAccountExists(email);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void checkAccountExists(String email){
        CollectionReference usersRef = firestore.collection("users");
        DocumentReference userDocRef = usersRef.document(email);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    //document exists
                    Log.d("firestore", "document exists");
                } else {
                    //document does not exist
                    Map<String, String> userData = new HashMap<>();
                    userData.put("email", email);
                    //CollectionReference usersRef = firestore.collection("users");
                    usersRef.document(email).set(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(RegisterActivity.this, "Data added to Firebase", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(RegisterActivity.this, "Failed to add user data to Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Map<String, Integer> wrappedCounter = new HashMap<>();
                    wrappedCounter.put("wrappedCounter", 1);
                    usersRef.document(email).set(wrappedCounter, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(RegisterActivity.this, "Wrapped counter initialized", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(RegisterActivity.this, "Wrapper counter not made", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Map<String, ArrayList<String>> following = new HashMap<>();
                    following.put("following", null);
                    usersRef.document(email).set(following, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "Following collection initialized");
                        }
                    });
                    usersRef.document(email).collection("wrapped")
                            .add(new HashMap<>())
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("Firestore", "Collection 'wrapped' added");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firestore", "Error adding collection", e);
                                }
                            });
                    //Toast.makeText(RegisterAct.this, "You've registered!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            } else {
                Log.d("Document", "Failed with: ", task.getException());
            }
        });
    }
}