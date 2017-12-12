package jam.valeryfardeli.com;

import java.util.Comparator;

/**
 * Created by valeryfardeli on 12/5/17.
 */

public class Score {
  private String username;
  private String email;
  private long finalGameScore;

  public Score() {
    // default constructor is needed
  }

  public Score(String username, String email, long finalGameScore) {
    this.username = username;
    this.email = email;
    this.finalGameScore = finalGameScore;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public long getFinalGameScore() {
    return finalGameScore;
  }

  public static Comparator<Score> FinalScoreComparator = new Comparator<Score>() {

    public int compare(Score s1, Score s2) {

      int score1 = (int) s1.getFinalGameScore();
      int score2 = (int) s2.getFinalGameScore();

      return score2 - score1;
    }
  };
}
