package dev.adamag.survivegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Activity_Menu extends AppCompatActivity {
    private static final String TAG = "Activity_Menu";
    private MaterialButton startButton;
    private TextInputEditText userIdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();
    }

    private void initViews() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeServerCall();
            }
        });
    }

    private void findViews() {
        startButton = findViewById(R.id.menu_BTN_start);
        userIdInput = findViewById(R.id.menu_EDT_id);
    }

    private void makeServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = getString(R.string.url);
                String data = getJSON(url);
                Log.d(TAG, "Server data: \"" + data + "\"");
                if (data != null && !data.trim().isEmpty()) {
                    startGame(userIdInput.getText().toString(), data);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_Menu.this, "Failed to get valid data from server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void startGame(String userId, String serverData) {
        int index;
        try {
            if (userId == null) {
                throw new NullPointerException("User ID is null");
            }
            Log.d(TAG, "User ID: " + userId);
            Log.d(TAG, "User ID length: " + userId.length());
            if (userId.length() < 8) {
                throw new IndexOutOfBoundsException("User ID is too short");
            }
            char charAtPosition7 = userId.charAt(7);
            Log.d(TAG, "Character at position 7: " + charAtPosition7);
            index = Character.getNumericValue(charAtPosition7);
            Log.d(TAG, "Derived index: " + index);

            String[] dataArray = serverData.split(",");
            Log.d(TAG, "Data array length: " + dataArray.length);
            Log.d(TAG, "Data array content: " + java.util.Arrays.toString(dataArray));

            if (index < 0 || index >= dataArray.length) {
                throw new NumberFormatException("Index out of bounds");
            }

            String gameState = dataArray[index];
            Intent intent = new Intent(this, Activity_Game.class);
            intent.putExtra(Activity_Game.EXTRA_ID, userId);
            intent.putExtra(Activity_Game.EXTRA_STATE, gameState);
            startActivity(intent);

        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            Log.e(TAG, "Invalid user ID or server data: " + userId, e);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Activity_Menu.this, "Invalid user ID or server data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static String getJSON(String url) {
        StringBuilder data = new StringBuilder();
        HttpsURLConnection con = null;
        try {
            Log.d(TAG, "Connecting to URL: " + url);
            URL urlObj = new URL(url);
            con = (HttpsURLConnection) urlObj.openConnection();
            con.connect();
            int responseCode = con.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned non-OK status: " + responseCode);
                return "";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
            br.close();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        Log.d(TAG, "Received data: \"" + data.toString() + "\"");
        return data.toString();
    }
}
