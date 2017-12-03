package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class GameOverActivity extends Activity {

  Button playAgainButton;
  Button backToMainButton;
  ImageView gameOverView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.activity_game_over);

    playAgainButton = (Button) findViewById(R.id.play_again);
    backToMainButton = (Button) findViewById(R.id.back_to_main);
    gameOverView = (ImageView) findViewById(R.id.game_over);

    playAgainButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
      }
    });

    backToMainButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    gameOverView.setAlpha(0f);
    gameOverView.animate().alpha(1.0f).setDuration(1000);
  }
}
