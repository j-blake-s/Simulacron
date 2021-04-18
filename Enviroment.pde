class Enviroment {
  
  private CameraRig rig;
  public Enviroment() {
    PVector eye = new PVector(300,300,300);
    rig = new FreeCameraRig(eye);
    rig.center(new PVector(299,300,299));
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
    int base = 2000;
    int grain = 100;

    noFill();
    stroke(255);
    for (int i = -base; i < base; i += grain) {
      line(i,floor,-base,i,floor,base);
      line(-base,floor,i,base,floor,i);
    }
  }


  public void drawCenterAxis() {
    // (0,0,0)

    int sphereRadius = 2;
    int lineLength = 500;
    noFill();

    stroke(255,0,0); // RED
    line(0,0,0,0,lineLength,0); // +Y-axis
    
    stroke(0,255,0); // GREEN
    line(0,0,0,lineLength,0,0); // +X-axis
 
    stroke(0,0,255); // BLUE
    line(0,0,0,0,0,lineLength); // +Z-axis
  
    fill(255);
    draw_sphere(sphereRadius,0,lineLength,0);
    draw_sphere(sphereRadius,lineLength,0,0);
    draw_sphere(sphereRadius,0,0,lineLength);
  }


  private void draw_sphere(int r, float x, float y, float z) {
    pushMatrix();
    translate(x,y,z);
    noStroke();
    sphere(r);
    popMatrix();
  }
}
