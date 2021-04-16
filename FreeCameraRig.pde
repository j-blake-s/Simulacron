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