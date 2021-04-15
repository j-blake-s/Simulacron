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

Enviroment env = new Enviroment();
float[] eye =  {width/2 , height/2 , 400};
float[] center = {width/2 , height/2 , 0};
float[] up = {0,1,0};

Camera cam_ = new Camera( eye , center , up);
Camera cam = new CameraController(cam_,env);

public void setup() {
  
  background(0);
  frameRate(60);
  
  
}

public void draw() {
  
  env.clear();
  stroke(255);
  noFill();
  box(90);
  box(100);
  env.draw();
  
}

public void keyPressed() {
  handleKeyPress(keyCode,true);
}
public void keyReleased() {
  handleKeyPress(keyCode,false);
}

class Camera {
  
  PVector up;
  PVector eye;
  PVector center;
  

  Camera() {
    //this.init([0,1,0],[50,50,0],[0,1,0]);
  }
  
 
  Camera(float[] eye_, float[] center_, float[] up_ ) {
    this.init(eye_,center_,up_);
  }
  
 
  private void init(float[] eye_, float[] center_, float[] up_ ) {
    PVector e = new PVector(eye_[0],eye_[1],eye_[2]);
    PVector c = new PVector(center_[0],center_[1],center_[2]);
    PVector u = new PVector(up_[0],up_[1],up_[2]);
    
    
    this.eye = e;
    this.center = c;
    this.up = u;
  }
  

  public void film() {
    camera(eye.x,eye.y,eye.z,center.x,center.y,center.z,up.x,up.y,up.z);
  }
  
  

  public void setUp(PVector up_) {
    this.up = up_;
  } 

  public PVector getUp() {
    return this.up;
  }
 


  public void setEye(PVector eye_) {
    this.eye = eye_;
  }

  public PVector getEye() {
    return this.eye;
  }



  public void setCenter(PVector center_) {
    this.center = center_;
  }

  public PVector getCenter() {
    return this.center;
  }
  
  
}


class CameraController extends CameraWrapper {

  Enviroment env;
  CameraController(Camera cam_,Enviroment env_) {
     super(cam_);
     this.env = env_;
     this.env.cam = this;
    
  }
   
  public void film() {
    this.update();
    super.film();
  }

  //Update the camera after every update cycle
  //Uses https://keycode.info/ for key info
  private void update() {
     

  

    HashMap<Integer,Boolean> keys = this.env.getUserInput();
     
    // If keys is null, then user input is not being allowed
    if (keys == null) {
      return;
    } 

    //Othewise check user input
    else {
      this.updateEye(keys);
      this.updateCenter(keys);
      this.updateUp(keys);
    }
  }
  private void updateEye(HashMap<Integer,Boolean> keyMap) {

    PVector eyeDirection = new PVector(0,0);
  
    if (keyMap == null)
      return;
      
   
    //Checking keys
    if (keyMap.containsKey(37) && keyMap.get(37) == true) { // Left Arrow
      eyeDirection.x -= 1;
    }

    if (keyMap.containsKey(38) && keyMap.get(38) == true) { //Up Arrow
      eyeDirection.y -= 1;
    }

    if (keyMap.containsKey(39) && keyMap.get(39) == true) { //Right Arrow
      eyeDirection.x += 1;
    }

    if (keyMap.containsKey(40) && keyMap.get(40) == true) { //Down Arrow
      eyeDirection.y += 1;
    }

    super.setEye(Math.CamMath.moveEye(super.cam, eyeDirection));

  }
  private void updateCenter(HashMap<Integer,Boolean> keyMap){
    return;
  }
  private void updateUp(HashMap<Integer,Boolean> keyMap) {
    return;
  }

  

}

class CameraWrapper extends Camera {

  Camera cam;

  CameraWrapper(Camera cam_) {
    this.cam = cam_;
  }

  public void film() {
    this.cam.film();
  }
  



  public void setUp(PVector up_) {
    this.cam.up = up_;
  } 

  public PVector getUp() {
    return this.cam.up;
  }
 


  public void setEye(PVector eye_) {
    this.cam.eye = eye_;
  }

  public PVector getEye() {
    return this.cam.eye;
  }



  public void setCenter(PVector center_) {
    this.cam.center = center_;
  }

  public PVector getCenter() {
    return this.cam.center;
  }

}
HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();

public void handleKeyPress(int keycode,Boolean pressed) {
  switch (keycode){
    default:
      keys.put(keycode,pressed);
      break;
  }
}

class Enviroment {
  
  Camera cam;
  Enviroment() {
  }

  // Clear the enviroment 
  public void clear() {
    background(0);
  }

  // Draw the enviroment state
  public void draw() {
    // Stage Camera
    this.film();
  }


  public void film() {
    this.cam.film();
  }
  

  public HashMap<Integer,Boolean> getUserInput() {
    if (this.allowsUserInput()) {
      return keys;
    } else {
      return null;
    }
  }
  
  private Boolean allowsUserInput() {
    return true;
  }
}
static class Math {
  static class CamMath {
    
    public static PVector moveEye(Camera cam, PVector direction) {
      
      float dv = 0.02f; 
      
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
