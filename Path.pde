public class Path {

  // Variables

    // Default
    private int DEF_PATH_SIZE = 10;

    // Instance
    int path_size = DEF_PATH_SIZE;
    int index = 0;

  // Construction
    Path() {}

    Path(int in_path_size) {
      init(in_path_size);
    }

    void init(int in_path_size) {
      path_size = in_path_size < 0 ? DEF_PATH_SIZE : in_path_size;
    }

}