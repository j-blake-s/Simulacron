public void drawLine(PVector from, PVector to) {
  line(from.x,from.y,from.z,to.x,to.y,to.z);
}

public void drawSphere(PVector pos, float radius) {
  pushMatrix();
  translate(pos.x,pos.y,pos.z);
  sphere(radius);
  popMatrix();
}

