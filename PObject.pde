public interface PObject {
  
  public PVector getPos();
  public PVector setPos(PVector vector);

  public PVector getVel();
  public PVector setVel(PVector vector);
  
  public PVector getAcc();
  public PVector setAcc(PVector vector);
  

  public float getMass();
  public float setMass(float f);

}