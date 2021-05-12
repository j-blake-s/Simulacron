public abstract class PObjectBase {

  /**
    * Shape
    * Includes drawing method
    * Gives info on the bounds of the shape
    */
  protected Shape shape;
  protected PVector stroke;
  protected PVector fill;

  /**
    * State
    * Determines the state of the PObject and what it can do
    */
  protected State state;

  // Physics Information
  protected PVector pos;
  protected PVector vel;
  protected PVector acc;
  protected PVector netForce;
  protected float mass = 1.0;

  /** 
    * Disables the stroke feature for this object
    */
  void setNoStroke() {
    stroke = new PVector(-1,-1,-1);
  }
  
  /**
    * Stores the Black White value for use later
    */
  PVector setStroke(int b_w) {
    stroke = new PVector(b_w,b_w,b_w);
    return stroke;
  }
  
  /**
    * Stores the RGB value for use later
    */
  PVector setStroke(int r, int g, int b) {
    stroke = new PVector(r,g,b);
    return stroke;
  }

  PVector getStroke() {
    return stroke;
  }

  /**
    * Disables the fill feature for this object
    */
  void setNoFill() {
    fill = new PVector(-1,-1,-1);
  }

  /**
    *  Stores the Black White value for use later
    */
  PVector setFill(int b_w) {
    fill = new PVector(b_w,b_w,b_w);
    return fill;
  }

  /**
    * Stores the RGB value for use later
    */
  PVector setFill(int r, int g, int b) {
    fill = new PVector(r,g,b);
    return fill;
  }

  PVector getFill() {
    return this.fill;
  }

  /**
    * Position
    */
  public PVector getPos() {
    return this.pos;
  }
  public PVector setPos(PVector vector) {
    this.pos = vector;
    return this.pos;
  }

  /**
    * Velocity
    */
  public PVector getVel() {
    return this.vel;
  }
  public PVector setVel(PVector vector) {
    this.vel = vector;
    return this.vel;
  }
  
  /**
    * Acceleration
    */
  public PVector getAcc() {
    return this.acc;
  }
  public PVector setAcc(PVector vector) {
    this.acc = vector;
    return this.acc;
  }
  
  /**
    * Mass
    */
  public float getMass() {
    return this.mass;
  }
  public float setMass(float f) {
    this.mass = f;
    return this.mass;
  }
}