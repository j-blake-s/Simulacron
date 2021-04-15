void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);
}

void draw() {

  background(0);

}

void keyPressed() {
  handleKeyPress(keyCode,true);
}
void keyReleased() {
  handleKeyPress(keyCode,false);
}
