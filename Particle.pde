public abstract class Particle implements Drawable {

  protected PVector pos;
  protected PVector vel;
  protected PVector acc;
  protected float mass;
  
  
  protected boolean hidden;

  protected PVector stroke;
  protected PVector fill;


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

  public void update() {
    vel.add(acc);
    pos.add(vel);
    acc.set(0,0,0);
  }

  public void shadow(PObject obj) {
    shiftTo(obj);
    mimick(obj);
  }
  public void become(PObject obj) {
    shadow(obj);
    this.mass = obj.getMass();
  }
  public void mimick(PObject obj) {
    this.vel = obj.getVel();
    this.acc = obj.getAcc();
  }

  public void shiftTo(PObject obj) {
    this.pos = obj.getPos();
  }

   /**
    * Draws the object to the canvas
    */
  public abstract void draw();

  /**
    * Brings this object out of hiding and allows it to be drawn to the canvas
    */
  public void show() {
    this.hidden = false;
  }

  /**
    * Hides this object by not letting it be drawn to the canvas
    */
  public void hide() {
    this.hidden = true;
  }

  /**
    * Checks whether this object is hidden or not
    */
  public boolean isHidden() {
    return this.hidden;
  }

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
    return this.stroke;
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

  

}