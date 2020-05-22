package com.example.firebasesocialmediaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView postsListView;
    private ArrayList<String> postsFromUsers;
    private ArrayAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private ImageView sentPostImage;
    private TextView txtDescription;
    private ArrayList<DataSnapshot> dataSnapshots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        firebaseAuth=FirebaseAuth.getInstance();
        postsListView=findViewById(R.id.postsListView);
        sentPostImage=findViewById(R.id.sentPostImage);
        txtDescription=findViewById(R.id.txtDescription);
        dataSnapshots=new ArrayList<>();
        postsFromUsers=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,postsFromUsers);
        postsListView.setAdapter(adapter);
        postsListView.setOnItemClickListener(this);
        FirebaseDatabase.getInstance().getReference().child("my_users").child(firebaseAuth.getCurrentUser().getUid()).child("recieved_posts").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                dataSnapshots.add(dataSnapshot);

                String fromWhomUsername=(String) dataSnapshot.child("fromWhom").getValue();
                postsFromUsers.add(fromWhomUsername);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataSnapshot myDataSnapShots=dataSnapshots.get(position);
        String downloadLink=(String)myDataSnapShots.child("imageLink").getValue();
        Picasso.get().load(downloadLink).into(sentPostImage);
        txtDescription.setText((String)myDataSnapShots.child("des").getValue());

    }
}
