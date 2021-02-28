

class CameraController extends CameraWrapper {

  Enviroment env;
  CameraController(Camera cam_,Enviroment env_) {
     super(cam_);
     this.env = env_;
     this.env.cam = this;
    
  }
   
  void film() {
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
