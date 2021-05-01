public class Ball extends Particle {

  private final PVector DEF_POS = new PVector(0,0,0);
  private final PVector DEF_VEL = new PVector(0,0,0);
  private final PVector DEF_ACC = new PVector(0,0,0);
  private final float DEF_MASS = 1;
  private final int DEF_RADIUS = 20;


  private int radius;

  public Ball() {
    this.init(DEF_POS,DEF_VEL,DEF_ACC,DEF_RADIUS,DEF_MASS);
  }
  public Ball(PVector pos_) {
    this.init(pos_,DEF_VEL,DEF_ACC,DEF_RADIUS,DEF_MASS);
  }
  public Ball(PVector pos_,float mass_) {
    this.init(pos_,DEF_VEL,DEF_ACC,DEF_RADIUS,mass_);
  }
  public Ball(PVector pos_,PVector vel_) {
    this.init(pos_,vel_,DEF_ACC,DEF_RADIUS,DEF_MASS);
  }
  public Ball(PVector pos_,PVector vel_,float mass_) {
    this.init(pos_,vel_,DEF_ACC,DEF_RADIUS,mass_);
  }
  public Ball(PVector pos_,PVector vel_,PVector acc_) {
    this.init(pos_,vel_,acc_,DEF_RADIUS,DEF_MASS);
  }
  public Ball(PVector pos_,PVector vel_,PVector acc_,float mass_) {
    this.init(pos_,vel_,acc_,DEF_RADIUS,mass_);
  }

  public Ball(PVector pos_,int radius_) {
    this.init(pos_,DEF_VEL,DEF_ACC,radius_,DEF_MASS);
  }
  public Ball(PVector pos_,int radius_,float mass_) {
    this.init(pos_,DEF_VEL,DEF_ACC,radius_,mass_);
  }
  public Ball(PVector pos_,PVector vel_,int radius_) {
    this.init(pos_,vel_,DEF_ACC,radius_,DEF_MASS);
  }
  public Ball(PVector pos_,PVector vel_,int radius_,float mass_) {
    this.init(pos_,vel_,DEF_ACC,radius_,mass_);
  }
  public Ball(PVector pos_,PVector vel_,PVector acc_,int radius_) {
    this.init(pos_,vel_,acc_,radius_,DEF_MASS);
  }
  public Ball(PVector pos_,PVector vel_,PVector acc_,int radius_,float mass_) {
    this.init(pos_,vel_,acc_,radius_,mass_);
  }



  private void init(PVector pos_,PVector vel_,PVector acc_,int radius_,float mass_) {
    this.pos = pos_;
    this.vel = vel_;
    this.acc = acc_;
    this.mass = mass_;
    this.radius = radius_;
    this.setStroke(255);
    this.setNoFill();
  }


  public void draw() {
    if (isHidden())
      return;

    if (stroke.x < 0)
      noStroke();
    else 
      stroke(stroke.x,stroke.y,stroke.z);
    
    if (fill.x < 0)
      noFill();
    else
      fill(fill.x,fill.y,fill.z);
    

    drawSphere(pos,radius);
    
  }

}