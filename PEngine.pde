public void gravity(PObject obj) {
  
  PVector grav = PVector.mult(GLOBAL_DOWN,GLOBAL_G);
  PVector acc = obj.getAcc();
  acc.add(grav);
  obj.setAcc(acc);
}
