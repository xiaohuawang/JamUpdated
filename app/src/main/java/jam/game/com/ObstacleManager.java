package jam.game.com;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the placement and creations of the obstacles
 * in the game.
 */
public class ObstacleManager {
  private List<Obstacle> obstacles;
  private int obstacleGap;
  private int color;

  private long startTime;

  public ObstacleManager(int obstacleGap, int color) {
    this.obstacleGap = obstacleGap;
    this.color = color;

    startTime = System.currentTimeMillis();

    obstacles = new ArrayList<>();

    populateObstacles();
  }

  public Obstacle getCurrentObstacle() {
    if (obstacles.get(0).getRectangle().right < (int) (50 * Constants.LOGICAL_DENSITY)) {
      return obstacles.get(1);
    }
    return obstacles.get(0);
  }

  /**
   * Initialize and create the obstacles
   * in their appropriate positions.
   */
  private void populateObstacles() {
    int currentX = 0;
    int firstObstacleWidth = 5 * Constants.SCREEN_WIDTH / 4;
    int firstOBstacleHeight = (Constants.SCREEN_HEIGHT / 2) + (int) (10 * Constants.LOGICAL_DENSITY);
    Obstacle firstObstacle = new Obstacle(firstOBstacleHeight,
            firstObstacleWidth,
            color,
            currentX);
    obstacles.add(firstObstacle);
    currentX += firstObstacleWidth + obstacleGap;
    while (currentX < (3 * Constants.SCREEN_WIDTH)) {
      int obstacleWidth = (int) (((Math.random() * 100) + 50) * Constants.LOGICAL_DENSITY);
      int obstacleHeight = (Constants.SCREEN_HEIGHT / 2) + (int) ((10 +
              ((Math.pow(-1, Math.floor(Math.random() * 2))) * (Math.random() * 100))) * Constants.LOGICAL_DENSITY);
      Obstacle obstacle = new Obstacle(obstacleHeight,
              obstacleWidth,
              color,
              currentX);
      obstacles.add(obstacle);
      currentX += obstacleWidth + obstacleGap;
    }
  }

  public void update(float screenSpeed) {
    if (startTime < Constants.INIT_TIME) {
      startTime = Constants.INIT_TIME;
    }
    int elapsedTime = (int) (System.currentTimeMillis() - startTime);
    startTime = System.currentTimeMillis();

    // from right to left of the screen is 4 seconds
    float speed = Constants.SCREEN_WIDTH / screenSpeed;

    for (Obstacle obstacle : obstacles) {
      obstacle.decrementX(speed * elapsedTime);
    }

    // when the first obstacle is off the screen
    if (obstacles.get(0).getRectangle().right <= 0) {
      int obstacleWidth = (int) (((Math.random() * 100) + 50) * Constants.LOGICAL_DENSITY);
      int obstacleHeight = (Constants.SCREEN_HEIGHT / 2) + (int) ((10 +
              ((Math.pow(-1, Math.floor(Math.random() * 2))) * (Math.random() * 100))) * Constants.LOGICAL_DENSITY);
      Obstacle obstacle = new Obstacle(obstacleHeight,
              obstacleWidth,
              color,
              obstacles.get(obstacles.size() - 1).getRectangle().right + obstacleGap);
      obstacles.add(obstacle);
      obstacles.remove(0);
    }
  }

  public void draw(Canvas canvas) {
    for (Obstacle obstacle : obstacles) {
      obstacle.draw(canvas);
    }
  }
}
