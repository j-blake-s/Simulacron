public class Clock {

  private int t = 0;
  private boolean on = true;

  public Clock() {
    on = true;
  }
  public Clock(boolean is_on) {
    on = is_on;
  }
  
  // Updates the time
  void tick() {
    if (on)
      t += 1;
  }

  // Returns the time
  int time() {
    return t;
  }

  // Starts the clock
  void start() {
    on = true;
  }

  // Stops the clock
  int stop() {
    on = false;
    return t;
  }

  // Resets the time on the watch
  void reset() {
    t = 0;
  }
}