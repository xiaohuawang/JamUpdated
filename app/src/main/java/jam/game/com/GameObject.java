package jam.game.com;


import android.graphics.Canvas;

/**
 * Interface for every object that is going to be drawn
 * in the game panel.
 */
public interface GameObject {
  // draw the object in the canvas
  void draw(Canvas canvas);

  // update the object states
  void update();
}
