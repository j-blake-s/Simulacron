Environment env;
PObject obj;
PVector[] path;
int path_size = 1000;
int count = 0;

int clock = 0;


void setup() {
  fullScreen(P3D);
  smooth(8);
  background(0);
  frameRate(60);
  env = new Environment();
  HashMap<String,Object> settings = new HashMap<String,Object>();
  settings.put("pos",new PVector(-100,200,200));
  settings.put("vel",new PVector(3,1,-1));
  obj = new PObject(settings);
  path = new PVector[path_size];
  for (int i = 0; i < path_size;++i) {
    path[i] = null;
  }

}

void draw() {

  env.clear();

  drawSphere(new PVector(0,500,0),10);
  PVector diff = PVector.sub(new PVector(0,500,0),obj.get_pos());
  diff.div(sq(diff.mag()));
  diff.mult(8);


  obj.add_force(diff);

  if (clock % 3 == 0) {
    PVector temp = new PVector(0,0,0);
    temp.set(obj.get_pos());
    path[count++] = temp;
    count %= path_size;
  }

  fill(255,0,0);
  noStroke();
  for (int i = 0; i < path_size-1;++i) {
    int from = (count + i) % path_size;
    int to = (count + i + 1) % path_size;

    stroke(255,0,0);
    if (path[from] != null & path[to] != null) {
      drawLine(path[from],path[to]);
    }
  }

  obj.update();

  

  drawFloorGrid(-1,-1,-1);
  drawRoom(-1,-1);

  env.film();
  clock += 1;
  clock %= 60;
  
}





