import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Simulacron extends PApplet {

Environment env;

public void setup() {
  
  
  background(0);
  frameRate(60);
  env = new Environment();
}

public void draw() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
  env.film();
}





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
/**
 *-> Faciliatates use of the camera function.
 *-> Keeps track of position, direction, and orientation.
 *
 * @author: Blake Seekings
 * @DoC:    4/15/2021
*/
class Camera {
  
  private PVector u;
  private PVector e;
  private PVector c;
  private boolean on = true;
  

  Camera() {
    this.u = new PVector(0,-1,0);
    this.e = new PVector(0,0,0);
    this.c = new PVector(0,0,0);
  }

  Camera(PVector eye, PVector center, PVector up) {
    this.u = up;
    this.e = eye;
    this.c = center;
  }
  
  
  // Film
  public boolean film() {
    if (!is_on())
      return false;
    camera(e.x,e.y,e.z,c.x,c.y,c.z,u.x,u.y,u.z);
    return true;
  }
  public boolean film(PVector eye_,PVector center_,PVector up_) {
    if (!is_on())
      return false;
    camera(eye_.x,eye_.y,eye_.z,center_.x,center_.y,center_.z,up_.x,up_.y,up_.z);
    return true;
  }
  

  // On/Off
  public boolean power() {
    on = !on;
    return on;
  }
  public boolean is_on() {
    return on;
  }

  // Up
  public PVector up(PVector up_) { this.u = up_; return this.u; } 
  public PVector up() { return this.u; }

  // Eye
  public PVector eye(PVector eye_) { this.e = eye_;return this.e; }
  public PVector eye() { return this.e; }
  
  // Center
  public PVector center(PVector center_) { this.c = center_; return this.c; }
  public PVector center() { return this.c; }
  
  
}
public abstract class CameraRig {

  protected PVector e = new PVector(0,0,0);
  protected PVector c = new PVector(0,0,0);
  protected PVector u = GLOBAL_DOWN;
  protected Camera cam = null;


  private final String DEF_ATTACH_MODE = "cam_snap";
  private final boolean DEF_REPLACE = false;

  // Moves the camera according to some function
  public abstract boolean move();

  // Displays the instructions for the camera to the screen
  public abstract void instructions();


  // Films the camera
  public boolean film() {
    if (cam != null)
      return cam.film();
    return false;
  }

  // Set current camera to null and returns what was attached.
  public Camera detach() {
    Camera temp = cam;
    cam = null;
    return temp;
  }

  
  public boolean attach(Camera cam_) {
    return this.attach(cam_,DEF_ATTACH_MODE);
  }

  public boolean attach(Camera cam_, String attach_mode) {
    return this.attach(cam_,attach_mode,DEF_REPLACE);
  }
  
  // Attaches the a camera to this rig, if possible.
  public boolean attach(Camera cam_, String attach_mode, boolean replace) {

    if (!replace && this.cam != null)
      return false;

    switch (attach_mode) {

      // Rig snaps to camera position
      case "rig_snap":
        cam = cam_;
        rig_snap();
        return true;

      // Camera snaps to rig position
      case "cam_snap":
        cam = cam_;
        cam_snap();
        return true;

      // Incorrect mode
      default:
        println("Invalid attach mode!");
        return false;
    }
  }



  // Snaps the rig to the camera's position
  private void rig_snap() {
    eye(cam.eye());
    center(cam.center());
    up(cam.up());
  }

  // Snaps the camera to the rig's position
  private void cam_snap() {
    cam.eye(eye());
    cam.center(center());
    cam.up(up());
  }

  // Up
  public PVector up(){ return this.u; }
  public PVector up(PVector up_) {
    this.u=up_;
    if (cam != null)
      cam.up(up());

    return up();
  } 

  // Eye
  public PVector eye(){ return this.e; }
  public PVector eye(PVector eye_) {
    this.e=eye_;
    if (cam != null)
      cam.eye(eye());

    return eye();
  }
  
  
  // Center
  public PVector center(){ return this.c; }
  public PVector center(PVector center_) {
    this.c = center_;
    if (cam != null)
      cam.center(center());

    return this.c;
  } 
}
class Environment {
  
  private CameraRig rig;
  private final float floor = 0;
  
  public Environment() {
    PVector eye = new PVector(300,300,300);
    rig = new FreeCameraRig(eye);
  }

  public Environment(CameraRig c) {
    rig = c;
  }

  public void clear() {
    background(0);
  }

  public void film() {
    rig.move();
    rig.film();
    rig.instructions();
  }

  public float floor() {
    return floor;
  }




  private void draw_sphere(int r, float x, float y, float z) {
    pushMatrix();
    translate(x,y,z);
    noStroke();
    sphere(r);
    popMatrix();
  }
}
public class FreeCameraRig extends CameraRig {

