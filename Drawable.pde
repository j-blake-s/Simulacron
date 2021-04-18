public interface Drawable extends PObject {

  /**
    * Draws the object to the canvas
    */
  void draw();

  /**
    * Brings this object out of hiding and allows it to be drawn to the canvas
    */
  void show();

  /**
    * Hides this object by not letting it be drawn to the canvas
    */
  void hide();

  /**
    * Checks whether this object is hidden or not
    */
  boolean isHidden();

  /** 
    * Disables the stroke feature for this object
    */
  void setNoStroke();
  
  /**
    * Stores the Black White value for use later
    */
  PVector setStroke(int black_white);
  
  /**
    * Stores the RGB value for use later
    */
  PVector setStroke(int r, int g, int b);

  /**
    * Disables the fill feature for this object
    */
  void setNoFill();

  /**
    *  Stores the Black White value for use later
    */
  PVector setFill(int black_white);

  /**
    * Stores the RGB value for use later
    */
  PVector setFill(int r, int g, int b);

}