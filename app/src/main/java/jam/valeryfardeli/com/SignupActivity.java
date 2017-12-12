package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class SignupActivity extends Activity {
  private static final String LEADERBOARD_REF = "leaderboard";
  private static final String USERS_REF = "users";

  private FirebaseAuth auth;
  private EditText signupInputEmail;
  private EditText signupInputPassword;
  private TextView error;
  private Button btnSignUp;
  private Button btnLinkToLogIn;
  private ProgressBar progressBar;

  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
  DatabaseReference mLeaderboardRef = mRootRef.child(LEADERBOARD_REF);
  DatabaseReference mUserRef = mRootRef.child(USERS_REF);

  List<Score> leaderboard;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_signup);

    auth = FirebaseAuth.getInstance();

    signupInputEmail = (EditText) findViewById(R.id.email_input);
    signupInputPassword = (EditText) findViewById(R.id.password_input);
    error = (TextView) findViewById(R.id.error_message);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    btnSignUp = (Button) findViewById(R.id.signup_button);
    btnLinkToLogIn = (Button) findViewById(R.id.go_to_login_button);

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitForm();
      }
    });

    btnLinkToLogIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }

  /**
   * Submit the entried of the email and password input texts, and
   * create the appropriate User into the Firebase.
   */
  private void submitForm() {
    final String email = signupInputEmail.getText().toString().trim();
    String password = signupInputPassword.getText().toString().trim();
    if (!checkEmail()) {
      return;
    }
    if (!checkPassword()) {
      return;
    }
    error.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);

    auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  FirebaseUser currentUser = auth.getCurrentUser();
                  progressBar.setVisibility(View.GONE);
//                  String username = email.substring(0, email.lastIndexOf("@"));
                  String username = currentUser.getUid();
                  Score score = new Score(username, currentUser.getEmail(), 0);

                  User user = new User(username, score);
                  mUserRef.child(username).setValue(user);

                  startActivity(new Intent(SignupActivity.this, MainMenuActivity.class));
                } else {
                  progressBar.setVisibility(View.GONE);
                  error.setVisibility(View.VISIBLE);
                  error.setText("Email has been used, please choose another email");
                }
              }
            });
  }

  /**
   * Check the email input text format.
   *
   * @return true if user inputs in the correct format,
   * false otherwise.
   */
  private boolean checkEmail() {
    String email = signupInputEmail.getText().toString().trim();
    if (email.isEmpty() || !isEmailValid(email)) {
      error.setVisibility(View.VISIBLE);
      error.setText(R.string.err_msg_email);
      return false;
    }
    error.setVisibility(View.INVISIBLE);
    return true;
  }

  /**
   * Check the password input text format.
   *
   * @return true if user inputs in the correct format,
   * false otherwise.
   */
  private boolean checkPassword() {
    String password = signupInputPassword.getText().toString().trim();
    if (password.isEmpty() || !isPasswordValid(password)) {
      error.setVisibility(View.VISIBLE);
      error.setText(R.string.err_msg_password);
      return false;
    }
    error.setVisibility(View.INVISIBLE);
    return true;
  }

  private boolean isEmailValid(String email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  private boolean isPasswordValid(String password) {
    return (password.length() >= 6);
  }

  /**
   * Set the condition of the progress bar when the lifecycle
   * is on Resume.
   */
  @Override
  protected void onResume() {
    super.onResume();
    progressBar.setVisibility(View.GONE);
  }

//  /**
//   * Update the leaderboard score if the leaderboard currently is below
//   * 10 by adding the new created User score (score by default is 0).
//   */
//  @Override
//  protected void onStart() {
//    super.onStart();
//    mLeaderboardRef.addValueEventListener(new ValueEventListener() {
//      @Override
//      public void onDataChange(DataSnapshot dataSnapshot) {
//        Map<String, Score> ls = new HashMap<>();
//        for (DataSnapshot leaderboardScoreSnapshot : dataSnapshot.getChildren()) {
//          Score board = leaderboardScoreSnapshot.getValue(Score.class);
//          ls.put(leaderboardScoreSnapshot.getKey(), board);
//        }
//
//        leaderboard = new ArrayList<>(ls.values());
//        Collections.sort(leaderboard, Score.FinalScoreComparator);
//      }
//
//      @Override
//      public void onCancelled(DatabaseError databaseError) {
//
//      }
//    });
//  }
}
