public interface PObject {
  
  public PVector get_pos();
  public PVector set_pos(PVector vector);

  public PVector get_vel();
  public PVector set_vel(PVector vector);
  
  public PVector get_acc();
  public PVector set_acc(PVector vector);
  
  public float get_mass();
  public float set_mass(float f);

}