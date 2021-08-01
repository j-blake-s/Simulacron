Environment env;

PObject thing;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);
  env = new Environment();
  thing = new PObject(new PVector(0,500,0));
  
}

void draw() {
  p0();
  thing.apply_force(new PVector(0.005,0.01,0));
  thing.update();
  thing.track();
  thing.draw_path();
  p9();
}

void p0() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
}

void p9() {
  env.film();
}





