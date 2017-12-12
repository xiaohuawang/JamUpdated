package jam.game.com;

import android.graphics.Canvas;
import android.os.Process;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
  public static final int MAX_FPS = 30;
  private double averageFps;
  private SurfaceHolder surfaceHolder;
  private GamePanel gamePanel;
  private boolean running;
  public static Canvas canvas;

  public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
    super();
    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
    this.surfaceHolder = surfaceHolder;
    this.gamePanel = gamePanel;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  @Override
  public void run() {
    long startTime;
    long timeMillis;
    long waitTime;
    int frameCount = 0;
    long totalTime = 0;
    long targetTime = 1000 / MAX_FPS;

    while (running) {
      startTime = System.nanoTime();
      canvas = null;

      try {
        canvas = this.surfaceHolder.lockCanvas();
        synchronized (surfaceHolder) {
          // move the objects when update is called
          this.gamePanel.update();

          // draw the canvas for the newly updated display
          this.gamePanel.draw(canvas);
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      } finally {
        if (canvas != null) {
          try {
            surfaceHolder.unlockCanvasAndPost(canvas);
          } catch (Exception exception) {
            exception.printStackTrace();
          }
        }
      }

      timeMillis = (System.nanoTime() - startTime) / 1000000;
      waitTime = targetTime - timeMillis;
      try {
        if (waitTime > 0) {
          this.sleep(waitTime);
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
      totalTime += System.nanoTime() - startTime;
      frameCount++;

      if (frameCount == MAX_FPS) {
        averageFps = 1000 / ((totalTime / frameCount) / 1000000);
        frameCount = 0;
        totalTime = 0;
//        System.out.println(averageFps);
      }

//      try {
//        Thread.sleep(20);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    }
  }
}
