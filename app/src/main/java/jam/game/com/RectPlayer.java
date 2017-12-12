package jam.game.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {
  public static int RUN_STATE = 0;
  public static int JUMP_UP_STATE = 1;
  public static int JUMP_DOWN_STATE = 2;
  public static int IDLE_STATE = 3;

  private int state = 0;

  private Rect rectangle;
  private int color;

  private Animation idle;
  private Animation run;
  private Animation jumpUp;
  private Animation jumpFall;
  private AnimationManager animationManager;

  /**
   * Default constructor for RectPlayer class.
   * @param rectangle rectangle shape object
   * @param color the color of the rectangle shape
   */
  public RectPlayer(Rect rectangle, int color) {
    this.rectangle = rectangle;
    this.color = color;

    BitmapFactory bitmapFactory = new BitmapFactory();
    Bitmap idle1 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.idle_1);
    Bitmap idle2 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.idle_2);
    Bitmap jumpFallImg = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.jump_fall);
    Bitmap jumpUpImg = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.jump_up);
    Bitmap run1 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_1);
    Bitmap run2 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_2);
    Bitmap run3 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_3);
    Bitmap run4 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_4);
    Bitmap run5 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_5);
    Bitmap run6 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
            R.drawable.run_6);

    idle = new Animation(new Bitmap[]{idle1, idle2}, 0.5f);
    jumpUp = new Animation(new Bitmap[]{jumpUpImg}, 2);
    jumpFall = new Animation(new Bitmap[]{jumpFallImg}, 2);
    run = new Animation(new Bitmap[]{run1, run2, run3, run4, run5, run6}, 0.5f);

    animationManager = new AnimationManager(new Animation[]{run, jumpUp, jumpFall, idle});
  }

  /**
   * Getter for rectangle state.
   * @return rectangle state of this object
   */
  public Rect getRectangle() {
    return rectangle;
  }

  public void setState(int state) {
    this.state = state;
  }

  /**
   * Draw the object on canvas.
   * @param canvas game canvas
   */
  @Override
  public void draw(Canvas canvas) {
    animationManager.draw(canvas, rectangle);
  }

  @Override
  public void update() {
    animationManager.update();
  }

  /**
   * Update the rectangle position of the object.
   * The point should be at the right-bottom of the object.
   * @param point point position of the object
   */
  public void update(Point point) {
    // left, top, right, bottom
    rectangle.set(point.x - rectangle.width(),
            point.y - rectangle.height(),
            point.x,
            point.y);
    animationManager.playAnimation(state);
    animationManager.update();
  }
}