  float F_SPEED = 30;
  float LR_SPEED = 30;
  float UP_SPEED = 20;
  float UD_LOOK_SPEED = 0.05f;
  float LR_LOOK_SPEED = 0.1f;
  float ud_theta = 0;
  float lr_theta = 0;
  float CAM_RADIUS = 1;

  private final PVector DEF_UP = GLOBAL_DOWN;
  private final PVector DEF_EYE = new PVector(0,0,0);

  
  // Default Constructor
  public FreeCameraRig() {
    init(DEF_EYE,DEF_UP);
  }

  public FreeCameraRig(PVector eye_) {
    init(eye_,DEF_UP);
  }

  public FreeCameraRig(PVector eye_, PVector up_) {
    init(eye_,up_);
  }

  public void init(PVector eye_,PVector up_) {

    eye(eye_);
    center(new PVector(eye_.x+CAM_RADIUS,eye_.y,eye_.z));
    up(up_);

    attach(new Camera());
  }

  // Implement abstract method from CameraRig
  public void instructions() {
    //textSize(32);
    //fill(255);
    //text("Hello",0,0);
  }
  // Implement abstract method from CameraRig
  public boolean move() {
    
    // WASD key presses
    int[] WASD = get_WASD();
    int WS = WASD[0] - WASD[2]; // -1 | 0 | 1
    int AD = WASD[1] - WASD[3]; // -1 | 0 | 1

    // UP/DOWN key presses
    int[] up_down_keys = {32,17};
    int[] up_down = get_keys(up_down_keys);
    int UD = up_down[0] - up_down[1];

    // Determine forward/backward movement
    PVector forward = PVector.sub(center(),eye());
    forward.normalize();
    forward.mult(F_SPEED);

    // Determing left/right movement
    PVector left = up().cross(forward);
    left.normalize();
    left.mult(LR_SPEED);

    // Multiply by key presses for direction
    forward.mult(WS);
    left.mult(AD);

    // Apply changes
    PVector change = PVector.add(forward,left);
    change.add(new PVector(0,UP_SPEED*UD,0));
    eye(eye().add(change));
    center(center().add(change));
    
    change_view();
    return true;
  }

