package jam.game.com;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
  private List<Scene> scenes;
  public static int ACTIVE_SCENE;

  public SceneManager(GamePanel gamePanel) {
    scenes = new ArrayList<>();
    ACTIVE_SCENE = 0;
    scenes.add(new GameplayScene(gamePanel));
  }

  public void update() {
    scenes.get(ACTIVE_SCENE).update();
  }

  public void update(int jumpState) {
    scenes.get(ACTIVE_SCENE).update(jumpState);
  }

  public void draw(Canvas canvas) {
    scenes.get(ACTIVE_SCENE).draw(canvas);
  }
}
