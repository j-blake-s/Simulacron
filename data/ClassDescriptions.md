# Class Descriptions

__File Name__ | _File Type_ | <extends_implements> __File(s)__ | <depends_on> __File(s)__

## Interfaces

*__PObject.pde__* | _Interface_

  > * Keystone of the physics engine
  > * Enforces position, velocity, acceleration, and mass on all subclasses.

*__Drawable.pde__* | _Interface_ | extends *__PObject.pde__*

  > * Keystone of the GUI
  > * Enforces ability to be drawn on all subclasses

## Classes

__Environment.pde__ | _Class_ | depends on *__CameraRig.pde__*

  > * Holds all of the contextual information for and faciliates the current scene's execution
  > * Has a CameraRig object which is used to display the scene

*__Particle.pde__* | _Class_ | implements *__Drawable.pde__*

  > * Implements all of the methods from *__PObject__* and *__Drawable__*
  > * Contains 1 abstract method:
  >> * draw() _Draws this Particle to the screen_

__Ball.pde__ | _Class_ | extends *__Particle.pde__*

  > * Displays a sphere to the screen at its location
  > * Subject to gravity

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

__FreeCameraRig.pde__ | _Class_ | extends *__CameraRig.pde__* | depends __Camera__

  > * Controlled via user input to give free range movement
  > * WASD for horizontal, SPACE + LFT CTRL for up/down, RT_MOUSE_BTN for looking left/right

## Method Files

*__graphics.pde__* | _Method File_

  > * Complex Graphical methods

*__math.pde__* | _Method File_

  > * Generic mathematical methods

*__helpers.pde__* | _Method File_
  
  > * Simple methods which provide extra functionality to Processing methods

*__PEngine.pde__* | _Method File_ | depends on *__PObject.pde__*
  
  > * Applies forces to PObjects

*__input.pde__* | _Method File_
  
  > * Methods which relate to user input such as mouse clicks and key presses

## Variable Files

*__settings.pde__* | _Variable File_

  > * Contains global variables
