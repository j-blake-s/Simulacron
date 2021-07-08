Environment env;
Clock time;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);
  env = new Environment();
  time = new Clock();
  
}

void draw() {
  start();
  stop();
}

void start() {
  env.clear();
  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);
}

void stop() {
  env.film();
  time.tick();
}





