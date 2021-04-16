class Enviroment {
  
  private CameraRig rig;
  public Enviroment() {
    PVector eye = new PVector(100,100,100);
    rig = new FreeCameraRig(eye);
    rig.center(new PVector(0,0,0));
  }

  public Enviroment(CameraRig c) {
    rig = c;
  }

  public void clear() {
    background(0);
    drawFloorGrid();
    drawCenterAxis();
  }

  public void film() {
    rig.move();
    rig.film();
  }

  public void drawFloorGrid() {
    int y = 0;
    int base = 8000;
    int grain = 400;

    noFill();
    stroke(255);
    for (int i = -base; i < base; i += grain) {
      line(i,y,-base,i,y,base);
      line(-base,y,i,base,y,i);
    }
  }


  public void drawCenterAxis() {
    // (0,0,0)

    noFill();

    stroke(255,0,0); // RED
    line(0,0,0,0,50,0); // +Y-axis
    
    stroke(0,255,0); // GREEN
    line(0,0,0,50,0,0); // X-axis
 
    stroke(0,0,255); // BLUE
    line(0,0,0,0,0,50);
  
    fill(255);
    draw_sphere(2,0,50,0);
    draw_sphere(2,50,0,0);
    draw_sphere(2,0,0,50);
  }


  private void draw_sphere(int r, float x, float y, float z) {
    pushMatrix();
    translate(x,y,z);
    noStroke();
    sphere(r);
    popMatrix();
  }
}
