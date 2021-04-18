

final PVector down = new PVector(0,-1,0);
final float G = -0.1;



public void gravity(PObject obj) {
  
  PVector acc = obj.getAcc();
  acc.add(0,G,0);
  obj.setAcc(acc);

}


public void bounce(Enviroment env, PObject obj) {

  float floor = env.floor();
  PVector pos = obj.getPos();
  PVector vel = obj.getVel();
  PVector acc = obj.getAcc();  
  // Check floor
  if (pos.y <= floor)
    acc.add(0,10,0);
  
  obj.setAcc(acc);

  
}
