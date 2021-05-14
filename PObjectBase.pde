public abstract class PObjectBase {

  /**
    * Shape
    * Includes drawing method
    * Gives info on the bounds of the shape
    */
  protected Shape shape = null;
  protected PVector objStroke = new PVector(255,255,255);
  protected PVector objFill = new PVector(255,255,255);

  /**
    * State
    * Determines the state of the PObject and what it can do
    */
  protected State state = null;

  // Physics Information
  protected PVector pos;
  protected PVector vel = new PVector(0,0,0);
  protected PVector acc = new PVector(0,0,0);
  protected PVector netForce = new PVector(0,0,0);
  protected float mass = 1.0;




  /** 
    * Disables the stroke feature for this object
    */
  void setNoStroke() {
    objStroke = new PVector(-1,-1,-1);
  }
  
  /**
    * Stores the Black White value for use later
    */
  PVector setStroke(int b_w) {
    objStroke = new PVector(b_w,b_w,b_w);
    return objStroke;
  }
  
  /**
    * Stores the RGB value for use later
    */
  PVector setStroke(int r, int g, int b) {
    objStroke = new PVector(r,g,b);
    return objStroke;
  }

  PVector getStroke() {
    return objStroke;
  }

  /**
    * Disables the fill feature for this object
    */
  void setNoFill() {
    objFill = new PVector(-1,-1,-1);
  }

  /**
    *  Stores the Black White value for use later
    */
  PVector setFill(int b_w) {
    objFill = new PVector(b_w,b_w,b_w);
    return objFill;
  }

  /**
    * Stores the RGB value for use later
    */
  PVector setFill(int r, int g, int b) {
    objFill = new PVector(r,g,b);
    return objFill;
  }

  PVector getFill() {
    return objFill;
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