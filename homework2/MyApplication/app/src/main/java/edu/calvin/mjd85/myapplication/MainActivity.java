package edu.calvin.mjd85.myapplication;

/*
@author Mark Davis
@date October 21, 2016
Homework 2, CS262 Calvin College
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
    Main Activity:
    -sends request to 336 server
    -creates players from player object received from server
    -displays player content according to user input
 */
public class MainActivity extends AppCompatActivity {

    private EditText playerText;
    private Button fetchButton;

    private List<Player> playerList = new ArrayList<>();
    private ListView itemsListView;

    /* This formatter can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    //private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerText = (EditText) findViewById(R.id.playerText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.playerListView);

        // See comments on this formatter above.
        //numberFormat.setMaximumFractionDigits(0);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerText.getText() == null) {
                    dismissKeyboard(playerText);
                    new GetPlayerTask().execute(createURL());
                }
                else {
                   dismissKeyboard(playerText);
                   new GetPlayerTask().execute(createURL_player(playerText.getText().toString()));
                }
            }
        });
    }

    /**
     * Formats a URL for individual player with given parameter
     *
     * @param id the target city
     * @return URL formatted for player list
     */
    private URL createURL_player(String id) {
        try {
            String urlString = getString(R.string.web_service_one) + URLEncoder.encode(id, "UTF-8");
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * returns URL for player list
     *
     * @return URL formatted for player list
     */
    private URL createURL() {
        try {
            String urlString = getString(R.string.web_service_list);
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for GETing the current weather data from openweathermap.org asynchronously
     */
    private class GetPlayerTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return new JSONObject(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject player) {
            if (player != null) {
                Log.d(TAG, player.toString());
                convertJSONtoArrayList(player);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts the JSON player data to an arraylist suitable for a listview adapter
     *
     * @param player
     */
    private void convertJSONtoArrayList(JSONObject player) {
        playerList.clear(); // clear old weather data
        try {
            JSONArray list = player.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject pla = list.getJSONObject(i);

                int player_id = pla.getInt("id");
                String player_email = pla.getString("emailaddress");
                String player_name = pla.getString("name");

                String playerLineText = player_id + ", " + player_email + " ," + player_name;
                playerList.add(new Player(playerLineText));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the player data on the playerListView through a simple adapter
     */
    private void updateDisplay() {
        if (playerList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Player item : playerList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("player", item.getPlayerText());
            data.add(map);
        }

        int resource = R.layout.activity_main;
        String[] from = {"player"};
        int[] to = {R.id.playerListView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }
}