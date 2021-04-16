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
  env.film();

}

