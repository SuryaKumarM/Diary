package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.diary;
import ui.DiaryRecyclerAdapter;
import utils.diaryapi;

public class diarylist extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private String name;
    private String id;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<diary> diaryList;
    private RecyclerView recyclerView;
    private DiaryRecyclerAdapter diaryRecyclerAdapter;
    private CollectionReference collectionReference=db.collection("diary");
    private TextView nostory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarylist);
        firebaseAuth=FirebaseAuth.getInstance();
        currentuser=firebaseAuth.getCurrentUser();

        nostory=findViewById(R.id.no_story);
        diaryList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if(diaryapi.getInstance() !=null)
        {
            id=diaryapi.getInstance().getId();
            name=diaryapi.getInstance().getUsername();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(currentuser!=null && firebaseAuth!=null){
                    Intent i=new Intent(diarylist.this,addmemory.class);
                    i.putExtra("username",name);
                    i.putExtra("id",id);
                    startActivity(i);
                }
                break;
            case R.id.action_signout:
                if(currentuser!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(diarylist.this,MainActivity.class));
                }
                break;
            case R.id.action_about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId",diaryapi.getInstance().getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot stories:queryDocumentSnapshots){
                                diary d= stories.toObject(diary.class);
                                diaryList.add(d);
                            }
                            diaryRecyclerAdapter=new DiaryRecyclerAdapter(diarylist.this,diaryList);
                            recyclerView.setAdapter(diaryRecyclerAdapter);
                            diaryRecyclerAdapter.notifyDataSetChanged();
                        }else{
                           nostory.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
