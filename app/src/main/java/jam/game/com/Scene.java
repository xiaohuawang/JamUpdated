package jam.game.com;

import android.graphics.Canvas;

/**
 * Scene interface to manage the code for every
 * Scene.
 */
public interface Scene {
  void update();

  void update(int jumpState);

  void draw(Canvas canvas);

  /**
   * When scene supposed to end.
   */
  void terminate();
}
