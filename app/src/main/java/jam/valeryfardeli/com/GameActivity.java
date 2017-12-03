package jam.valeryfardeli.com;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

public class GameActivity extends Activity {

  /**
   * Initialize:
   * - the display of the app to be full screen
   * - the view, so there is no title bar
   * - constants of SCREEN_HEIGHT and SCREEN_WIDTH.
   *
   * @param savedInstanceState savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the window to full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // initialize the view to be no title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    // get the screen width and height
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    Constants.SCREEN_WIDTH = displayMetrics.widthPixels;
    Constants.SCREEN_HEIGHT = displayMetrics.heightPixels;
    Constants.LOGICAL_DENSITY = displayMetrics.density;

    try {
      prepareBackgroundSong();
    } catch (IOException e) {
      e.printStackTrace();
    }

    requestAudioPermissions();
  }


  //Requesting run-time permissions

  //Create placeholder for user's consent to record_audio permission.
  //This will be used in handling callback
  private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

  private void requestAudioPermissions() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

      //When permission is not granted by user, show them message why this permission is needed.
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.RECORD_AUDIO)) {
        Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

        //Give user option to still opt-in the permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_RECORD_AUDIO);

      } else {
        // Show user dialog to grant permission to record audio
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_RECORD_AUDIO);
      }
    }
    //If permission is granted, then go ahead recording audio
    else if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {

      //Go ahead with recording audio now
      setContentView(new GamePanel(this));
      //recordAudio();
    }
  }

  //Handling callback
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_RECORD_AUDIO: {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted, yay!
          setContentView(new GamePanel(this));
          //recordAudio();
        } else {
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
          Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
        }
        return;
      }
    }
  }

  public void moveToGameOverActivity() {
    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
    startActivity(intent);
    finish();
  }

  /**
   * Prepare the media player to play a background music.
   *
   * @throws IOException when background music is not found
   */
  private void prepareBackgroundSong() throws IOException {
    AssetFileDescriptor afd = getAssets().openFd("falling.wav");
    Constants.FALLING_MEDIA_PLAYER = new MediaPlayer();
    Constants.FALLING_MEDIA_PLAYER.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
    Constants.FALLING_MEDIA_PLAYER.prepare();
  }
}
