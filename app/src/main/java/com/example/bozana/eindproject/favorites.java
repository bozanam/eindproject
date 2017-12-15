package com.example.bozana.eindproject;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
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

        getFromDB();


    }


    public void getFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = mAuth.getCurrentUser().getUid().toString();
                for (DataSnapshot dataSnapshot1: dataSnapshot.child("Users").child(user).getChildren()) {
                    String dtsnapshot = dataSnapshot1.getValue().toString();
                    Log.d("HIERR", "onDataChange: " + Objects.toString(dtsnapshot));

                    String[] lijst = dtsnapshot.split(",");
                    Log.d("Tweede key", "   " + Arrays.toString(lijst)); // database vanaf tomdekr  // ?? waarom 3x ??

                    for (int i = 0; i < lijst.length; i++) {

                        String[] lijst2 = dtsnapshot.split("=");
                        titles.add(lijst2[i]);
                    }

                    ArrayAdapter<String> myAdapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
                    lijstzicht.setAdapter(myAdapter);

                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("OK", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }


}



