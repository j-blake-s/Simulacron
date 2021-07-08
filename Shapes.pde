public class Ball extends Shape {
  Ball() {}
  Ball(float in_size) { set_size(in_size); }
  void make_shape(Point p) { drawSphere(p.get_pos(),size); }
}