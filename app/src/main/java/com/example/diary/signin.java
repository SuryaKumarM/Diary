package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import utils.diaryapi;

public class signin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private ProgressBar progressBar1;
    private AutoCompleteTextView username1;
    private EditText phno1;
    private AutoCompleteTextView email1;
    private EditText password1;
    private Button signin1;
    //firestore connection
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar1=findViewById(R.id.progress_s);
        username1=findViewById(R.id.username_s);
        phno1=findViewById(R.id.phone_s);
        email1=findViewById(R.id.email_s);
        password1=findViewById(R.id.password_s);
        signin1=findViewById(R.id.sign_s);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();
                if(currentuser!=null)
                {
                    //already logged in
                }
                else
                {
                    //no user at
                }
            }
        };
        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email1.getText().toString())
                        && !TextUtils.isEmpty(password1.getText().toString())
                        && !TextUtils.isEmpty(username1.getText().toString())
                        && !TextUtils.isEmpty(phno1.getText().toString())){
                    String email=email1.getText().toString().trim();
                    String password=password1.getText().toString().trim();
                    String username=username1.getText().toString().trim();
                    String phno=phno1.getText().toString().trim();
                    createnew(email, password, username, phno);
                }
                else{
                    Toast.makeText(signin.this,"Empty fields not allowed",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    private void createnew(String email, String password, final String username, final String phno) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(phno)) {
            progressBar1.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentuser = firebaseAuth.getCurrentUser();
                                final String userid = currentuser.getUid();
                                Map<String, String> userobj = new HashMap<>();
                                userobj.put("userid", userid);
                                userobj.put("username", username);
                                userobj.put("phno", phno);
                                collectionReference.add(userobj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        documentReference.get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                                            progressBar1.setVisibility(View.INVISIBLE);
                                                            String name=task.getResult().getString("username");
                                                            String phone=task.getResult().getString("phno1");
                                                            diaryapi diaryapi= utils.diaryapi.getInstance();
                                                            diaryapi.setUsername(name);
                                                            diaryapi.setId(userid);
                                                            diaryapi.setPhone(phone);
                                                            Intent i=new Intent(signin.this,diarylist.class);
                                                            i.putExtra("username",name);
                                                            i.putExtra("phno",phone);
                                                            i.putExtra("id",userid);
                                                            startActivity(i);

                                                        }else{
                                                            progressBar1.setVisibility(View.INVISIBLE);

                                                        }
                                                    }
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            } else {

//something went wrong
                            }
                        }


                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
