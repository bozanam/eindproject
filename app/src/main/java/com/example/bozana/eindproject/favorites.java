package com.example.bozana.eindproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class favorites extends AppCompatActivity {

    ListView lijstzicht;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ArrayList titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        lijstzicht = findViewById(R.id.lijstvanfavo);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        lijstzicht = findViewById(R.id.lijstvanfavo);
        mAuth = FirebaseAuth.getInstance();
        titles = new ArrayList<>();
        listClick();
        getFromDB();
    }

    public void getFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            //retrieves the data stored on firebase
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = mAuth.getCurrentUser().getUid().toString();
                for (DataSnapshot dataSnapshot1: dataSnapshot.child("Users").child(user).getChildren()) {
                    String dtsnapshot = dataSnapshot1.getValue().toString();

                    //splits the dtsnapshot object into smaller pieces
                    String[] lijst = dtsnapshot.split(",");

                    for (int i = 0; i < lijst.length; i++) {
                        //removes the unnessessary data and adds the relevant one to "lijst"
                        String substring;
                        if( i == lijst.length - 1){
                            substring = lijst[i].substring(22, lijst[i].length() -1 );
                        } else{
                            substring = lijst[i].substring(22, lijst[i].length());
                        }

                        titles.add(substring);
                    }

                // adds the data to the ListView via an Adapter
                    ArrayAdapter<String> myAdapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
                    lijstzicht.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("OK", "loadPost:onCancelled", databaseError.toException());

            }
        };
        mDatabase.addValueEventListener(postListener);
    }


    public void listClick(){
// makes sure something happens when user clickes on one of the list items.
        lijstzicht.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemClicked = adapterView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), LinesActivity.class);
                intent.putExtra("chosenItem", itemClicked);
                startActivity(intent);

            }
        });
    }


    public void goHome(View view) {
        // Sends the user to the second Activity when the button is clicked
        Intent intent = new Intent(getApplicationContext(), second_Activity.class);
        startActivity(intent);
        finish();
    }
}



