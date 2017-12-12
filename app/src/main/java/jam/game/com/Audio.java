package jam.game.com;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Thread to manage live recording/playback of voice input from the device's microphone.
 */
public class Audio extends Thread {
  private boolean running = true;

  private static final int SAMPLE_DELAY = 0;
  private static final int SAMPLE_RATE = 16000;
  private AudioRecord recorder;
  private double lastLevel = 0;
  private int bufferSize;
  private GamePanel gamePanel;

  /**
   * Give the thread high priority so that it's not canceled unexpectedly, and start it
   */
  public Audio(GamePanel gamePanel) {
    super();
    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    this.gamePanel = gamePanel;
//    start();
  }

  @Override
  public void run() {
//    Log.i("Audio", "Running Audio Thread");
    recorder = null;
//    short[][] buffers = new short[256][160];
//    int ix = 0;

    /*
     * Initialize buffer to hold continuously recorded audio data, start recording, and
     * start playback.
     */
    try {
      bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
      recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
      if (recorder.getState() != AudioRecord.STATE_INITIALIZED) {
        Log.e("tag", "AudioRecord init failed");
        return;
      }
      recorder.startRecording();
      /*
       * Loops until something outside of this thread stops it.
       * Reads the data from the recorder and writes it to the audio track for playback.
       */
      while (running) {
//        Log.i("Map", "Writing new data to buffer");
        try {
          Thread.sleep(SAMPLE_DELAY);
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
        readAudioBuffer();

        if (lastLevel > 30 && lastLevel < 80) {
          gamePanel.setJumpState(1);
        } else if (lastLevel >= 80) {
          gamePanel.setJumpState(2);
        } else {
          gamePanel.setJumpState(0);
        }

      }
    } catch (Throwable x) {
      Log.w("Audio", "Error reading voice audio", x);
    } finally {
      if (!running) {
        recorder.stop();
        recorder.release();
      }
    }
  }

  /**
   * Called from outside of the thread in order to stop the recording/playback loop
   */
  public void setRunning(boolean running) {
    this.running = running;
  }

  private void readAudioBuffer() {
    try {
      short[] buffer = new short[bufferSize];

      int bufferReadResult = 1;

      if (recorder != null) {

        // Sense the voice...
        bufferReadResult = recorder.read(buffer, 0, bufferSize);
        double sumLevel = 0;
        for (int i = 0; i < bufferReadResult; i++) {
          sumLevel += buffer[i];
        }
        lastLevel = Math.abs((sumLevel / bufferReadResult));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
