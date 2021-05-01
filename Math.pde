PVector DEF_MASK = new PVector(1,1,1);
public PVector negate(PVector vec, PVector mask) {
  
  /** Function: -2n+1
    * O(0) =  1
    * O(1) = -1
    */
  mask.mult(-2);
  mask.add(1,1,1);

  PVector temp = new PVector(vec.x*mask.x,vec.y*mask.y,vec.z*mask.z);
  return temp;
}

public PVector negate(PVector vec) {
  return negate(vec,DEF_MASK);
}

