public class ManualCameraRig extends CameraRig {

  // Default Constructor
  public ManualCameraRig() {
    attach(new Camera());
  }

  public ManualCameraRig(PVector eye_,PVector center_,PVector up_) {
    eye(eye_);
    center(center_);
    up(up_);
    attach(new Camera());

  }

  // Implement abstract method from CameraRig
  public boolean move() {
    PVector temp = eye();

    temp.set(temp.x+5,temp.y,temp.z);

    eye(temp);
    return true;
  }
}