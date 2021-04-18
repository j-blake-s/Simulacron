Enviroment env;
Ball ball;
void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);

  env = new Enviroment();
  ball = new Ball();
}

void draw() {

  env.clear();
  gravity(ball);
  bounce(env,ball);
  ball.update();
  ball.draw();
  env.film();

}

