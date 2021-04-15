CameraRig rig;
void setup() {
  fullScreen(P3D);
  smooth(8);

  PVector eye = new PVector(0,0,0);
  PVector center = new PVector(width/2,height/2,500);
  PVector up = new PVector(0,-1,0);
  rig = new ManualCameraRig(eye,center,up);
  background(0);
  frameRate(60);
}

void draw() {

  background(0);

  // Let's create a room
  stroke(255);
  noFill();
  box(width,height,1000);

  rig.film();
  rig.move();
  
}

void keyPressed() {
  handleKeyPress(keyCode,true);
}
void keyReleased() {
  handleKeyPress(keyCode,false);
}
