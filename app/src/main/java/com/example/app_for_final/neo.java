package com.example.app_for_final;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class neo extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    String id,name,diameter,date,velocity,distance,is_potentially_hazardous_asteroid,today;

    ArrayList<HashMap<String, String>> contactList;

    String[] neo_id = new String[100];
    String[] date_full = new String[100];
    String[] velocity_earth = new String[100];
    String[] distance_earth = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neo);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        getSupportActionBar().hide();

        new GetContacts().execute();

    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(neo.this,"Fetching data from NASA directory...",Toast.LENGTH_LONG).show();

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String url = "https://api.nasa.gov/neo/rest/v1/feed?start_date="+today+"&end_date="+today+"&api_key=3vgYWNjKFyD7st8laqSmoZBka5uHuluQcYXusMtr";
            String jsonStr = sh.makeServiceCall(url);

            // Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);
                    JSONObject temp = c.getJSONObject("near_earth_objects");
                    JSONArray x = temp.getJSONArray(today);
                    for (int i =0 ; i<x.length() ; i++)
                    {
                        JSONObject data = x.getJSONObject(i);
                        id = String.valueOf(data.getString("id"));
                        neo_id[i] = id;
                        name = data.getString("name");
                        Log.e(TAG,"ID: "+id);
                        Log.e(TAG,"Name: "+name);

                        JSONObject temp_diameter = data.getJSONObject("estimated_diameter");
                        temp_diameter = temp_diameter.getJSONObject("meters");
                        diameter = String.valueOf ((temp_diameter.getInt("estimated_diameter_min")+temp_diameter.getInt("estimated_diameter_max"))/2)+" meters";
                        Log.e(TAG,"Diameter: "+diameter);

                        JSONArray jsonArray = data.getJSONArray("close_approach_data");
                        temp_diameter = jsonArray.getJSONObject(0);
                        date = temp_diameter.get("close_approach_date_full").toString();
                        date_full[i] = date;

                        JSONObject temp_diameter1 = temp_diameter.getJSONObject("relative_velocity");
                        velocity =temp_diameter1.get("kilometers_per_hour").toString()+" km/h";
                        velocity_earth[i] = velocity;


                        temp_diameter1 = temp_diameter.getJSONObject("miss_distance");
                        distance = temp_diameter1.get("kilometers").toString()+" km";
                        distance_earth[i] = distance;

                        is_potentially_hazardous_asteroid = data.get("is_potentially_hazardous_asteroid").toString();

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("name", "Asteroid Name: "+name);
                        contact.put("diameter","Estimated Diameter: "+diameter);
                        contact.put("date", "Close Approach Date & Time: "+date);
                        contact.put("velocity", "Velocity: "+velocity);
                        contact.put("distance","Distance to Earth: "+distance);
                        if(is_potentially_hazardous_asteroid=="true")
                        {
                            contact.put("report","Report: This asteroid could be dangerous to planet Earth!" );
                        }
                        else {
                            contact.put("report","Report: This asteroid poses no threat to planet Earth!" );
                        }


                        // adding contact to contact list
                        contactList.add(contact);
                    }





                    // looping through All Contacts

                    // tmp hash map for single contact


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(neo.this, contactList,
                    R.layout.neo_list, new String[]{ "name","diameter","date","velocity","distance","report"},
                    new int[]{R.id.name,R.id.diameter,R.id.date,R.id.velocity,R.id.distance,R.id.is_potentially_hazardous_asteroid});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e(TAG,"Neo_id: "+neo_id[i]);
                    Intent intent = new Intent(neo.this,MainActivity.class);
                    intent.putExtra("neo_id",neo_id[i]);
                    intent.putExtra("date",date_full[i]);
                    intent.putExtra("velocity",velocity_earth[i]);
                    intent.putExtra("distance",distance_earth[i]);
                    startActivity(intent);
                }
            });
        }
    }
}
