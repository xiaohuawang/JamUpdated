package jam.game.com;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class LeaderboardActivity extends Activity {
  private static final String LEADERBOARD_REF = "leaderboard";

  ListView leaderboardView;

  ArrayAdapter<String> arrayAdapter;
  FirebaseAuth auth;
  FirebaseUser currentUser;
  String uid;

  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
  DatabaseReference mLeaderboardRef = mRootRef.child(LEADERBOARD_REF);

  ArrayList<String> leaderboardArray;
  List<Score> leaderboard;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_leaderboard);

    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
    uid = currentUser.getUid();

    leaderboardView = (ListView) findViewById(R.id.leaderboard_table);
  }

  /**
   * Get the realtime database for the leaderboard table
   * from fire base.
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
        updateUI();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  /**
   * Update the UI to show the current leaderboard table.
   */
  private void updateUI() {
    leaderboardArray = new ArrayList<>();
    for (Score board : leaderboard) {
      String component = board.getEmail() + ": " + board.getFinalGameScore();
      leaderboardArray.add(component);
    }
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, leaderboardArray);
    leaderboardView.setAdapter(arrayAdapter);
  }
}
