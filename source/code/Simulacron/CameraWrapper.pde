
class CameraWrapper extends Camera {

  Camera cam;

  CameraWrapper(Camera cam_) {
    this.cam = cam_;
  }

  void film() {
    this.cam.film();
  }
  



  void setUp(PVector up_) {
    this.cam.up = up_;
  } 

  PVector getUp() {
    return this.cam.up;
  }
 


  void setEye(PVector eye_) {
    this.cam.eye = eye_;
  }

  PVector getEye() {
    return this.cam.eye;
  }



  void setCenter(PVector center_) {
    this.cam.center = center_;
  }

  PVector getCenter() {
    return this.cam.center;
  }

}
