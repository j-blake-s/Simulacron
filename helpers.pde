public void drawLine(PVector from, PVector to) {
  line(from.x,from.y,from.z,to.x,to.y,to.z);
}

public void drawSphere(PVector pos, float radius) {
  pushMatrix();
  translate(pos.x,pos.y,pos.z);
  sphere(radius);
  popMatrix();
}

public void applyFill(PVector f) {
  if (f.x < 0 || f.y < 0 || f.z < 0)
    noFill();
  else
    fill(f.x,f.y,f.z);
}

public void applyStroke(PVector s) {
  if (s.x < 0 || s.y < 0 || s.z < 0)
    noStroke();
  else
    stroke(s.x,s.y,s.z);
}