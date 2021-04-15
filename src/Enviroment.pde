HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();

void handleKeyPress(int keycode,Boolean pressed) {
  switch (keycode){
    default:
      keys.put(keycode,pressed);
      break;
  }
}

class Enviroment {
  
  Camera cam;
  Enviroment() {
  }
  
  void film() {
    this.cam.film();
  }
  
  HashMap<Integer,Boolean> getUserInput() {
    if (this.allowsUserInput()) {
      return keys;
    } else {
      return null;
    }
  }
  
  private Boolean allowsUserInput() {
    return true;
  }
}
