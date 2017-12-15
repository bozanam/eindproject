package com.example.bozana.eindproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class second_Activity extends AppCompatActivity {

    ArrayList<Integer> myArray = new ArrayList<Integer>() ;
    ListView theListView;
    private FirebaseAuth mAuth;


    JSONArray JSArray;
    ArrayList<String> newArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);
        final String url = "http://poetrydb.org/title";
        final RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        theListView = findViewById(R.id.lijst);
        mAuth = FirebaseAuth.getInstance();


        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
            //Get the data from the API and put it in the adapter
                    @Override
                    public void onResponse(JSONObject response) {
                         newArray = new ArrayList<>();
                            try {

                            JSArray = response.getJSONArray("titles");
                            for (int i = 0; i < JSArray.length(); i++) {
                                newArray.add(JSArray.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setAdapter(newArray);
                    }
                }

                , null);
        queue.add(jsonobject);
        listClick();
    }

    public void listClick(){
        //makes sure something happens when user clicks on a title
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemClicked = adapterView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), LinesActivity.class);
                intent.putExtra("chosenItem", itemClicked);
                startActivity(intent);

            }
        });
    }


    private void setAdapter( ArrayList<String> arry){
        ArrayAdapter<String> myAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arry);
        theListView.setAdapter(myAdapter);
    }


    public void goToFavs(View view) {
        // When the user clickes the button sends him to the favorites activity
        Intent intent = new Intent(getApplicationContext(), favorites.class);
        startActivity(intent);
    }


    public void signOut(View view) {
        //Signs the user out when the button is clicked
        // Sends user to login page
            mAuth.getInstance()
                    .signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
    }
}
