Enviroment env = new Enviroment();
float[] eye =  {width/2 , height/2 , 400};
float[] center = {width/2 , height/2 , 0};
float[] up = {0,1,0};

Camera cam_ = new Camera( eye , center , up);
Camera cam = new CameraController(cam_,env);

void setup() {
  fullScreen(P3D);
  background(0);
  frameRate(60);
  smooth(8);
  
}

void draw() {
  background(0);
  stroke(255);
  noFill();
  box(90);
  box(100);
  env.film();
  
}

void keyPressed() {
  handleKeyPress(keyCode,true);
}
void keyReleased() {
  handleKeyPress(keyCode,false);
}
