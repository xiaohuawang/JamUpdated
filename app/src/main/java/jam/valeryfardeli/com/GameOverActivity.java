package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOverActivity extends Activity {
  private static final String LEADERBOARD_REF = "leaderboard";
  private static final String USERS_REF = "users";

  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
  DatabaseReference mLeaderboardRef = mRootRef.child(LEADERBOARD_REF);
  DatabaseReference mUserRef = mRootRef.child(USERS_REF);

  private Button playAgainButton;
  private Button backToMainButton;
  private ImageView gameOverView;
  private TextView scoreView;
  private int totalScore;

  private FirebaseAuth auth;
  private FirebaseUser currentUser;

  private String uid;
  private List<Score> leaderboard;
  private User user;
  Score userCurrentLeaderboardScore;

  /**
   * Initialize Firebase, and UI components.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.activity_game_over);

    Intent intent = getIntent();
    totalScore = intent.getIntExtra("score", 0);

    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();

    if (currentUser != null) {
      uid = currentUser.getUid();
    } else {
      System.exit(1);
    }

    playAgainButton = (Button) findViewById(R.id.play_again);
    backToMainButton = (Button) findViewById(R.id.back_to_main);
    gameOverView = (ImageView) findViewById(R.id.game_over);
    scoreView = (TextView) findViewById(R.id.score_value);

    userCurrentLeaderboardScore = new Score(uid, currentUser.getEmail(), totalScore);

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

  /**
   * Update the firebase database based on the user current score.
   * If user current score can reach the leaderboard, update the leaderboard.
   */
  @Override
  protected void onStart() {
    super.onStart();

    mLeaderboardRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Map<String, Score> ls = new HashMap<>();
        for (DataSnapshot leaderboardScoreSnapshot : dataSnapshot.getChildren()) {
          Score board = leaderboardScoreSnapshot.getValue(Score.class);
          ls.put(leaderboardScoreSnapshot.getKey(), board);
        }

        leaderboard = new ArrayList<>(ls.values());
        Collections.sort(leaderboard, Score.FinalScoreComparator);
        updateLeaderboard();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    mUserRef.child(uid).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);
        updateUser();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    gameOverView.setAlpha(0f);
    gameOverView.animate().alpha(1.0f).setDuration(1000);
  }

  /**
   * Update the leaderboard table if current user score reach
   * the top ten score in leaderboard table.
   */
  private void updateLeaderboard() {
    if (leaderboard.size() > 0) {
      if (leaderboard.get(leaderboard.size() - 1).getFinalGameScore()
              < totalScore || leaderboard.size() < 10) {
        int index = leaderboard.size();
        for (Score board : leaderboard) {
          if (board.getUsername().equals(uid)
                  && board.getFinalGameScore() < totalScore) {
            index = leaderboard.indexOf(board);
          } else if (board.getUsername().equals(uid)
                  && board.getFinalGameScore() >= totalScore) {
            index = -1;
          }
        }
        if (leaderboard.size() == 10 && index >= 0) {
          if (index == 10) {
            index = 9;
          }
          leaderboard.remove(index);
          leaderboard.add(index, userCurrentLeaderboardScore);
          Collections.sort(leaderboard, Score.FinalScoreComparator);
          mLeaderboardRef.setValue(leaderboard);
        } else if (leaderboard.size() < 10 && index == leaderboard.size()) {
          leaderboard.add(userCurrentLeaderboardScore);
          Collections.sort(leaderboard, Score.FinalScoreComparator);
          mLeaderboardRef.setValue(leaderboard);
        } else if (leaderboard.size() < 10 && index >= 0) {
          leaderboard.remove(index);
          leaderboard.add(index, userCurrentLeaderboardScore);
          Collections.sort(leaderboard, Score.FinalScoreComparator);
          mLeaderboardRef.setValue(leaderboard);
        }
      }
    } else {
      leaderboard.add(userCurrentLeaderboardScore);
      mLeaderboardRef.setValue(leaderboard);
    }
    // set the score display view
    scoreView.setText(String.valueOf(totalScore));
  }

  /**
   * Update the user high score if current score is
   * higher than the previous score in firebase.
   */
  private void updateUser() {
    if (totalScore > user.getScore().getFinalGameScore()) {
      user = new User(uid, userCurrentLeaderboardScore);
      mUserRef.child(uid).setValue(user);
    }
  }
}
