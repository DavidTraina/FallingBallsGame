package traina.david.fallingballsgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        final Button mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToStart();
            }
        });

        final Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame();
            }
        });
    }

    private void switchToStart() {
        Intent tmp = new Intent(this, StartingActivity.class);
        startActivity(tmp);
        finish();
    }

    private void display() {

    }

    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        startActivity(tmp);
        finish();
    }
}
