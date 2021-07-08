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
PObject obj;
PVector[] path;
int path_size = 1000;
int count = 0;

int clock = 0;


public void setup() {
  
  
  background(0);
  frameRate(60);
  env = new Environment();
  HashMap<String,Object> settings = new HashMap<String,Object>();
  settings.put("pos",new PVector(-100,200,200));
  settings.put("vel",new PVector(3,1,-1));
  obj = new PObject(settings);
  path = new PVector[path_size];
  for (int i = 0; i < path_size;++i) {
    path[i] = null;
  }

}

public void draw() {

  env.clear();

  drawSphere(new PVector(0,500,0),10);
  PVector diff = PVector.sub(new PVector(0,500,0),obj.get_pos());
  diff.div(sq(diff.mag()));
  diff.mult(8);


  obj.add_force(diff);

  if (clock % 3 == 0) {
    PVector temp = new PVector(0,0,0);
    temp.set(obj.get_pos());
    path[count++] = temp;
    count %= path_size;
  }

  fill(255,0,0);
  noStroke();
  for (int i = 0; i < path_size-1;++i) {
    int from = (count + i) % path_size;
    int to = (count + i + 1) % path_size;

    stroke(255,0,0);
    if (path[from] != null & path[to] != null) {
      drawLine(path[from],path[to]);
    }
  }

  obj.update();

  

  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);

  env.film();
  clock += 1;
  clock %= 60;
  
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
    int[] up_down_keys = {32,17}; // Graphed to Space and Lft-Ctrl
    int[] up_down = get_keys(up_down_keys);
    int UD = up_down[0] - up_down[1];

    // Determine forward/backward movement
    PVector forward = PVector.sub(center(),eye());
    forward.normalize();

    // Determine left/right movement
    PVector left = up().cross(forward);
    left.normalize();

    // Determine up/down movement
    PVector up = left.cross(forward);
    up.normalize();

    // Multiply by key presses for direction
    forward.mult(WS*F_SPEED);
    left.mult(AD*LR_SPEED);
    up.mult(UD*UP_SPEED);

    // Apply changes
    PVector change = PVector.add(forward,left);
    change.add(up);
    eye(eye().add(change));
    center(center().add(change));
    
    // Change direction camera is looking
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
/** PObject
  * The superclass of the physics engine. All objects will inherit from this.
  */
public class PObject {
  // Variables
    // Default
    private Shape DEF_SHAPE = new Ball();
    private Point DEF_POINT = new Point();

    // Instance 
    Shape shape = DEF_SHAPE;
    Point point = DEF_POINT;

  // Construction

    // Default
    PObject(HashMap<String,Object> settings) {
      init(settings);
    }

    // init Method
    public void init(HashMap<String,Object> settings) {
      set_pos((PVector)settings.get("pos"));
      set_vel((PVector)settings.get("vel"));
      set_acc((PVector)settings.get("acc"));
      set_force((PVector)settings.get("force"));
      
      if (settings.containsKey("mass")) {
        set_mass((float)settings.get("mass"));
      }

    }

  // Adapter Methods

    public void update() {
      this.point.update();
      //this.shape.draw(point);
    }

    public void add_force(PVector in_force) {
      this.point.add_force(in_force);
    }
    

  // Getters/Setters
    // Point
      // Pos
      public PVector get_pos() {
        return this.point.get_pos();
      }
      public void set_pos(PVector in_pos) {
        this.point.set_pos(in_pos);
      }

      // Vel 
      public PVector get_vel() {
        return this.point.get_vel();
      }
      public void set_vel(PVector in_vel) {
        this.point.set_vel(in_vel);
      }

      // Acc
      public PVector get_acc() {
        return this.point.get_acc();
      }
      public void set_acc(PVector in_acc) {
        this.point.set_acc(in_acc);
      }

      // Force
      public PVector get_force() {
        return this.point.get_force();
      }
      public void set_force(PVector in_force) {
        this.point.set_force(in_force);
      }

      // Mass
      public float get_mass() {
        return this.point.get_mass();
      }
      public void set_mass(float in_mass) {
        this.point.set_mass(in_mass);
      }
}
/** Point
  * Tracks physical information as it relates to a "Point" in space.
  * Forces applied to a Point cause it to move when updated.
  */
