class Environment {
  
  private CameraRig rig;
  private final float floor = 0;
  
  public Environment() {
    PVector eye = new PVector(300,300,300);
    rig = new FreeCameraRig(eye);
  }

  public Environment(CameraRig c) {
    rig = c;
  }

  public void clear() {
    background(0);
  }

  public void film() {
    rig.move();
    rig.film();
    rig.instructions();
  }

  public float floor() {
    return floor;
  }




  private void draw_sphere(int r, float x, float y, float z) {
    pushMatrix();
    translate(x,y,z);
    noStroke();
    sphere(r);
    popMatrix();
  }
}
