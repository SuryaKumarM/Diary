package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import utils.diaryapi;

public class login extends AppCompatActivity {
  private Button signin2;
  private Button login;
  private FirebaseAuth firebaseAuth;
  private ProgressBar progressBar;
  private FirebaseAuth.AuthStateListener authStateListener;
  private FirebaseUser currentuser;
  private FirebaseFirestore db=FirebaseFirestore.getInstance();
  private CollectionReference collectionReference=db.collection("users");
  private AutoCompleteTextView username;
  private EditText password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
       signin2=findViewById(R.id.sign);
        login=findViewById(R.id.login);
        progressBar=findViewById(R.id.progress);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
       signin2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i =new Intent(login.this,signin.class);
               startActivity(i);
           }
       });
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               loginuser(username.getText().toString().trim(),password.getText().toString().trim());
           }


       });

    }
    private void loginuser(String email, String pwd) {
    if(!TextUtils.isEmpty(email)&&
        !TextUtils.isEmpty(pwd)){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        final String currentuserid=user.getUid();
                        collectionReference.whereEqualTo("userid",currentuserid)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(error!=null){

                                        }
                                        assert value != null;
                                        if(!value.isEmpty()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            for (QueryDocumentSnapshot snapshot:value){
                                                diaryapi diary=diaryapi.getInstance();
                                                diary.setUsername(snapshot.getString("username"));
                                                diary.setId(snapshot.getString("userid"));
                                                diary.setPhone(snapshot.getString("phno"));
                                                startActivity(new Intent(login.this,diarylist.class));
                                                finish();
                                            }

                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        }else{
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(login.this,"please enter valid email and password",Toast.LENGTH_LONG).show();
    }
    }
}
