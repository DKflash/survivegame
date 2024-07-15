package dev.adamag.survivegame;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_Game extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_STATE = "EXTRA_STATE";
    private ImageButton[] directionButtons;
    private int currentLevel = 0;
    private boolean isSuccessful = true;
    private int[] moveSteps = {1, 1, 1, 2, 2, 2, 3, 3, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String userId = getIntent().getStringExtra(EXTRA_ID);
        if (userId != null && userId.length() == moveSteps.length) {
            for (int i = 0; i < moveSteps.length; i++) {
                try {
                    moveSteps[i] = Character.getNumericValue(userId.charAt(i)) % 4;
                } catch (NumberFormatException e) {
                    moveSteps[i] = 0; // Default value in case of error
                    Toast.makeText(this, "Invalid character in user ID", Toast.LENGTH_SHORT).show();
                }
            }
        }

        findViews();
        initViews();
    }

    private void onDirectionClicked(int direction) {
        if (isSuccessful && direction != moveSteps[currentLevel]) {
            isSuccessful = false;
        }
        currentLevel++;
        if (currentLevel >= moveSteps.length) {
            endGame();
        }
    }

    private void endGame() {
        String gameState = getIntent().getStringExtra(EXTRA_STATE);
        if (gameState == null) {
            gameState = "Unknown"; // Default value in case of null
        }
        if (isSuccessful) {
            Toast.makeText(this, "Survived in " + gameState, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You Failed", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void initViews() {
        directionButtons = new ImageButton[]{
                findViewById(R.id.game_BTN_left),
                findViewById(R.id.game_BTN_right),
                findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down)
        };
        for (int i = 0; i < directionButtons.length; i++) {
            final int index = i;
            directionButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDirectionClicked(index);
                }
            });
        }
    }

    private void findViews() {
        directionButtons = new ImageButton[]{
                findViewById(R.id.game_BTN_left),
                findViewById(R.id.game_BTN_right),
                findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down)
        };
    }
}
