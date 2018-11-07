package traina.david.fallingballsgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartingActivity extends AppCompatActivity {
    //TODO: pass this without making static
    /**
     * Indicates how difficult the game will be. 1 <= difficulty <= 3;
     */
    private int difficulty = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        final Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame();
            }
        });
    }

    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        startActivity(tmp);
    }


}
