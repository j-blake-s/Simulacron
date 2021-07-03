# Class Descriptions

__File Name__ | _File Type_ | <extends_implements> __File(s)__ | <depends_on> __File(s)__

## Non-Initalized Classes

*__PObjectBase__* | _Abstract Class_ | depends on *__Shape__*, *__State__*
  
  > * Forms the base of PObject
  > * Defines basic methods, i.e getters, setters, etc.

*__Shape__* | _Interface_ | 

  > * The different shapes that PObjects can take
  > * Draws the shape to the screen

*__State__* | _Interface_ |

  > * UMIMPLEMENTED
  > * Plan for it to determine the functionality of the POBject

## Classes

__PObject__ | _Class_ | extends *__PObjectBase__* | depends on *__Shape__*, *__State__*

  > * Keystone of the program
  > * PObjects have a moddable shape which controls how they look
  > * Can be subject to forces

__Ball__ | _Class_ | implements *__Shape__*

  > * Draws a sphere of different radii to the screen
  > * Radius is passed to this as parameter
  > * Requries PVector denoting position as parameter for drawing

__Environment.pde__ | _Class_ | depends on *__CameraRig.pde__*

  > * Holds all of the contextual information for and faciliates the current scene's execution
  > * Has a CameraRig object which is used to display the scene

__Camera.pde__ | _Class_

  > * Maintains the eye, center, and up vectors for itself.
  > * Calls the Processing::camera() function to film itself.

*__CameraRig.pde__* | _Class_ | depends on __Camera.pde__

  > * Able to attach and detach different __Camera__ objects to itself.
  > * Maintains eye, center, and up vectors for itself.
  > * Changes in positional information are also applied to any attached __Camera__ object.
  > * Contains 2 abstract methods
  >> * move() _Defines how the CameraRig moves each frame_
  >> * instructions() _Shows instructions for using the CameraRig to the screen._

__FreeCameraRig.pde__ | _Class_ | extends *__CameraRig.pde__* | depends on __Camera__ 

  > * Controlled via user input to give free range movement
  > * WASD for horizontal, SPACE + LFT CTRL for up/down
  > * Drag mouse for looking around
  
## Method Files

*__graphics.pde__* | _Method File_

  > * Complex Graphical methods

*__math.pde__* | _Method File_

  > * Generic mathematical methods

*__helpers.pde__* | _Method File_
  
  > * Simple methods which provide extra functionality to Processing methods


*__input.pde__* | _Method File_
  
  > * Methods which relate to user input such as mouse clicks and key presses

## Variable Files

*__settings.pde__* | _Variable File_

  > * Contains global variables
