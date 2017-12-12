package jam.valeryfardeli.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class LoginActivity extends Activity {
  private Button btnLogin;
  private Button btnLinkToSignUp;
  private FirebaseAuth auth;
  private EditText loginInputEmail;
  private EditText loginInputPassword;
  private ProgressBar progressBar;
  private TextView error;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_login);

    // get firebase authentication
    auth = FirebaseAuth.getInstance();

    btnLogin = (Button) findViewById(R.id.login_button);
    btnLinkToSignUp = (Button) findViewById(R.id.go_to_sign_up_button);
    loginInputEmail = (EditText) findViewById(R.id.email_input);
    loginInputPassword = (EditText) findViewById(R.id.password_input);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    error = (TextView) findViewById(R.id.error_message);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitForm();
      }
    });

    btnLinkToSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }

  /**
   * Submit the entried of the email and password input texts, and
   * get the appropriate User from the Firebase.
   */
  private void submitForm() {
    String email = loginInputEmail.getText().toString().trim();
    String password = loginInputPassword.getText().toString().trim();
    if (!checkEmail()) {
      return;
    }
    if (!checkPassword()) {
      return;
    }
    error.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);

    auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  progressBar.setVisibility(View.GONE);
                  startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                } else {
                  progressBar.setVisibility(View.GONE);
                  error.setVisibility(View.VISIBLE);
                  error.setText("Email and Password does not match");
                }
              }
            });
  }

  /**
   * Check the email input text format.
   * @return true if user inputs in the correct format,
   *         false otherwise.
   */
  private boolean checkEmail() {
    String email =  loginInputEmail.getText().toString().trim();
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
   * @return true if user inputs in the correct format,
   *         false otherwise.
   */
  private boolean checkPassword() {
    String password = loginInputPassword.getText().toString().trim();
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

  private boolean isPasswordValid(String password){
    return (password.length() >= 6);
  }

  @Override
  protected void onResume() {
    super.onResume();
    progressBar.setVisibility(View.GONE);
  }
}
