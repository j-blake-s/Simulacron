public class System {
  
  // Variables

    // Default

    // Instance
    ArrayList<PObject> system;

  // Construction

    // Default Constructor
    System() {
      this.system = new ArrayList<PObject>();
    }

  // System Methods

    /** Update
      * Updates every PObect in this System.
      */
    void update() {
      for (int i = 0; i < system.size();++i) {
        PObject temp = system.get(i);
        temp.update();
        system.set(i,temp);
      }
    }

    /** Apply Force
      * Applies a given force to every object in the system.
      * @param a force vector
      */
    void apply_force(PVector in_force) {
      for (int i = 0; i < system.size();++i) {
        PObject temp = system.get(i);
        temp.apply_force(in_force);
        system.set(i,temp);
      }
    }


  // Insertion and Deletion
    
    // Default Add
    void add() {
      system.add(new PObject());
    }

    void add(PVector in_pos) {
      system.add(new PObject(in_pos));
    }
    // Full Add
    void add(HashMap<String,Object> settings) {
      system.add(new PObject(settings));
    }

    // Remove PObject
    PObject remove(int index) {
      if (index < system.size() && index >= 0) {
        return system.remove(index);
      }

      return null;
    }










    



}