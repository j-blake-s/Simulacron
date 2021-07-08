/** Point
  * Tracks physical information as it relates to a "Point" in space.
  * Forces applied to a Point cause it to move when updated.
  */
public class Point {
  // Variables
    // Default
      private PVector DEF_POS = new PVector(0,0,0);
      private PVector DEF_VEL = new PVector(0,0,0);
      private PVector DEF_ACC = new PVector(0,0,0);
      private PVector DEF_FORCE = new PVector(0,0,0);
      private float DEF_MASS = 1;
      private float MAX_MASS = 10000;


    // Instance
      PVector pos = DEF_POS;
      PVector vel = DEF_VEL;
      PVector acc = DEF_ACC;
      PVector force = DEF_FORCE;
      float   mass = DEF_MASS;

  // Physics Methods
    
    /** Add Force
      * Adds some force vector to the net force of this Point.
      * @param PVector of force
      */
    void add_force(PVector in_force) {
      this.force.add(in_force);
    }

    /** Update
      * Applies accumulated force onto this Point.
      */
    void update() {

      // A = F/m
      acc.set(PVector.div(force,mass));

      // V = dA
      vel.add(acc);

      // P = dV
      pos.add(vel);

      // Set net force to 0 for next frame
      force.set(0,0,0);

    }

  // Technical Methods

    /** Clone
      * Creates a new Point with the same parameters as this Point.
      * @returns The newly created Point.
      */
    Point clone() {
      return new Point(pos,vel,acc,force,mass);
    }

    /** Copy
      * This Point copies another Point's parameters.
      * @param Some Point object
      */
    void copy(Point in_point) {
      init(in_point.pos,in_point.vel,in_point.acc,in_point.force,in_point.mass);
    }

  // Construction

    // Default Constructor
    Point() {
      init(null,null,null,null,-1);
    }

    // Shorthand Constructors
    Point(PVector in_pos) {
      init(in_pos,null,null,null,-1);
    }

    Point(PVector in_pos,float in_mass) {
      init(in_pos,null,null,null,in_mass);
    }

    // Full Constructor
    Point(PVector in_pos,PVector in_vel,PVector in_acc,PVector in_force,float in_mass) {
      init(in_pos,in_vel,in_acc,in_force,in_mass);
    }

    // Init Method
    private void init(PVector in_pos,PVector in_vel,PVector in_acc,PVector in_force,float in_mass) {
      set_pos(in_pos);
      set_vel(in_vel);
      set_acc(in_acc);
      set_force(in_force);
      set_mass(in_mass);
    }

  // Getters/Setters

    // Position
      PVector get_pos() {
        return this.pos;
      }
      void set_pos(PVector in_pos) {
        this.pos = (in_pos==null) ?  this.pos : in_pos;
      }

    // Velocity
      PVector get_vel() {
        return this.vel;
      }
      void set_vel(PVector in_vel) {
        this.vel = (in_vel==null) ? this.vel : in_vel;
      }


    // Acceleration
      PVector get_acc() {
        return this.acc;
      }

      void set_acc(PVector in_acc) {
        this.acc = (in_acc==null) ? this.acc : in_acc;
      }

    // Force
      PVector get_force() {
        return this.force;
      }
      void set_force(PVector in_force) {
        this.force = (in_force == null) ? this.force : in_force;
      }

    // Mass
      float get_mass() {
        return this.mass;
      }
      void set_mass(float in_mass) {
        this.mass = (in_mass <= 0 || in_mass > MAX_MASS) ? this.mass : in_mass;
      }

}