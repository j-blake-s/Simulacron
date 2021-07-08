/** Shape
  * Tracks graphical info
  * Draws objects to screen
  */
public abstract class Shape {
  // Variables
    // Default
    protected int[] DEF_STROKE = new int[]{255};
    protected int[] DEF_FILL = new int[]{255};
    protected float DEF_SIZE = 20;

    // Instance
    int[] stroke_info = DEF_STROKE;
    int[] fill_info = DEF_FILL;
    float size = DEF_SIZE;

  // Abstract Methods
    /** Make Shape
      * Makes some shape around a given Point
      * @param some Point in space
      */
    abstract void make_shape(Point p);

  // Functional Methods
    
    /** Draw
      * Sets global stroke and fill, then makes shape around some Point
      * @param some Point in space
      */
    void draw(Point p) {
      apply_fill();
      apply_stroke();
      make_shape(p);
    }


    /** Apply fill
      * Sets the global fill to this Shapes fill info using the fill method
      */
    void apply_fill() {
      switch (fill_info.length) {

        // Grayscale
        case 1:
          fill(fill_info[0]);
          break;

        // Grayscale w/ alpha
        case 2:
          fill(fill_info[0],fill_info[1]);
          break;

        // RGB
        case 3:
          fill(fill_info[0],fill_info[1],fill_info[2]);
          break;

        // RGB w/ alpha
        case 4:
          fill(fill_info[0],fill_info[1],fill_info[2],fill_info[3]);
          break;

        // No fill
        default:
          noFill();
          break;
      }
    }

    /** Apply Stroke
      * Sets the global stroke to this Shapes stroke info using the stroke method
      */
    void apply_stroke() {
      switch (stroke_info.length) {

        // Grayscale
        case 1:
          stroke(stroke_info[0]);
          break;

        // Grayscale w/ alpha
        case 2:
          stroke(stroke_info[0],stroke_info[1]);
          break;

        // RGB
        case 3:
          stroke(stroke_info[0],stroke_info[1],stroke_info[2]);
          break;

        // RGB w/ alpha
        case 4:
          stroke(stroke_info[0],stroke_info[1],stroke_info[2],stroke_info[3]);
          break;

        // No stroke
        default:
          noStroke();
          break;
      }
    }

    /** Clear
      * Sets the stroke and fill to empty
      */
    void clear() {
      set_no_stroke();
      set_no_fill();
    }

  // Technical Methods

    /** Copy
      * Copies the stroke and fill of another Shape
      * @param some Shape
      */
    void copy(Shape s) {
      set_stroke(s.get_stroke());
      set_fill(s.get_fill());
    }

  // Setting fill

    // No fill
    void set_no_fill() {
      this.fill_info = new int[]{};
    }

    // Copy fill
    void set_fill(int[] in_fill) {
      this.fill_info = in_fill;
    }

    // Default fill
    void set_fill() {
      this.fill_info = DEF_FILL;
    }

    // Grayscale
    void set_fill(int b_w) {
      this.fill_info = new int[]{b_w};
    }

    // Grayscale w/ alpha
    void set_fill(int b_w,int alpha) {
      this.fill_info = new int[]{b_w,alpha};
    }

    // RGB
    void set_fill(int r, int g, int b) {
      this.fill_info = new int[]{r,g,b};
    }

    // RGB w/ alpha
    void set_fill(int r, int g, int b, int alpha) {
      this.fill_info = new int[]{r,g,b,alpha};
    }

    // Get fill 
    int[] get_fill() {
      return this.fill_info;
    }


  // Setting stroke
    
    // No stroke
    void set_no_stroke() {
      this.stroke_info = new int[]{};
    }

    // Copy stroke
    void set_stroke(int[] in_stroke) {
      this.stroke_info = in_stroke;
    }

    // Default stroke
    void set_stroke() {
      this.stroke_info = DEF_STROKE;
    }

    // Grayscale
    void set_stroke(int b_w) {
      this.stroke_info = new int[]{b_w};
    }

    // Grayscale w/ alpha
    void set_stroke(int b_w, int alpha) {
      this.stroke_info = new int[]{b_w,alpha};
    }

    // RGB
    void set_stroke(int r, int g, int b) {
      this.stroke_info = new int[]{r,g,b};
    }

    // RGB w/ alpha
    void set_stroke(int r, int g, int b, int alpha) {
      this.stroke_info = new int[]{r,g,b,alpha};
    }

    // Get stroke 
    int[] get_stroke() {
      return this.stroke_info;
    }




  // Getters/Setters
    // Size
    void set_size(float in_size) {
      this.size = in_size < 1 ? this.size : in_size;
    }
    float get_size() {
      return this.size;
    }
}