  public void change_view() {
    
    if(!mouse_is_dragged)
      return;
  
    // Get mouse movement
    PVector mouse_change = new PVector((pmouseX-mouseX)*LR_LOOK_SPEED, (pmouseY-mouseY)*UD_LOOK_SPEED);

    // Change camera angle based on mouse movement
    ud_theta += mouse_change.y;
    ud_theta = min(89,ud_theta);
    ud_theta = max(-89,ud_theta);
    lr_theta += mouse_change.x % 360;

    // Find new position of camera center
    float rad = CAM_RADIUS;
    float y = sin(radians(ud_theta)) * rad;
    rad = sqrt(sq(rad) - sq(y));
    float x = cos(radians(lr_theta))*rad;
    float z = sin(radians(lr_theta))*rad;

    // Change camera center position
    center(new PVector(x+e.x,y+e.y,z+e.z));
  }
}
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
  protected float mass = 1.0f;




  /** 
    * Disables the stroke feature for this object
    */
  public void setNoStroke() {
    objStroke = new PVector(-1,-1,-1);
  }
  
  /**
    * Stores the Black White value for use later
    */
  public PVector setStroke(int b_w) {
    objStroke = new PVector(b_w,b_w,b_w);
    return objStroke;
  }
  
  /**
    * Stores the RGB value for use later
    */
  public PVector setStroke(int r, int g, int b) {
    objStroke = new PVector(r,g,b);
    return objStroke;
  }

  public PVector getStroke() {
    return objStroke;
  }

  /**
    * Disables the fill feature for this object
    */
  public void setNoFill() {
    objFill = new PVector(-1,-1,-1);
  }

  /**
    *  Stores the Black White value for use later
    */
  public PVector setFill(int b_w) {
    objFill = new PVector(b_w,b_w,b_w);
    return objFill;
  }

  /**
    * Stores the RGB value for use later
    */
  public PVector setFill(int r, int g, int b) {
    objFill = new PVector(r,g,b);
    return objFill;
  }

  public PVector getFill() {
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
public interface Shape {
  public void draw(PVector pos);
}
public interface State {
  
}
 public void drawCenterAxis() {
    // (0,0,0)

    int sphereRadius = 2;
    int lineLength = 500;
    noFill();

    stroke(255,0,0); // RED
    line(0,0,0,0,lineLength,0); // +Y-axis
    
    stroke(0,255,0); // GREEN
    line(0,0,0,lineLength,0,0); // +X-axis
 
    stroke(0,0,255); // BLUE
    line(0,0,0,0,0,lineLength); // +Z-axis
  
    fill(255);
    drawSphere(new PVector(0,lineLength,0),sphereRadius);
    drawSphere(new PVector(lineLength,0,0),sphereRadius);
    drawSphere(new PVector(0,0,lineLength),sphereRadius);
  }


/**
  * Draws a floor grid
  * @param floorHeight  - Y height of the grid
  * @param floorSize    - Side length of the grid
  * @param gridDensity  - Space between each grid line
  */
int DEF_FLOOR_HEIGHT = 0;
int DEF_FLOOR_SIZE = 3000;
int DEF_GRID_DENSITY = 200;
public void drawFloorGrid(int floorHeight, int floorSize, int gridDensity) {

  int fh = (floorHeight >= 0) ? floorHeight : DEF_FLOOR_HEIGHT;
  int fs = (floorSize >= 0) ? floorSize : DEF_FLOOR_SIZE;
  int gd = (gridDensity >= 0) ? gridDensity : DEF_GRID_DENSITY;
  
  noFill();
  stroke(255);

  for (int i = -fs; i <= fs; i += gd) {
    line(i,fh,-fs,i,fh,fs);
    line(-fs,fh,i,fs,fh,i);
  }
}



/**
  * Draws a room centerd from (0,0,0)
  * @param ceiling    - Y height of the ceiling
  * @param floorSize  - Size of the base of the room
*/
float DEF_ROOM_HEIGHT_ = 3000;
float DEF_ROOM_BASE = 6000;
public void drawRoom(float height_, float base) {

  float h = (height_ > 0) ? height_ : DEF_ROOM_HEIGHT_;
  float b = (base/2 > 0) ? base/2 : DEF_ROOM_BASE/2;
  float m = h/2;

  PVector template = new PVector(-b,-m,-b);

  // Generate 4 points
  for (int i = 0; i < 2;++i) {
    for (int j = 0; j < 2;++j) {

      PVector lmask = new PVector(i,(j+i)%2,j);
      PVector from = negate(template,lmask);

      // For each point, generate 3 more points 
      int[] temp = new int[]{0,0,0};
      for (int k = 0;k < 3;++k) {
        
        temp[k] = 1;
        PVector rmask = new PVector(temp[0],temp[1],temp[2]);
        PVector to = negate(from, rmask);
        from.add(0,m,0);
        to.add(0,m,0);

        // Draw a line from the original point and the generated points
        noFill();
        stroke(255);
        drawLine(from,to);

        from.add(0,-m,0);
        temp[k] = 0;
      }
    }
  }
}



public void drawLine(PVector from, PVector to) {
  line(from.x,from.y,from.z,to.x,to.y,to.z);
}

public void drawSphere(PVector pos, float radius) {
  pushMatrix();
  translate(pos.x,pos.y,pos.z);
  sphere(radius);
  popMatrix();
}

public void applyFill(PVector f) {
  if (f.x < 0 || f.y < 0 || f.z < 0)
    noFill();
  else
    fill(f.x,f.y,f.z);
}

public void applyStroke(PVector s) {
  if (s.x < 0 || s.y < 0 || s.z < 0)
    noStroke();
  else
    stroke(s.x,s.y,s.z);
}
HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();
boolean mouse_is_dragged = false;

public int[] get_ULDR() {
  int[] temp = {38,37,40,39};
  return get_keys(temp);
}
public int[] get_WASD() {
  int[] temp = {87,65,83,68};
  return get_keys(temp);
}
public int[] get_keys(int[] key_codes) {
  
  int[] temp = new int[key_codes.length];
  int count = 0;
  for (int code : key_codes) {
    if(keys.get(code) == null || keys.get(code) == false)
      temp[count++] = 0;
    else
      temp[count++] = 1; 
  }
  return temp;
}


public void mouseDragged() {
  mouse_is_dragged = true;
}

public void mouseReleased() {
  mouse_is_dragged = false;
}

public void keyPressed() {
  keys.put(keyCode,true);
}
public void keyReleased() {
  keys.put(keyCode,false);
}
PVector DEF_MASK = new PVector(1,1,1);
public PVector negate(PVector vec, PVector mask) {
  
  /** Function: -2n+1
    * O(0) =  1
    * O(1) = -1
    */
  mask.mult(-2);
  mask.add(1,1,1);

  PVector temp = new PVector(vec.x*mask.x,vec.y*mask.y,vec.z*mask.z);
  return temp;
}

public PVector negate(PVector vec) {
  return negate(vec,DEF_MASK);
}

public PVector rotateVector(PVector vec, PVector phi) {

  PVector v2D_xy = new PVector(vec.x,vec.y);
  v2D_xy.rotate(phi.x);
  PVector v2D_xz = new PVector(v2D_xy.x,vec.z);
  v2D_xz.rotate(phi.y);

  return new PVector( v2D_xz.x, v2D_xy.y,v2D_xz.y );
}

final boolean GLOBAL_DEBUG = true;
final float GLOBAL_G = 0.1f;
final PVector GLOBAL_DOWN = new PVector(0,-1,0);
  public void settings() {  fullScreen(P3D);  smooth(8); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Simulacron" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
