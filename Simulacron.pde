Environment env;
PObject obj;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);

  env = new Environment();
  obj = new PObject();
  obj.setPos(new PVector(0,500,0));
  obj.setVel(new PVector(0,0,0));
  
}

void draw() {
  p1();
}

void p1() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
  obj.applyForce(new PVector(0,-0.1,0));
  obj.update();
  obj.draw();
  env.film();
}

