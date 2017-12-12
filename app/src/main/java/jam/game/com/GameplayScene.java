package jam.game.com;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class GameplayScene implements Scene {
  private static final int JUMP_VELOCITY_LOW = (int) (-13 * Constants.LOGICAL_DENSITY);
  private static final int JUMP_VELOCITY_HIGH = (int) (-20 * Constants.LOGICAL_DENSITY);
  private static final int LEFT_RECTANGLE = (int) (50 * Constants.LOGICAL_DENSITY);
  private static final int TOP_RECTANGLE = (Constants.SCREEN_HEIGHT / 2) - (int) (55 * Constants.LOGICAL_DENSITY);
  private static final int RIGHT_RECTANGLE = (int) (82 * Constants.LOGICAL_DENSITY);
  private static final int BOTTOM_RECTANGLE = Constants.SCREEN_HEIGHT / 2;
  private static final int OBSTACLE_GAP = (int) (85 * Constants.LOGICAL_DENSITY);
  private static final int GAME_OVER_TIME = 4000; // in millis
  private static final int NO_VELOCITY = 0;
  private static final int MAX_VELOCITY = (int) (10 * Constants.LOGICAL_DENSITY);
  private static final int GRAVITY = (int) (1 * Constants.LOGICAL_DENSITY);
  private static final int MAX_TOTAL_JUMP = 1;
  private static final int STAGE_2_SCORE = 10;
  private static final int STAGE_3_SCORE = 20;

  private static int count;
  private static int jumpLevel;

  private float screenSpeed;
  private RectPlayer player;
  private Point playerPoint;
  private ObstacleManager obstacleManager;
  private boolean gameOver;
  private long gameOverTime;
  private int velocity;
  private int totalJumps;
  private int score;
  private boolean gotScore;
  private boolean playFalllingSound;

  private Bitmap backgroundBitmap;
  private Paint backgroundPaint;
  private GamePanel gamePanel;

  public GameplayScene(GamePanel gamePanel) {
    this.gamePanel = gamePanel;

    playFalllingSound = false;
    screenSpeed = 5000.0f;

    // set initial height jump velocity
    velocity = 0;

    // set initial total jumps
    totalJumps = 0;

    // set inital gameOver
    gameOver = false;

    jumpLevel = 0;
    count = 0;

    // set initial score
    score = 0;
    gotScore = true;
    Rect playerRectangle = new Rect(LEFT_RECTANGLE, TOP_RECTANGLE,
            RIGHT_RECTANGLE, BOTTOM_RECTANGLE);
    player = new RectPlayer(playerRectangle, Color.BLUE);
    playerPoint = new Point(RIGHT_RECTANGLE, BOTTOM_RECTANGLE);

    obstacleManager = new ObstacleManager(OBSTACLE_GAP, Color.BLACK);

    // set up the background bitmap
    Resources res = Constants.CURRENT_CONTEXT.getResources();
    Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.game_background);
    backgroundBitmap = Bitmap.createScaledBitmap(bitmap, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
    backgroundPaint = new Paint();
  }

  /**
   * Case not game over: update obstacles and player positions.
   * Case game over: stop updating obstacles, and make the player fall.
   */
  @Override
  public void update() {
    if (!gameOver) {
      int currentObstacleHeight;
      if (obstacleManager.getCurrentObstacle().getRectangle().left > playerPoint.x) {
        currentObstacleHeight = Constants.SCREEN_HEIGHT;
      } else {
        currentObstacleHeight = obstacleManager.getCurrentObstacle().getRectangle().top;
      }
      if (velocity < NO_VELOCITY || (playerPoint.y <= Constants.SCREEN_HEIGHT
              && playerPoint.y < currentObstacleHeight)) {
        if (velocity >= MAX_VELOCITY) {
          velocity = MAX_VELOCITY;
        } else {
          velocity = velocity + GRAVITY;
        }

        if (velocity < 0) {
          player.setState(RectPlayer.JUMP_UP_STATE);
        } else if (velocity > 0) {
          player.setState(RectPlayer.JUMP_DOWN_STATE);
        }

      } else {
        totalJumps = 0;
        velocity = NO_VELOCITY;
        player.setState(RectPlayer.RUN_STATE);
      }
      int pointX = playerPoint.x;
      int pointY = playerPoint.y + velocity;
      playerPoint.set(pointX, pointY);

      player.update(playerPoint);
      if (score == STAGE_2_SCORE) {
        screenSpeed = 3500.0f;
      }

      if (score == STAGE_3_SCORE) {
        screenSpeed = 4000.0f;
      }
      obstacleManager.update(screenSpeed);

      // update the score
      if (!gotScore && obstacleManager.getCurrentObstacle().getRectangle().left
              < player.getRectangle().right - (int) (3 * Constants.LOGICAL_DENSITY)) {
        gotScore = true;
        score++;
      }
      if (gotScore && obstacleManager.getCurrentObstacle().getRectangle().left >= player.getRectangle().right) {
        gotScore = false;
      }

      if (obstacleManager.getCurrentObstacle().playerCollide(player)) {
        gameOver = true;
        gameOverTime = System.currentTimeMillis();
      }

    } else {
      if (System.currentTimeMillis() - gameOverTime < GAME_OVER_TIME) {
        if (!playFalllingSound) {
          Constants.FALLING_MEDIA_PLAYER.start();
        }
        player.setState(RectPlayer.IDLE_STATE);
        if (playerPoint.y >= Constants.SCREEN_HEIGHT + (int) (100 * Constants.LOGICAL_DENSITY)) {
          velocity = 0;
        } else {
          velocity = velocity + GRAVITY;
          int pointX = playerPoint.x;
          int pointY = playerPoint.y + velocity;
          playerPoint.set(pointX, pointY);
          player.update(playerPoint);
        }
      } else {
        gamePanel.gameOver(score);
      }
    }
  }

  public void update(int jumpState) {
    if (jumpState != 0 || count != 0) {
      count++;
      if (jumpState > jumpLevel) {
        jumpLevel = jumpState;
      }
      if (count == 4) {
        if (jumpLevel == 1) {
          doJump(JUMP_VELOCITY_LOW);
        } else if (jumpLevel == 2) {
          doJump(JUMP_VELOCITY_HIGH);
        }
        jumpLevel = 0;
        count = 0;
      }
    }
  }

  private void doJump(int jumpLevel) {
    if (!gameOver) {
      if (totalJumps < MAX_TOTAL_JUMP) {
        velocity = jumpLevel;
        totalJumps++;
      }
    }
  }

  /**
   * Draw everything in our game to the canvas (display the game).
   *
   * @param canvas current screen canvas to display our game
   */
  @Override
  public void draw(Canvas canvas) {
    // for now, set the canvas background into a color
    canvas.drawBitmap(backgroundBitmap, 0, 0, backgroundPaint);

    player.draw(canvas);
    obstacleManager.draw(canvas);

    Paint scorePaint = new Paint();
    scorePaint.setTextSize((int) (25 * Constants.LOGICAL_DENSITY));
    scorePaint.setColor(Color.BLACK);
    canvas.drawText(String.valueOf(score),
            (int) (30 * Constants.LOGICAL_DENSITY),
            (int) (45 * Constants.LOGICAL_DENSITY),
            scorePaint);
  }

  @Override
  public void terminate() {
    SceneManager.ACTIVE_SCENE = 0;
  }
}
