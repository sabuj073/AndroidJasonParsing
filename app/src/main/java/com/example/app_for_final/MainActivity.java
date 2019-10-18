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

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String id,date_full;
    String name,absolute_magnitude_h,diameter,date,velocity,distance,epoch_date_close_approach,orbiting_body,orbit_id,orbit_determination_date,first_observation_date,
            last_observation_date,data_arc_in_days,observations_used,orbit_uncertainty,minimum_orbit_intersection,jupiter_tisserand_invariant,epoch_osculation,eccentricity,
            semi_major_axis,inclination,ascending_node_longitude,orbital_period,perihelion_distance,perihelion_argument,aphelion_distance,perihelion_time,mean_anomaly,
            mean_motion,equinox,orbit_class_type,orbit_class_description,orbit_class_range,is_sentry_object,is_potentially_hazardous_asteroid;

    private static final String TAG = HttpHandler.class.getSimpleName();

    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getStringExtra("neo_id");
        date_full = getIntent().getStringExtra("date");
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        getSupportActionBar().hide();

        new GetContacts().execute();


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Fetching data from NASA directory...", Toast.LENGTH_LONG).show();

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            // String url = "http://api.androidhive.info/contacts/";
            String url = "https://api.nasa.gov/neo/rest/v1/neo/"+id+"?api_key=3vgYWNjKFyD7st8laqSmoZBka5uHuluQcYXusMtr";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    // Getting JSON Array node

                    name =c.get("name").toString();
                    absolute_magnitude_h = c.get("absolute_magnitude_h").toString();
                    JSONObject temp_diameter = c.getJSONObject("estimated_diameter");
                    temp_diameter = temp_diameter.getJSONObject("meters");
                    diameter = String.valueOf ((temp_diameter.getInt("estimated_diameter_min")+temp_diameter.getInt("estimated_diameter_max"))/2)+" meters";

                    JSONArray jsonArray = c.getJSONArray("close_approach_data");
                    temp_diameter = jsonArray.getJSONObject(0);
                    date = date_full;

                    JSONObject temp_diameter1 = temp_diameter.getJSONObject("relative_velocity");
                    //velocity =temp_diameter1.get("kilometers_per_hour").toString()+" km/h";
                    velocity = getIntent().getStringExtra("velocity");
                    temp_diameter1 = temp_diameter.getJSONObject("miss_distance");
                    //distance = temp_diameter1.get("kilometers").toString()+" km";
                    distance = getIntent().getStringExtra("distance");
                    epoch_date_close_approach = temp_diameter.get("epoch_date_close_approach").toString();
                    orbiting_body = temp_diameter.getString("orbiting_body");

                    temp_diameter = c.getJSONObject("orbital_data");
                    orbit_id = temp_diameter.get("orbit_id").toString();
                    orbit_determination_date = temp_diameter.get("orbit_determination_date").toString();
                    first_observation_date = temp_diameter.get("first_observation_date").toString();
                    last_observation_date = temp_diameter.get("last_observation_date").toString();
                    data_arc_in_days = temp_diameter.get("data_arc_in_days").toString();
                    observations_used = temp_diameter.get("observations_used").toString();
                    orbit_uncertainty = temp_diameter.get("orbit_uncertainty").toString();
                    minimum_orbit_intersection = temp_diameter.get("minimum_orbit_intersection").toString();
                    jupiter_tisserand_invariant = temp_diameter.get("jupiter_tisserand_invariant").toString();
                    epoch_osculation = temp_diameter.get("epoch_osculation").toString();
                    eccentricity = temp_diameter.get("eccentricity").toString();
                    semi_major_axis = temp_diameter.get("semi_major_axis").toString();
                    inclination = temp_diameter .get("inclination").toString();
                    ascending_node_longitude = temp_diameter.get("ascending_node_longitude").toString();
                    orbital_period = temp_diameter.get("orbital_period").toString();
                    perihelion_distance = temp_diameter.get("perihelion_distance").toString();
                    perihelion_argument = temp_diameter.get("perihelion_argument").toString();
                    aphelion_distance = temp_diameter.get("aphelion_distance").toString();
                    perihelion_time = temp_diameter.get("perihelion_time").toString();
                    mean_anomaly = temp_diameter.get("mean_anomaly").toString();
                    mean_motion = temp_diameter.get("mean_motion").toString();
                    equinox = temp_diameter.get("equinox").toString();

                    temp_diameter = temp_diameter.getJSONObject("orbit_class");
                    orbit_class_type = temp_diameter.get("orbit_class_type").toString();
                    orbit_class_description = temp_diameter.get("orbit_class_description").toString();
                    orbit_class_range = temp_diameter.get("orbit_class_range").toString();
                    is_sentry_object = c.get("is_sentry_object").toString();
                    is_potentially_hazardous_asteroid = c.get("is_potentially_hazardous_asteroid").toString();



                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("name", "Asteroid Name: "+name);
                    contact.put("absolute_magnitude_h", "absolute_magnitude_h: "+absolute_magnitude_h);
                    contact.put("diameter","Estimated Diameter: "+diameter);
                    contact.put("date","Close Approach date and time: "+date);
                    contact.put("velocity","velocity: "+ velocity);
                    contact.put("distance","Distance to Earth: "+distance);
                    contact.put("epoch_date_close_approach","epoch_date_close_approach: "+epoch_date_close_approach);
                    contact.put("orbiting_body","orbiting_body: "+orbiting_body);
                    contact.put("orbit_id","orbit_id: "+orbit_id);
                    contact.put("orbit_determination_date","orbit_determination_date: "+orbit_determination_date);
                    contact.put("first_observation_date","first_observation_date: "+first_observation_date);
                    contact.put("last_observation_date","last_observation_date: "+last_observation_date);
                    contact.put("data_arc_in_days","data_arc_in_days: "+data_arc_in_days);
                    contact.put("observations_used","observations_used: "+observations_used);
                    contact.put("orbit_uncertainty","orbit_uncertainty: "+orbit_uncertainty);
                    contact.put("minimum_orbit_intersection","minimum_orbit_intersection: "+minimum_orbit_intersection);
                    contact.put("jupiter_tisserand_invariant","jupiter_tisserand_invariant: "+jupiter_tisserand_invariant);
                    contact.put("epoch_osculation","epoch_osculation: "+epoch_osculation);
                    contact.put("eccentricity","eccentricity: "+eccentricity);
                    contact.put("semi_major_axis","semi_major_axis: "+semi_major_axis);
                    contact.put("inclination","inclination: "+inclination);
                    contact.put("ascending_node_longitude","ascending_node_longitude: "+ascending_node_longitude);
                    contact.put("orbital_period","orbital_period: "+orbital_period);
                    contact.put("perihelion_distance","perihelion_distance: "+perihelion_distance);
                    contact.put("perihelion_argument","perihelion_argument: "+perihelion_argument);
                    contact.put("aphelion_distance","aphelion_distance: "+aphelion_distance);
                    contact.put("perihelion_time","perihelion_time: "+perihelion_time);
                    contact.put("mean_anomaly","mean_anomaly: "+mean_anomaly);
                    contact.put("mean_motion","mean_motion: "+mean_motion);
                    contact.put("equinox","equinox: "+equinox);
                    contact.put("orbit_class_type","orbit_class_type: "+orbit_class_type);
                    contact.put("orbit_class_description","orbit_class_description: "+orbit_class_description);
                    contact.put("orbit_class_range","orbit_class_range: "+orbit_class_range);
                    contact.put("is_sentry_object","is_sentry_object: "+is_sentry_object);
                    contact.put("click","Click here to see the orbit");
                    if(is_potentially_hazardous_asteroid=="true")
                    {
                        contact.put("report","Report: This asteroid could be dangerous to planet Earth!" );
                    }
                    else {
                        contact.put("report","Report: This asteroid poses no threat to planet Earth!" );
                    }
                   // contact.put("report","is_potentially_hazardous_asteroid: "+is_potentially_hazardous_asteroid);
                    contactList.add(contact);

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
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.list_item, new String[]{ "name","absolute_magnitude_h","diameter","date","velocity","distance","epoch_date_close_approach",
            "orbiting_body","orbit_id","orbit_determination_date","first_observation_date","last_observation_date","data_arc_in_days",
            "observations_used","orbit_uncertainty","minimum_orbit_intersection","jupiter_tisserand_invariant","epoch_osculation","eccentricity",
            "semi_major_axis","inclination","ascending_node_longitude","orbital_period","perihelion_distance","perihelion_argument","aphelion_distance",
            "perihelion_time","mean_anomaly","mean_motion","equinox","orbit_class_type","orbit_class_description","orbit_class_range","is_sentry_object","report","click"},
                    new int[]{R.id.name,R.id.absolute_magnitude_h,R.id.diameter,R.id.date, R.id.velocity,R.id.distance,R.id.epoch_date_close_approach,
                    R.id.orbiting_body,R.id.orbit_id,R.id.orbit_determination_date,R.id.first_observation_date,R.id.last_observation_date,R.id.data_arc_in_days,
                    R.id.observations_used,R.id.orbit_uncertainty,R.id.minimum_orbit_intersection,R.id.jupiter_tisserand_invariant,R.id.epoch_osculation,R.id.eccentricity,
                   R.id.semi_major_axis,R.id.inclination,R.id.ascending_node_longitude,R.id.orbital_period,R.id.perihelion_distance,R.id.perihelion_argument,R.id.aphelion_distance,
                    R.id.perihelion_time,R.id.mean_anomaly,R.id.mean_motion,R.id.equinox,R.id.orbit_class_type,R.id.orbit_class_description,R.id.orbit_class_range,
                    R.id.is_sentry_object,R.id.report,R.id.click});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(MainActivity.this,orbit.class);
                    intent.putExtra("neo_name", name);
                    startActivity(intent);

                }
            });
        }
    }


}
