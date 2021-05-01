Enviroment env;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);

  env = new Enviroment();
  ball = new Ball();
}

void draw() {
  background(0);
  drawFloorGrid(-1,-1,-1);
  drawRoom(0,1000,2000);
}

