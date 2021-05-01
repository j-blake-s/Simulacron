Enviroment env;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);

  env = new Enviroment();
}

void draw() {
  env.clear();
  drawFloorGrid(-1,3000,200);
  drawRoom(0,1500,6000);
  env.film();
}

