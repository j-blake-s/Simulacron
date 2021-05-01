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

  void init(PVector eye_,PVector dir,PVector up_) {

    PVector temp = PVector.add(eye_,dir.normalize());
    eye(eye_);
    center(temp);
    up(up_);

    attach(new Camera());
  }

  // Implement abstract method from CameraRig
  public boolean move() {
    
    float move_forward = 10;
    float move_left_right = 10;
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