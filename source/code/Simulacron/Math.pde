static class Math {
  static class CamMath {
    
    static PVector moveEye(Camera cam, PVector direction) {
      
      float dv = 0.02; 
      
      //Get copies
      PVector eye = cam.getEye().copy();
      PVector center = cam.getCenter().copy();
      //PVector up = cam.getUp().copy();

      eye = eye.sub(center); //Get eye relative to center
      float radius = eye.mag(); //Store the initial radius of the eye
      eye.normalize();
      
      PVector zAxis = new PVector(0,1); //TODO - Changeable: zAxis.z -> -1
      PVector xz = new PVector(eye.x,eye.z);
      float a = PVector.angleBetween(xz,zAxis);
      xz.rotate(a); //Get xz to zAxis
      PVector temp = new PVector(eye.y,xz.y); //TODO - Changeable: Switch values
      temp.rotate(dv*direction.y);
      xz = new PVector(0,temp.y);
      xz.rotate(-a + dv*direction.x);

      eye = new PVector(xz.x,temp.x,xz.y);
      eye.mult(radius); //Return the eye to the initial radius
      eye = eye.add(center); //Return eye to absolute position
      return eye;
    }
    
  }
}
