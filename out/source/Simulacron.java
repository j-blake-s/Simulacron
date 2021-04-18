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
    PVector eye = new PVector(300,300,300);
    rig = new FreeCameraRig(eye);
    rig.center(new PVector(299,300,299));
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
    int base = 2000;
    int grain = 100;

    noFill();
    stroke(255);
    for (int i = -base; i < base; i += grain) {
      line(i,y,-base,i,y,base);
      line(-base,y,i,base,y,i);
    }
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
    
    float move_forward = 5;
    float move_left_right = 5;
    float move_up_speed = 5;

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
    forward.mult(move_forward);



    // Determing left/right movement
    PVector left = new_up;
    //left.normalize();
    left = left.cross(forward);
    left.normalize();
    left.mult(move_left_right);



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

    PVector m_u = new PVector(0,move_up_speed*UD,0);
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
public interface PObject {
  
  public PVector get_pos();
  public PVector set_pos();

  public PVector get_vel();
  public PVector set_vel();
  
  public PVector get_acc();
  public PVector set_acc();
  
  public PVector get_mass();
  public PVector set_mass();

  public void draw();
  public void show();
  public void hide();

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
