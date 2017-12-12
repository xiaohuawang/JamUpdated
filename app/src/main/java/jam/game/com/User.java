package jam.game.com;

public class User {
  private String username;
  private Score score;

  public User() {
    // Default constructor required for calls to DataSnapshot.getValue(User.class)
  }

  public User(String username, Score score) {
    this.username = username;
    this.score = score;
  }

  public String getUsername() {
    return username;
  }

  public Score getScore() {
    return score;
  }
}