public class Point {
  // Variables
    // Default
      private PVector DEF_POS = new PVector(0,0,0);
      private PVector DEF_VEL = new PVector(0,0,0);
      private PVector DEF_ACC = new PVector(0,0,0);
      private PVector DEF_FORCE = new PVector(0,0,0);
      private float DEF_MASS = 1;
      private float MAX_MASS = 10000;


    // Instance
      PVector pos = DEF_POS;
      PVector vel = DEF_VEL;
      PVector acc = DEF_ACC;
      PVector force = DEF_FORCE;
      float   mass = DEF_MASS;

  // Physics Methods
    
    /** Add Force
      * Adds some force vector to the net force of this Point.
      * @param PVector of force
      */
    public void add_force(PVector in_force) {
      this.force.add(in_force);
    }

    /** Update
      * Applies accumulated force onto this Point.
      */
    public void update() {

      // A = F/m
      acc.set(PVector.div(force,mass));

      // V = dA
      vel.add(acc);

      // P = dV
      pos.add(vel);

      // Set net force to 0 for next frame
      force.set(0,0,0);

    }

  // Technical Methods

    /** Clone
      * Creates a new Point with the same parameters as this Point.
      * @returns The newly created Point.
      */
    public Point clone() {
      return new Point(pos,vel,acc,force,mass);
    }

    /** Copy
      * This Point copies another Point's parameters.
      * @param Some Point object
      */
    public void copy(Point in_point) {
      init(in_point.pos,in_point.vel,in_point.acc,in_point.force,in_point.mass);
    }

  // Construction

    // Default Constructor
    Point() {
      init(null,null,null,null,-1);
    }

    // Shorthand Constructors
    Point(PVector in_pos) {
      init(in_pos,null,null,null,-1);
    }

    Point(PVector in_pos,float in_mass) {
      init(in_pos,null,null,null,in_mass);
    }

    // Full Constructor
    Point(PVector in_pos,PVector in_vel,PVector in_acc,PVector in_force,float in_mass) {
      init(in_pos,in_vel,in_acc,in_force,in_mass);
    }

    // Init Method
    private void init(PVector in_pos,PVector in_vel,PVector in_acc,PVector in_force,float in_mass) {
      set_pos(in_pos);
      set_vel(in_vel);
      set_acc(in_acc);
      set_force(in_force);
      set_mass(in_mass);
    }

  // Getters/Setters

    // Position
      public PVector get_pos() {
        return this.pos;
      }
      public void set_pos(PVector in_pos) {
        this.pos = (in_pos==null) ?  this.pos : in_pos;
      }

    // Velocity
      public PVector get_vel() {
        return this.vel;
      }
      public void set_vel(PVector in_vel) {
        this.vel = (in_vel==null) ? this.vel : in_vel;
      }


    // Acceleration
      public PVector get_acc() {
        return this.acc;
      }

      public void set_acc(PVector in_acc) {
        this.acc = (in_acc==null) ? this.acc : in_acc;
      }

    // Force
      public PVector get_force() {
        return this.force;
      }
      public void set_force(PVector in_force) {
        this.force = (in_force == null) ? this.force : in_force;
      }

    // Mass
      public float get_mass() {
        return this.mass;
      }
      public void set_mass(float in_mass) {
        this.mass = (in_mass <= 0 || in_mass > MAX_MASS) ? this.mass : in_mass;
      }

}
/** Shape
  * Tracks graphical info
  * Draws objects to screen
  */
public abstract class Shape {
  // Variables
    // Default
    protected int[] DEF_STROKE = new int[]{255};
    protected int[] DEF_FILL = new int[]{255};
    protected float DEF_SIZE = 5;

    // Instance
    int[] stroke_info = DEF_STROKE;
    int[] fill_info = DEF_FILL;
    float size = DEF_SIZE;

  // Abstract Methods
    /** Make Shape
      * Makes some shape around a given Point
      * @param some Point in space
      */
    public abstract void make_shape(Point p);

  // Functional Methods
    
    /** Draw
      * Sets global stroke and fill, then makes shape around some Point
      * @param some Point in space
      */
    public void draw(Point p) {
      apply_fill();
      apply_stroke();
      make_shape(p);
    }


