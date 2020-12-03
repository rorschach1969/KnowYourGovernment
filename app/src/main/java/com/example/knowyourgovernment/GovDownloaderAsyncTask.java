package com.example.knowyourgovernment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GovDownloaderAsyncTask extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private String location;
    private static final String API_KEY = "<YOUR_API_KEY>";
    private static final String civicAPI = "https://www.googleapis.com/civicinfo/v2/representatives";

    private JSONArray govOffices = new JSONArray();
    private ArrayList<Government> govResults;


    public GovDownloaderAsyncTask(MainActivity m) {
        this.mainActivity = m;
        this.govResults = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(String s) {
        parseOfficeJSONResults(s);
        mainActivity.GovDataResult(govResults);
        mainActivity.setLocation(location);
    }

    @Override
    protected String doInBackground(String... strings) {
        String address = strings[0];

        Uri.Builder civicURL = Uri.parse(civicAPI).buildUpon();
        civicURL.appendQueryParameter("key", API_KEY).appendQueryParameter("address", address);
        String urlFinal = civicURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlFinal);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            }
            conn.getErrorStream();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    private void parseOfficeJSONResults(String s){
        try{
            JSONObject jsonOuter = new JSONObject(s);

            JSONArray offices = jsonOuter.getJSONArray("offices");
            JSONArray officials = jsonOuter.getJSONArray("officials");
            JSONObject loc = jsonOuter.getJSONObject("normalizedInput");
            location = loc.getString("city") + ", " + loc.getString("state");
            if (!loc.getString("zip").isEmpty()){
                location = location + ", " + loc.getString("zip");
            }
            for (int i=0; i<offices.length(); i++){
                JSONObject of = offices.getJSONObject(i);
                JSONArray offIndices = of.getJSONArray("officialIndices");
                for (int j = 0; j < offIndices.length(); j++){
                    JSONObject official = officials.getJSONObject(offIndices.getInt(j));
                    String office = of.getString("name");
                    String name = official.getString("name");
                    JSONObject address;
                    String street = "";
                    String city = "";
                    String state = "";
                    String zip = "";
                    if (official.has("address")) {
                        address = official.getJSONArray("address").getJSONObject(0);
                        if (address.has("line1")) {
                            street = address.get("line1").toString();
                        }
                        if (address.has("line2")) {
                            street = street + ", " + address.getString("line2");
                        }
                        if (address.getString("line3").isEmpty()) {
                            street = street + ", " + address.getString("line3");
                        }
                        city = address.getString("city");
                        state = address.getString("state");
                        zip = address.getString("zip");
                    }
                    String party = "Unknown";
                    if (official.has("party")) {
                        party = official.getString("party");
                    }
                    String phone = "";
                    if (official.has("phones")) {
                        phone = official.getJSONArray("phones").getString(0);
                    }

                    String url = "";
                    if (official.has("urls")) {
                        url = official.getJSONArray("urls").getString(0);
                    }
                    String email = "";
                    if (official.has("emails")) {
                        email = official.getJSONArray("emails").getString(0);
                    }
                    String photoURL = "";
                    if (official.has("photoUrl")) {
                        photoURL = official.getString("photoUrl");
                    }
                    JSONArray channels = new JSONArray();
                    if (official.has("channels")){
                        channels = official.getJSONArray("channels");
                    }


                    Government gov = new Government(office, street, city, state, zip, party, phone, url, email, photoURL, name, channels.toString());
                    govResults.add(gov);
                }
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
