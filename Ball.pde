public class Ball implements Shape {

  private final float RADIUS_LIMIT = 100;
  private final float DEF_RADIUS = 20;
  private float radius = DEF_RADIUS;

  public Ball() {
 
  }

  public Ball(float r) {
    setRadius(r);
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float r) {
    radius = (0 < r && r < RADIUS_LIMIT) ? r : radius;
  }

  public void draw(PVector pos) {
    drawSphere(pos,radius);
  }
}