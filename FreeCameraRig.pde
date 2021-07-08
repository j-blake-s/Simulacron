public class FreeCameraRig extends CameraRig {

  float F_SPEED = 30;
  float LR_SPEED = 30;
  float UP_SPEED = 20;
  float UD_LOOK_SPEED = 0.05;
  float LR_LOOK_SPEED = 0.1;
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

  void init(PVector eye_,PVector up_) {

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

    if (mouse_change.mag() <= 20) {
      // Change camera angle based on mouse movement
      ud_theta += mouse_change.y;
      ud_theta = min(89,ud_theta);
      ud_theta = max(-89,ud_theta);
      lr_theta += mouse_change.x % 360;
    }

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