# Activity_Menu.java Changes

This document outlines the changes made to `Activity_Menu.java` and the reasons behind each change.

## Code Changes

1. **Class and Member Variables Renaming**
    - **Changed**: Renamed `menu_BTN_start` to `startButton` and `menu_EDT_id` to `userIdInput`.
    - **Reason**: Improved readability and consistency in naming conventions.

    ```java
    // Original
    private MaterialButton menu_BTN_start;
    private TextInputEditText menu_EDT_id;

    // Updated
    private MaterialButton startButton;
    private TextInputEditText userIdInput;
    ```

2. **Enhanced Logging**
    - **Changed**: Added detailed logging in various methods.
    - **Reason**: To provide more insights during debugging and ensure that the application flow can be traced easily.

    ```java
    // Original
    Log.d("pttt", data);

    // Updated
    Log.d(TAG, "Server data: \"" + data + "\"");
    Log.d(TAG, "User ID: " + userId);
    Log.d(TAG, "User ID length: " + userId.length());
    Log.d(TAG, "Character at position 7: " + charAtPosition7);
    Log.d(TAG, "Derived index: " + index);
    Log.d(TAG, "Data array length: " + dataArray.length);
    Log.d(TAG, "Data array content: " + java.util.Arrays.toString(dataArray));
    ```

3. **Improved Error Handling and Logging in `getJSON` Method**
    - **Changed**: Enhanced error handling in `getJSON` method to log the URL, response code, and received data.
    - **Reason**: To handle server errors gracefully and provide detailed logs for debugging purposes.

    ```java
    // Original
    try {
        HttpsURLConnection con2 = (HttpsURLConnection) new URL(url).openConnection();
        con2.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = br.readLine();
            if (readLine == null) {
                break;
            }
            sb.append(line + "\n");
        }
        br.close();
        data = sb.toString();
    } catch (MalformedURLException ex2) {
        ex2.printStackTrace();
    } catch (IOException ex3) {
        ex3.printStackTrace();
    }

    // Updated
    Log.d(TAG, "Connecting to URL: " + url);
    HttpsURLConnection con = null;
    try {
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
    ```

4. **User Feedback on Server Data Failure**
    - **Changed**: Added a check to see if the server data is empty or null and provide a toast message to the user if the data is invalid.
    - **Reason**: To ensure that the user is informed about the failure to retrieve valid data from the server.

    ```java
    // Original
    if (data != null) {
        Activity_Menu activity_Menu = Activity_Menu.this;
        activity_Menu.startGame(activity_Menu.menu_EDT_id.getText().toString(), data);
    }

    // Updated
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
    ```

5. **Improved Code Structure and Readability**
    - **Changed**: Reorganized and refactored methods to improve readability and maintainability.
    - **Reason**: To make the code more readable and maintainable by following best practices in coding.

    ```java








## Code Analysis and Discoveries

1. **Determining Button Clicks**
    - **Approach**: To determine which button corresponds to which direction, we tested with the following IDs:
        - `000000000`
        - `111111111`
        - `222222222`
        - `333333333`
    - **Findings**:
        - `0` corresponds to the left button.
        - `1` corresponds to the right button.
        - `2` corresponds to the up button.
        - `3` corresponds to the down button.

2. **Analyzing strings.xml and URL Content**
    - **Approach**: We checked the `strings.xml` file and ran the URL provided in the code to understand its content.
    - **Findings**: The URL contains names of countries. This information is used to map IDs to specific countries.

3. **Mapping User ID to Country**

    - **ID for Analysis**: `32406xxxx`
        - Steps to determine the country:
            1. Take the character at position 7 of the ID.
            2. Convert the character to an integer.
            3. Use this integer to index into the data array obtained from the URL to get the corresponding country.
