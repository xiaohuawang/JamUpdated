package jam.game.com;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * GamePanel class contains the game loop which is
 * rendering the object over and over again until the
 * game is over. I used surface view because I need to
 * update the GUI rapidly for the game loop.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SceneManager manager;
  private MainThread thread;
  private GameActivity gameActivity;

  // for voice
  private int jumpState;
  private Audio thread2;

  public GamePanel(Context context) {
    super(context);
    gameActivity = (GameActivity) context;

    getHolder().addCallback(this);

    Constants.CURRENT_CONTEXT = context;

    thread = new MainThread(getHolder(), this);
    thread2 = new Audio(this);

    manager = new SceneManager(this);

    setFocusable(true);
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    // restart the thread
    thread = new MainThread(getHolder(), this);
    thread2 = new Audio(this);

    Constants.INIT_TIME = System.currentTimeMillis();

    // start our game loop
    thread2.setRunning(true);
    thread.setRunning(true);
    thread2.start();
    thread.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    boolean retry = true;
    while (retry) {
      try {
        thread.setRunning(false);
        thread2.setRunning(false);
        thread.join();
        thread2.join();
      } catch (Exception e) {
        e.printStackTrace();
      }
      retry = false;
    }
  }

  /**
   * Update the game frame by frame.
   */
  public void update() {
    manager.update(jumpState);
    manager.update();
  }

  /**
   * Draw everything in our game to the canvas (display the game).
   * @param canvas current screen canvas to display our game
   */
  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    manager.draw(canvas);
  }

  /**
   * Set the jump state of the character.
   * 1 is jump low, 2 is jump high.
   * @param jumpState
   */
  public void setJumpState(int jumpState) {
    this.jumpState = jumpState;
  }

  /**
   * Move the activity of the caller to the GameOverActivity.
   * @param score final score that the user get.
   */
  public void gameOver(int score) {
    thread.setRunning(false);
    thread2.setRunning(false);
    gameActivity.setScoreIntent(score);
    gameActivity.moveToGameOverActivity();
  }
}
