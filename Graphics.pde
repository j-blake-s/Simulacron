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
    drawSphere(new PVector(0,lineLength,0),sphereRadius);
    drawSphere(new PVector(lineLength,0,0),sphereRadius);
    drawSphere(new PVector(0,0,lineLength),sphereRadius);
  }


/**
  * Draws a floor grid
  * @param floorHeight  - Y height of the grid
  * @param floorSize    - Side length of the grid
  * @param gridDensity  - Space between each grid line
  */
int DEF_FLOOR_HEIGHT = 0;
int DEF_FLOOR_SIZE = 3000;
int DEF_GRID_DENSITY = 200;
public void drawFloorGrid(int floorHeight, int floorSize, int gridDensity) {

  int fh = (floorHeight >= 0) ? floorHeight : DEF_FLOOR_HEIGHT;
  int fs = (floorSize >= 0) ? floorSize : DEF_FLOOR_SIZE;
  int gd = (gridDensity >= 0) ? gridDensity : DEF_GRID_DENSITY;
  
  noFill();
  stroke(255);

  for (int i = -fs; i <= fs; i += gd) {
    line(i,fh,-fs,i,fh,fs);
    line(-fs,fh,i,fs,fh,i);
  }
}



/**
  * Draws a room centerd from (0,0,0)
  * @param ceiling    - Y height of the ceiling
  * @param floorSize  - Size of the base of the room
*/
float DEF_ROOM_HEIGHT_ = 3000;
float DEF_ROOM_BASE = 6000;
public void drawRoom(float height_, float base) {

  float h = (height_ > 0) ? height_ : DEF_ROOM_HEIGHT_;
  float b = (base/2 > 0) ? base/2 : DEF_ROOM_BASE/2;
  float m = h/2;

  PVector template = new PVector(-b,-m,-b);

  // Generate 4 points
  for (int i = 0; i < 2;++i) {
    for (int j = 0; j < 2;++j) {

      PVector lmask = new PVector(i,(j+i)%2,j);
      PVector from = negate(template,lmask);

      // For each point, generate 3 more points 
      int[] temp = new int[]{0,0,0};
      for (int k = 0;k < 3;++k) {
        
        temp[k] = 1;
        PVector rmask = new PVector(temp[0],temp[1],temp[2]);
        PVector to = negate(from, rmask);
        from.add(0,m,0);
        to.add(0,m,0);

        // Draw a line from the original point and the generated points
        noFill();
        stroke(255);
        drawLine(from,to);

        from.add(0,-m,0);
        temp[k] = 0;
      }
    }
  }
}



