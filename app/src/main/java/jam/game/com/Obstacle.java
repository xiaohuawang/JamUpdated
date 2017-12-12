package jam.game.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Obstacle object in the game.
 */
public class Obstacle implements GameObject {
  private Rect rectangle;
  private int color;

  private Animation building;
  private AnimationManager animationManager;

  /**
   * Default constructor for obstacle.
   * The left side of the obstacle rectangle is
   * on startX point.
   * @param rectHeight obstacle's height
   * @param rectWidth obstacle's width
   * @param color color of obstacle
   * @param startX left side of the obstacle
   */
  public Obstacle(int rectHeight, int rectWidth, int color, int startX) {
    this.rectangle = new Rect(startX,
            Constants.SCREEN_HEIGHT - rectHeight + (int) (10 * Constants.LOGICAL_DENSITY),
            startX + rectWidth,
            Constants.SCREEN_HEIGHT + (int) (10 * Constants.LOGICAL_DENSITY));
    this.color = color;

    BitmapFactory bitmapFactory = new BitmapFactory();
    Bitmap buildingImg = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.building);

    building = new Animation(new Bitmap[]{buildingImg}, 2);
    animationManager = new AnimationManager(new Animation[]{building});
  }

  /**
   * Get the obstacle's rectangle.
   * @return obstacle's rectangle
   */
  public Rect getRectangle() {
    return rectangle;
  }

  /**
   * Move the obstacle to the left of the screen.
   * @param x moving distance of the obstacle to the
   *          left of the screen
   */
  public void decrementX(float x) {
    rectangle.left -= x;
    rectangle.right -= x;
  }

  public boolean playerCollide(RectPlayer player) {
    if (rectangle.contains(player.getRectangle().right,
            player.getRectangle().bottom - (int) (11 * Constants.LOGICAL_DENSITY))) {
      return true;
    }
    return false;
  }

  @Override
  public void draw(Canvas canvas) {
    animationManager.playAnimation(0);
    animationManager.update();
    animationManager.draw(canvas, rectangle);
  }

  @Override
  public void update() {
  }
}
