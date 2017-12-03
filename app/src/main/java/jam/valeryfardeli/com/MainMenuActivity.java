package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Main class of the game. This is the class where the app
 * runs first when it is being run.
 */
public class MainMenuActivity extends Activity {
  private ImageView header;
  private Button playButton;
  private Button leaderboardButton;
  private Button signOutButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.activity_main_menu);

    header = (ImageView) findViewById(R.id.header);

    playButton = (Button) findViewById(R.id.play);
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
      }
    });

    leaderboardButton = (Button) findViewById(R.id.leaderboard);
    leaderboardButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, LeaderboardActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    header.setAlpha(0f);
    header.animate().alpha(1.0f).setDuration(2000);
  }
}
