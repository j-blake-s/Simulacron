CameraRig rig;
float count = 0;
void setup() {
  fullScreen(P3D);
  smooth(8);

  PVector eye = new PVector(0,-200,200);
  PVector center = new PVector(0,0,0);
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
  sphere(100);

  beginCamera();
  rig.film();
  rotateX(count);
  count += 0.1;
  endCamera();

  
}

void keyPressed() {
  handleKeyPress(keyCode,true);
}
void keyReleased() {
  handleKeyPress(keyCode,false);
}
