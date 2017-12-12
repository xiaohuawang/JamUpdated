package jam.game.com;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
  private Bitmap[] frames;
  private int frameIndex;

  private boolean isPlaying = false;

  private float frameTime;

  private long lastFrame;

  public Animation(Bitmap[] frames, float animationTime) {
    this.frames = frames;
    frameIndex = 0;

    frameTime = animationTime / frames.length;

    lastFrame = System.currentTimeMillis();
  }

  public void draw(Canvas canvas, Rect destination) {
    if (!isPlaying) {
      return;
    }

//    scaleRect(destination);
    canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
  }

//  private void scaleRect(Rect rect) {
//    float whRatio = (float) (frames[frameIndex].getWidth()) / frames[frameIndex].getHeight();
//    if (rect.width() > rect.height()) {
//      rect.left = rect.right - (int) (rect.height() * whRatio);
//    } else {
//      rect.top = rect.bottom - (int) (rect.width() * (1 / whRatio));
//    }
//  }

  public void update() {
    if (!isPlaying) {
      return;
    }
    // frameTime will be in second
    if ((System.currentTimeMillis() - lastFrame) > frameTime * 1000) {
      frameIndex++;
      frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
      lastFrame = System.currentTimeMillis();
    }
  }

  /**
   * Check if the animation is currently playing
   * or not.
   * @return isPlaying boolean.
   */
  public boolean isPlaying() {
    return isPlaying;
  }

  /**
   * Play the animation.
   */
  public void play() {
    isPlaying = true;
    frameIndex = 0;
    lastFrame = System.currentTimeMillis();
  }

  /**
   * Stop the animation.
   */
  public void stop() {
    isPlaying = false;
  }
}
