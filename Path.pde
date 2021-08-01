public class Path {

  // Variables

    // Default
    private int DEF_PATH_SIZE = 10;

    // Instance
    int path_size = DEF_PATH_SIZE;
    int index = 0;
    PVector[] data;

  // Construction
    Path() {init(-1);}

    Path(int in_path_size) {
      init(in_path_size);
    }

    void init(int in_path_size) {
      path_size = in_path_size < 0 ? DEF_PATH_SIZE : in_path_size;

      data = new PVector[path_size];
      for (int i = 0; i < path_size;++i)
        data[i] = null;
      
    }

    void track(PObject obj) {
      data[index++] = obj.get_pos().copy();
      index %= path_size;
    }

    void draw() {
      for (int i = 0; i < path_size; ++i) {
        int actual = (i+index) % path_size;
        PVector pre = data[actual];
        PVector post = data[(actual+1)%path_size];

        if (pre != null && post != null)
          drawLine(pre,post);
      }
    }

}