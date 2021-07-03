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

public PVector rotateVector(PVector vec, PVector phi) {

  PVector v2D_xy = new PVector(vec.x,vec.y);
  v2D_xy.rotate(phi.x);
  PVector v2D_xz = new PVector(v2D_xy.x,vec.z);
  v2D_xz.rotate(phi.y);

  return new PVector( v2D_xz.x, v2D_xy.y,v2D_xz.y );
}

