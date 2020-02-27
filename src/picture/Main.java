package picture;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    switch (args[0]) {
      case "invert":
        Process.invert(args[1], args[2]);
        break;
      case "grayscale":
        Process.grayscale(args[1], args[2]);
        break;
      case "rotate":
        Process.rotate(Integer.parseInt(args[1]), args[2], args[3]);
        break;
      case "flip":
        Process.flip(args[1], args[2], args[3]);
        break;
      case "blend":
        // List builder to include all but the head and tail of the input
        List<String> inputpaths = new ArrayList<>();
        for (int i = 1; i < args.length - 1; i++) {
          inputpaths.add(i - 1, args[i]);
        }
        Process.blend(inputpaths, args[args.length - 1]);
        break;
      case "blur":
        Process.blur(args[1], args[2]);
        break;
      case "mosaic":
        // List builder to include all but the head and tail of the input
        List<String> inputps = new ArrayList<>();
        for (int i = 1; i < args.length - 1; i++) {
          inputps.add(i - 1, args[i]);
        }
        Process.mosaic(inputps, args[args.length - 1]);
        break;
    }
  }
}
