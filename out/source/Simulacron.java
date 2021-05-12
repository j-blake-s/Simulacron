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
  p1();
}

public void p1() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
  env.film();
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
    rig.center(new PVector(299,300,299));
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
    draw_sphere(sphereRadius,0,lineLength,0);
    draw_sphere(sphereRadius,lineLength,0,0);
    draw_sphere(sphereRadius,0,0,lineLength);
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

  float F_SPEED = 20;
  float LR_SPEED = 20;
  float UP_SPEED = 5;

  private final PVector DEF_UP = GLOBAL_DOWN;
  private final PVector DEF_DIR = new PVector(0,0,1);
  private final PVector DEF_EYE = new PVector(0,0,0);

  
  // Default Constructor
  public FreeCameraRig() {
    init(DEF_EYE,DEF_DIR,DEF_UP);
  }

  public FreeCameraRig(PVector eye_) {
    init(eye_,DEF_DIR,DEF_UP);
  }
  
  public FreeCameraRig(PVector eye_,PVector dir) {
    init(eye_,dir,DEF_UP);
  }

  public FreeCameraRig(PVector eye_,PVector dir, PVector up_) {
    init(eye_,dir,up_);
  }

  public void init(PVector eye_,PVector dir,PVector up_) {

    PVector temp = PVector.add(eye_,dir.normalize());
    eye(eye_);
    center(temp);
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




    // Get current values
    PVector new_eye = eye();
    PVector new_center = center();
    PVector new_up = up();

    // Determine forward/backward movement
    PVector forward = PVector.sub(new_center,new_eye);
    forward.normalize();
    forward.mult(F_SPEED);



    // Determing left/right movement
    PVector left = new_up;
    //left.normalize();
    left = left.cross(forward);
    left.normalize();
    left.mult(LR_SPEED);



    // Multiply by key presses for direction
    forward.mult(WS);
    left.mult(AD);

    // Accumalate changes
    PVector change = PVector.add(forward,left);

    // Apply changes
    new_eye.add(change);
    new_center.add(change);

    // Make changes
    eye(new_eye);
    center(new_center);
    up(new_up);

    int[] up_down_keys = {32,17};
    int[] up_down = get_keys(up_down_keys);
    int UD = up_down[0] - up_down[1];

    PVector m_u = new PVector(0,UP_SPEED*UD,0);
    eye(PVector.add(eye(),m_u));
    center(PVector.add(center(),m_u));

    change_view();
    return true;
  }

  public void change_view() {
    // Fix the y position
    
    float dir = 3;
    
    if (mouseX > pmouseX)
      dir = -dir;

    if (!mouse_is_dragged)
      dir = 0;

      
    PVector distance = new PVector(mouseX-pmouseX,mouseY-pmouseY);
    float amount = distance.mag(); // Should be big
    PVector max_amount = new PVector(width,height);
    distance.div(max_amount.mag());

    PVector diff = PVector.sub(center(),eye());
    PVector diff2D = new PVector(diff.x,diff.z);
    diff2D.rotate(dir*distance.mag());
    diff = new PVector(diff2D.x,diff.y,diff2D.y);
    center(PVector.add(eye(),diff));
  }
  public PVector add(PVector t,float a, float b, float c) {
    return new PVector(t.x+a,t.y+b,t.z+c);
  }
}
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
  protected float mass = 1.0f;

  /** 
    * Disables the stroke feature for this object
    */
  public void setNoStroke() {
    stroke = new PVector(-1,-1,-1);
  }
  
  /**
    * Stores the Black White value for use later
    */
  public PVector setStroke(int b_w) {
    stroke = new PVector(b_w,b_w,b_w);
    return stroke;
  }
  
  /**
    * Stores the RGB value for use later
    */
  public PVector setStroke(int r, int g, int b) {
    stroke = new PVector(r,g,b);
    return stroke;
  }

  public PVector getStroke() {
    return stroke;
  }

  /**
    * Disables the fill feature for this object
    */
  public void setNoFill() {
    fill = new PVector(-1,-1,-1);
  }

  /**
    *  Stores the Black White value for use later
    */
  public PVector setFill(int b_w) {
    fill = new PVector(b_w,b_w,b_w);
    return fill;
  }

  /**
    * Stores the RGB value for use later
    */
  public PVector setFill(int r, int g, int b) {
    fill = new PVector(r,g,b);
    return fill;
  }

  public PVector getFill() {
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
public interface Shape {
  public void draw();
}
public interface State {
  
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
float DEF_ROOM_HEIGHT_ = 1500;
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

public void drawSphere(PVector pos, int radius) {
  pushMatrix();
  translate(pos.x,pos.y,pos.z);
  sphere(radius);
  popMatrix();
}

public void fill(PVector fill) {
  if (fill.x < 0 || fill.y < 0 || fill.z < 0)
    noFill();
  else
    fill(fill.x,fill.y,fill.z);
}

public void stroke(PVector stroke) {
  if (stroke.x < 0 || stroke.y < 0 || stroke.z < 0)
    noStroke();
  else
    stroke(stroke.x,stroke.y,stroke.z);
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
