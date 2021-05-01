Environment env;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);

  env = new Environment();
}

void draw() {
  p1();
}

void p1() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
  env.film();
}

