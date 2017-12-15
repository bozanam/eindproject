package com.example.bozana.eindproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class LinesActivity extends AppCompatActivity {

    ListView lijstview;
    JSONArray JArray;
    ArrayList<String> volgendeArray;
    String gekozen;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);
        Intent intent = getIntent();
        gekozen = intent.getStringExtra("chosenItem").toString();
        mAuth = FirebaseAuth.getInstance();

        Log.d("HIER", "onCreate: " +gekozen);
        //String gekozen = intent.getExtras("chosenItem", itemClicked);
        final String url = "http://poetrydb.org/title/" + gekozen;
        Log.d("URL", "onCreate: " + url);
        final RequestQueue queue = Volley.newRequestQueue(this);
        lijstview = findViewById(R.id.lijnen);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListAdapter theAdapter = new ArrayAdapter<String>(LinesActivity.this,android.R.layout.simple_list_item_1);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        volgendeArray = new ArrayList<>();
                        //JSONArray JSArray;
                        Log.d("line", response.toString());

                        try {
                            JSONObject object = response.getJSONObject(0);
                            JSONArray test = object.getJSONArray("lines");

                            //JArray = response.getJSONArray("lines");
                            for (int i = 0; i < test.length(); i++) {
                                volgendeArray.add(test.getString(i));
                                Log.d("line", test.getString(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        setAdapter(volgendeArray);

                    }
                }

                , null);
        queue.add(jsonArrayRequest);
    }


    public void AddtoDB(){
        FirebaseUser user = mAuth.getCurrentUser();
        String title = gekozen.toString();



        mDatabase.child("Users").child(user.getUid()).child("favorite").push().setValue((gekozen));


    }

    private void setAdapter( ArrayList<String> arry){
        ArrayAdapter<String> myAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arry);


        lijstview.setAdapter(myAdapter);

    }


    public void addToFavs(View view) {
        AddtoDB();
    }



    public void goToFavs(View view) {
        Intent intent = new Intent(getApplicationContext(), favorites.class);
        startActivity(intent);

    }



}
