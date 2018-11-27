package com.example.burakarslan.instagramfirebaseclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    ArrayList<String> userEmailFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> userCommentFromFB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.add_post) {
                Intent ıntent = new Intent(getApplicationContext(), UploadAcitivity.class);
                startActivity(ıntent);
            }

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);

            userEmailFromFB = new ArrayList<String>();
            userCommentFromFB = new ArrayList<String>();
            userimageFromFB = new ArrayList<String>();

            firebaseDatabase = FirebaseDatabase.getInstance();
            myRef = firebaseDatabase.getReference();

            adapter = new PostClass(userEmailFromFB, userCommentFromFB, userimageFromFB, this);

            listView = (ListView) findViewById(R.id.listview);

            listView.setAdapter(adapter);

            getDataFromFirebase();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void getDataFromFirebase() {
        try {
            DatabaseReference newReferance = firebaseDatabase.getReference("Posts");
            newReferance.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                        userEmailFromFB.add(hashMap.get("useremail"));
                        userCommentFromFB.add(hashMap.get("comment"));
                        userimageFromFB.add(hashMap.get("downloadurl"));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
