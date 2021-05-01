/**
  * Draws a floor grid
  * @param floorHeight  - Y height of the grid
  * @param floorSize    - Side length of the grid
  * @param gridDensity  - Space between each grid line
  */
int DEF_FLOOR_HEIGHT = 0;
int DEF_FLOOR_SIZE = 2000;
int DEF_GRID_DENSITY = 100;
public void drawFloorGrid(int floorHeight, int floorSize, int gridDensity) {

  int fh = (floorHeight >= 0) ? floorHeight : DEF_FLOOR_HEIGHT;
  int fs = (floorSize >= 0) ? floorSize : DEF_FLOOR_SIZE;
  int gd = (gridDensity >= 0) ? gridDensity : DEF_GRID_DENSITY;
  
  noFill();
  stroke(255);

  for (int i = -fs; i < fs; i += gd) {
    line(i,fh,-fs,i,fh,fs);
    line(-fs,fh,i,fs,fh,i);
  }
}



/**
  * Draws a room centerd from (0,0,0)
  * @param floor      - Y height of the room
  * @param ceiling    - Y height of the ceiling
  * @param floorSize  - Size of the base of the room
*/
public void drawRoom(int floor, int ceiling, int floorSize) {
  
  int y1 = floor;
  int y2 = ceiling;

  int x1 = -floorSize/2;
  int x2 = floorSize/2;

  int z1 = -floorSize/2;
  int z2 =floorSize/2; 

  noFill();
  stroke(255);

  // Lines from (x1,y1,z1)
  line(x1,y1,z1,x2,y1,z1);
  line(x1,y1,z1,x1,y1,z2);
  line(x1,y1,z1,x1,y2,z1);

  // Lines from (x2,y1,z2)
  line(x2,y1,z2,x1,y1,z2);
  line(x2,y1,z2,x2,y2,z2);
  line(x2,y1,z2,x2,y1,z1);

  // Lines from (x2,y2,z1)
  line(x2,y2,z1,x2,y1,z1);
  line(x2,y2,z1,x2,y2,z2);
  line(x2,y2,z1,x1,y2,z1);


  // Lines from (x1,y2,z2)
  line(x1,y2,z2,x1,y1,z2);
  line(x1,y2,z2,x1,y2,z1);
  line(x1,y2,z2,x2,y2,z2);



  
}

