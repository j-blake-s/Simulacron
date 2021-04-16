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

Enviroment env;
public void setup() {
  
  
  background(0);
  frameRate(60);

  env = new Enviroment();
}

public void draw() {

  env.clear();
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
  protected PVector u = new PVector(0,-1,0);
  protected Camera cam = null;


  private final String DEF_ATTACH_MODE = "cam_snap";
  private final boolean DEF_REPLACE = false;

  // Moves the camera according to some function
  public abstract boolean move();

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
class Enviroment {
  
  private CameraRig rig;
  public Enviroment() {
    PVector eye = new PVector(100,100,100);
    rig = new FreeCameraRig(eye);
    rig.center(new PVector(0,0,0));
  }

  public Enviroment(CameraRig c) {
    rig = c;
  }

  public void clear() {
    background(0);
    drawFloorGrid();
    drawCenterAxis();
  }

  public void film() {
    rig.move();
    rig.film();
  }

  public void drawFloorGrid() {
    int y = 0;
    int base = 8000;
    int grain = 400;

    noFill();
    stroke(255);
    for (int i = -base; i < base; i += grain) {
      line(i,y,-base,i,y,base);
      line(-base,y,i,base,y,i);
    }
  }


  public void drawCenterAxis() {
    // (0,0,0)

    noFill();

    stroke(255,0,0); // RED
    line(0,0,0,0,50,0); // +Y-axis
    
    stroke(0,255,0); // GREEN
    line(0,0,0,50,0,0); // X-axis
 
    stroke(0,0,255); // BLUE
    line(0,0,0,0,0,50);
  
    fill(255);
    draw_sphere(2,0,50,0);
    draw_sphere(2,50,0,0);
    draw_sphere(2,0,0,50);
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

  private final PVector DEF_UP = new PVector(0,-1,0);
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
  public boolean move() {

    int scalar = 2;
    PVector diff = PVector.sub(center(),eye()).normalize();
    diff.mult(scalar); // Base vector - has forward direction

    int[] WASD = get_WASD();
    int[] ULDR = get_ULDR();

    PVector f_b_move = PVector.mult(diff,WASD[0] - WASD[2]); 
    PVector l_r_move = PVector.mult(diff,WASD[1] - WASD[3]); 
    int u_d_look = ULDR[0] - ULDR[2];
    int l_r_look = ULDR[1] - ULDR[3];
    
    PVector newEye = eye();
    PVector newCenter = center();
    PVector newUp = up();

    newEye.add(f_b_move);
    newCenter.add(f_b_move);

    return true;

    
  }

  public PVector add(PVector t,float a, float b, float c) {
    return new PVector(t.x+a,t.y+b,t.z+c);
  }
}
HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();


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

public void keyPressed() {
  keys.put(keyCode,true);
}
public void keyReleased() {
  keys.put(keyCode,false);
}
static class Math {
  static class CamMath {
    
    public static PVector moveEye(Camera cam, PVector direction) {
      /*
      float dv = 0.02; 
      
      //Get copies
      PVector eye = cam.getEye().copy();
      PVector center = cam.getCenter().copy();
      //PVector up = cam.getUp().copy();

      eye = eye.sub(center); //Get eye relative to center
      float radius = eye.mag(); //Store the initial radius of the eye
      eye.normalize();
      
      PVector zAxis = new PVector(0,1); //TODO - Changeable: zAxis.z -> -1
      PVector xz = new PVector(eye.x,eye.z);
      float a = PVector.angleBetween(xz,zAxis);
      xz.rotate(a); //Get xz to zAxis
      PVector temp = new PVector(eye.y,xz.y); //TODO - Changeable: Switch values
      temp.rotate(dv*direction.y);
      xz = new PVector(0,temp.y);
      xz.rotate(-a + dv*direction.x);

      eye = new PVector(xz.x,temp.x,xz.y);
      eye.mult(radius); //Return the eye to the initial radius
      eye = eye.add(center); //Return eye to absolute position
      return eye;
    */
    return null;
    }
    
  }
}
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
