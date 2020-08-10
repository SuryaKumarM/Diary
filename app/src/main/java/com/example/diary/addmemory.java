package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import model.diary;
import utils.diaryapi;

public class addmemory extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private ImageView backimage;
private ImageView frontimage;
private EditText postabout;
private EditText poststory;
private TextView currentuser;
private ProgressBar postprogress;
private Button savebutton;
private String id;
private String name;
private String phone;
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private FirebaseUser user;
private FirebaseFirestore db=FirebaseFirestore.getInstance();
private StorageReference storageReference;
private CollectionReference collectionReference=db.collection("diary");
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemory);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        backimage=findViewById(R.id.backimage);
        postabout=findViewById(R.id.post_about);
        poststory=findViewById(R.id.post_story);
        postprogress=findViewById(R.id.post_progress);
        currentuser=findViewById(R.id.post_username);
        savebutton=findViewById(R.id.save);
        savebutton.setOnClickListener(this);
        frontimage=findViewById(R.id.frontimage);
        frontimage.setOnClickListener(this);
        postprogress.setVisibility(View.INVISIBLE);
        if(diaryapi.getInstance() !=null)
        {
            id=diaryapi.getInstance().getId();
            name=diaryapi.getInstance().getUsername();
            currentuser.setText(name);
        }
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if(user!=null){

                }else{

                }
            }
        };

    }

    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.save:
        addstory();
        break;
    case R.id.frontimage:
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,GALLERY_CODE);
        break;
}
    }

    private void addstory() {
        final String about=postabout.getText().toString().trim();
        final String story=poststory.getText().toString().trim();
        postprogress.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(about)&&
        !TextUtils.isEmpty(story)&&
        imageUri!=null){
final StorageReference filepath=storageReference
        .child("diary_images")
        .child("image_"+ Timestamp.now().getSeconds());//iamge_281376
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          postprogress.setVisibility(View.INVISIBLE);
                          filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  String imageUri=uri.toString();
                                  diary diary =new diary();
                                  diary.setAbout(about);
                                  diary.setStory(story);
                                  diary.setImageUrl(imageUri);
                                  diary.setTimeadded(new Timestamp(new Date()));
                                  diary.setUsername(name);
                                  diary.setUserId(id);
                                  collectionReference.add(diary).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                      @Override
                                      public void onSuccess(DocumentReference documentReference) {
                                          postprogress.setVisibility(View.INVISIBLE);
                                          startActivity(new Intent(addmemory.this,diarylist.class));
                                          finish();

                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {

                                      }
                                  });
                              }
                          });




                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          postprogress.setVisibility(View.INVISIBLE);
                        }
                    });
        }else{
            postprogress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            if(data!=null){
                imageUri=data.getData();
                backimage.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
