
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
  

  void film() {
    camera(eye.x,eye.y,eye.z,center.x,center.y,center.z,up.x,up.y,up.z);
  }
  
  

  void setUp(PVector up_) {
    this.up = up_;
  } 

  PVector getUp() {
    return this.up;
  }
 


  void setEye(PVector eye_) {
    this.eye = eye_;
  }

  PVector getEye() {
    return this.eye;
  }



  void setCenter(PVector center_) {
    this.center = center_;
  }

  PVector getCenter() {
    return this.center;
  }
  
  
}