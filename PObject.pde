public class PObject extends PObjectBase {


  public PVector addForce(PVector force) {
    netForce.add(force);
    return netForce;
  }

  public void update() {
    // F = ma -> a = F/m
    acc = PVector.div(netForce,mass);
    vel.add(acc);
    pos.add(vel);
    netForce = new PVector(0,0,0);
  }

  public void draw() {

    stroke(this.stroke);
    fill(this.fill);
    this.shape.draw();
  }  
}