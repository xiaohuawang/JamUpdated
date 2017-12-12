package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Main class of the game. This is the class where the app
 * runs first when it is being run.
 */
public class MainMenuActivity extends Activity {
  private FirebaseAuth auth;
  private FirebaseUser currentUser;

  private ImageView header;
  private Button playButton;
  private Button instructionButton;
  private Button leaderboardButton;
  private Button signOutButton;
  private TextView userInfo;

  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
    if (currentUser != null) {
      username = currentUser.getUid();
    } else {
      System.exit(1);
    }
    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.activity_main_menu);

    header = (ImageView) findViewById(R.id.header);
    userInfo = (TextView) findViewById(R.id.user_info);
    userInfo.setText("Welcome " + currentUser.getEmail());

    playButton = (Button) findViewById(R.id.play);
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
      }
    });

    instructionButton = (Button) findViewById(R.id.instruction_button);
    instructionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, PlayInstructionActivity.class);
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

    signOutButton = (Button) findViewById(R.id.sign_out);
    signOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    header.setAlpha(0f);
    header.animate().alpha(1.0f).setDuration(2000);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    auth.signOut();
  }
}
