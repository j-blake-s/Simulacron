public void drawLine(PVector from, PVector to) {
  line(from.x,from.y,from.z,to.x,to.y,to.z);
}

public void drawSphere(PVector pos, int radius) {
  pushMatrix();
  translate(pos.x,pos.y,pos.z);
  sphere(radius);
  popMatrix();
}

public void fill(PVector fill) {
  if (fill.x < 0 || fill.y < 0 || fill.z < 0)
    noFill();
  else
    fill(fill.x,fill.y,fill.z);
}

public void stroke(PVector stroke) {
  if (stroke.x < 0 || stroke.y < 0 || stroke.z < 0)
    noStroke();
  else
    stroke(stroke.x,stroke.y,stroke.z);
}