    /** Apply fill
      * Sets the global fill to this Shapes fill info using the fill method
      */
    public void apply_fill() {
      switch (fill_info.length) {

        // Grayscale
        case 1:
          fill(fill_info[0]);
          break;

        // Grayscale w/ alpha
        case 2:
          fill(fill_info[0],fill_info[1]);
          break;

        // RGB
        case 3:
          fill(fill_info[0],fill_info[1],fill_info[2]);
          break;

        // RGB w/ alpha
        case 4:
          fill(fill_info[0],fill_info[1],fill_info[2],fill_info[3]);
          break;

        // No fill
        default:
          noFill();
          break;
      }
    }

    /** Apply Stroke
      * Sets the global stroke to this Shapes stroke info using the stroke method
      */
    public void apply_stroke() {
      switch (stroke_info.length) {

        // Grayscale
        case 1:
          stroke(stroke_info[0]);
          break;

        // Grayscale w/ alpha
        case 2:
          stroke(stroke_info[0],stroke_info[1]);
          break;

        // RGB
        case 3:
          stroke(stroke_info[0],stroke_info[1],stroke_info[2]);
          break;

        // RGB w/ alpha
        case 4:
          stroke(stroke_info[0],stroke_info[1],stroke_info[2],stroke_info[3]);
          break;

        // No stroke
        default:
          noStroke();
          break;
      }
    }

    /** Clear
      * Sets the stroke and fill to empty
      */
    public void clear() {
      set_no_stroke();
      set_no_fill();
    }

  // Technical Methods

    /** Copy
      * Copies the stroke and fill of another Shape
      * @param some Shape
      */
    public void copy(Shape s) {
      set_stroke(s.get_stroke());
      set_fill(s.get_fill());
    }

  // Setting fill

    // No fill
    public void set_no_fill() {
      this.fill_info = new int[]{};
    }

    // Copy fill
    public void set_fill(int[] in_fill) {
      this.fill_info = in_fill;
    }

    // Default fill
    public void set_fill() {
      this.fill_info = DEF_FILL;
    }

    // Grayscale
    public void set_fill(int b_w) {
      this.fill_info = new int[]{b_w};
    }

    // Grayscale w/ alpha
    public void set_fill(int b_w,int alpha) {
      this.fill_info = new int[]{b_w,alpha};
    }

    // RGB
    public void set_fill(int r, int g, int b) {
      this.fill_info = new int[]{r,g,b};
    }

    // RGB w/ alpha
    public void set_fill(int r, int g, int b, int alpha) {
      this.fill_info = new int[]{r,g,b,alpha};
    }

    // Get fill 
    public int[] get_fill() {
      return this.fill_info;
    }


  // Setting stroke
    
    // No stroke
    public void set_no_stroke() {
      this.stroke_info = new int[]{};
    }

    // Copy stroke
    public void set_stroke(int[] in_stroke) {
      this.stroke_info = in_stroke;
    }

    // Default stroke
    public void set_stroke() {
      this.stroke_info = DEF_STROKE;
    }

    // Grayscale
    public void set_stroke(int b_w) {
      this.stroke_info = new int[]{b_w};
    }

    // Grayscale w/ alpha
    public void set_stroke(int b_w, int alpha) {
      this.stroke_info = new int[]{b_w,alpha};
    }

    // RGB
    public void set_stroke(int r, int g, int b) {
      this.stroke_info = new int[]{r,g,b};
    }

    // RGB w/ alpha
    public void set_stroke(int r, int g, int b, int alpha) {
      this.stroke_info = new int[]{r,g,b,alpha};
    }

    // Get stroke 
    public int[] get_stroke() {
      return this.stroke_info;
    }




  // Getters/Setters
    // Size
    public void set_size(float in_size) {
      this.size = in_size < 1 ? this.size : in_size;
    }
    public float get_size() {
      return this.size;
    }
}
public class Ball extends Shape {
  Ball() {}
  Ball(float in_size) { set_size(in_size); }
  public void make_shape(Point p) { drawSphere(p.get_pos(),size); }
}
/** State
  * The "State" of a PObject determines reactions to certain stimuli
  */
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
int DEF_GRID_DENSITY = 750;
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
