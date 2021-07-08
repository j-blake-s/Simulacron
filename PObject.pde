/** PObject
  * The superclass of the physics engine. All objects will inherit from this.
  */
public class PObject {
  // Variables
    // Default
    private Shape DEF_SHAPE = new Ball();
    private Point DEF_POINT = new Point();

    // Instance 
    Shape shape = DEF_SHAPE;
    Point point = DEF_POINT;

  // Construction

    // Default
    PObject(HashMap<String,Object> settings) {
      init(settings);
    }

    // init Method
    void init(HashMap<String,Object> settings) {
      set_pos((PVector)settings.get("pos"));
      set_vel((PVector)settings.get("vel"));
      set_acc((PVector)settings.get("acc"));
      set_force((PVector)settings.get("force"));
      
      if (settings.containsKey("mass")) {
        set_mass((float)settings.get("mass"));
      }

    }

  // Adapter Methods

    void update() {
      this.point.update();
      //this.shape.draw(point);
    }

    void add_force(PVector in_force) {
      this.point.add_force(in_force);
    }
    

  // Getters/Setters
    // Point
      // Pos
      PVector get_pos() {
        return this.point.get_pos();
      }
      void set_pos(PVector in_pos) {
        this.point.set_pos(in_pos);
      }

      // Vel 
      PVector get_vel() {
        return this.point.get_vel();
      }
      void set_vel(PVector in_vel) {
        this.point.set_vel(in_vel);
      }

      // Acc
      PVector get_acc() {
        return this.point.get_acc();
      }
      void set_acc(PVector in_acc) {
        this.point.set_acc(in_acc);
      }

      // Force
      PVector get_force() {
        return this.point.get_force();
      }
      void set_force(PVector in_force) {
        this.point.set_force(in_force);
      }

      // Mass
      float get_mass() {
        return this.point.get_mass();
      }
      void set_mass(float in_mass) {
        this.point.set_mass(in_mass);
      }
}