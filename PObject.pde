public class PObject extends PObjectBase {

  private final Shape DEF_SHAPE = new Ball();
  private final PVector DEF_SPAWN = new PVector(0,0,0);

  public PObject() {
    init(null,null);
  }

  public PObject(PVector posi) {
    init(posi,null);
  }

  public PObject(PVector posi, Shape shape) {
    init(posi,shape);
  }


  private void init(PVector posi,Shape shape) {
    this.pos = (posi != null) ? posi : DEF_SPAWN;
    this.shape = (shape != null) ? shape : DEF_SHAPE;
  }

  public PVector applyForce(PVector force) {
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

    applyStroke(objStroke);
    applyFill(objFill);
    shape.draw(this.pos);

  }  
}