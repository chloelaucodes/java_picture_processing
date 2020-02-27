package picture;

import com.google.common.base.Preconditions;
import java.util.List;
import static picture.Utils.*;

public class Process {

  /**
   * @param inputpath the path where the input is from
   * @param outputpath the path where the file is exported to
   */
  public static void invert(String inputpath, String outputpath) {
    final int colorSize = 255;
    Picture picture = loadPicture(inputpath);
    for (int i = 0; i < picture.getWidth(); i++) {
      for (int j = 0; j < picture.getHeight(); j++) {
        int red = picture.getPixel(i, j).getRed();
        int green = picture.getPixel(i, j).getGreen();
        int blue = picture.getPixel(i, j).getBlue();
        // reversing the colours
        int newRed = colorSize - red;
        int newGreen = colorSize - green;
        int newBlue = colorSize - blue;

        Color color = picture.getPixel(i, j);
        color.setRed(newRed);
        color.setGreen(newGreen);
        color.setBlue(newBlue);
        picture.setPixel(i, j, color);
      }
    }
    savePicture(picture, outputpath);
  }

  /**
   * @param inputpath the path where the input is from
   * @param outputpath the path where the file is exported to
   */
  public static void grayscale(String inputpath, String outputpath) {
    Picture picture = loadPicture(inputpath);
    for (int i = 0; i < picture.getWidth(); i++) {
      for (int j = 0; j < picture.getHeight(); j++) {
        int red = picture.getPixel(i, j).getRed();
        int green = picture.getPixel(i, j).getGreen();
        int blue = picture.getPixel(i, j).getBlue();

        int avg = (red + green + blue) / 3;
        // taking the average colour to produce a grayscale
        Color color = picture.getPixel(i, j);
        color.setRed(avg);
        color.setGreen(avg);
        color.setBlue(avg);
        picture.setPixel(i, j, color);
      }
    }
    savePicture(picture, outputpath);
  }

  /**
   * @param angle angle to rotate the image by, in the option of 9, 180 and 270
   * @param inputpath the path where the input is from
   * @param outputpath the path where the file is exported to
   */
  public static void rotate(int angle, String inputpath, String outputpath) {
    Preconditions.checkArgument(angle == 90 || angle == 180 || angle == 270, "angle not valid");
    // ensures angle is 90, 180 or 270
    Picture pic = loadPicture(inputpath);
    switch (angle) {
        // It could be possible to apply 90 degree turn to the other cases
        // but it deemed less efficient as you would have to loop through
        // multiple times
      case 90:
        Picture rPic = createPicture(pic.getHeight(), pic.getWidth());
        for (int i = 0; i < pic.getWidth(); i++) {
          for (int j = 0; j < pic.getHeight(); j++) {

            Color color = pic.getPixel(i, j);
            // rotating to the right 90 degs would simply shift the
            // horizontal to the vertical and the vertical flipped
            // then mapped to horizontal
            int newi = pic.getHeight() - 1 - j;
            int newj = i;

            rPic.setPixel(newi, newj, color);
          }
        }
        savePicture(rPic, outputpath);
        break;

      case 180:
        Picture fPic = createPicture(pic.getWidth(), pic.getHeight());
        for (int i = 0; i < pic.getWidth(); i++) {
          for (int j = 0; j < pic.getHeight(); j++) {

            Color color = pic.getPixel(i, j);
            // turning 180 degs is just flipping the vertical index
            // and the horizontal index
            int newi = pic.getWidth() - 1 - i;
            int newj = pic.getHeight() - 1 - j;

            fPic.setPixel(newi, newj, color);
          }
        }
        savePicture(fPic, outputpath);
        break;

      case 270:
        Picture lPic = createPicture(pic.getHeight(), pic.getWidth());
        for (int i = 0; i < pic.getWidth(); i++) {
          for (int j = 0; j < pic.getHeight(); j++) {

            Color color = pic.getPixel(i, j);
            // rotating to the right 270 degs would simply shift be
            // equal to shifting to the left 90 degs so the vertical
            // shifted to the horizontal and horizontal flipped then
            // mapped to vertical
            int newi = j;
            int newj = pic.getWidth() - 1 - i;

            lPic.setPixel(newi, newj, color);
          }
        }
        savePicture(lPic, outputpath);
        break;
    }
  }

  /**
   * @param dir direction of flip, in either (H)orizontal or (V)ertical
   * @param inputpath the path where the input is from
   * @param outputpath the path where the file is exported to
   */
  public static void flip(String dir, String inputpath, String outputpath) {
    Picture picture = loadPicture(inputpath);
    Picture pic = createPicture(picture.getWidth(), picture.getHeight());
    switch (dir) {
      case "H":
        for (int i = 0; i < picture.getWidth(); i++) {
          for (int j = 0; j < picture.getHeight(); j++) {

            Color color = picture.getPixel(i, j);
            // horizontal flips simply flip the horizontal indices
            // and maintain the vertical
            int flipi = picture.getWidth() - 1 - i;
            int flipj = j;
            // flipj is a useless assignment but it makes the
            // coordinates clearer

            pic.setPixel(flipi, flipj, color);
          }
        }
       savePicture(pic, outputpath);
        break;
      case "V":
        for (int i = 0; i < picture.getWidth(); i++) {
          for (int j = 0; j < picture.getHeight(); j++) {

            Color color = picture.getPixel(i, j);
            // similarly vertical flips simply flip the vertiical
            // indices and maintain the horizontal
            int flipi = i;
            // flipi is a useless assignment but it makes the
            // coordinates clearer
            int flipj = picture.getHeight() - 1 - j;

            pic.setPixel(flipi, flipj, color);
          }
        }
        savePicture(pic, outputpath);
        break;
    }
  }

  /**
   * @param inputpaths the path where the inputs are from
   * @param outputpath the path where the file is exported to
   */
  public static void blend(List<String> inputpaths, String outputpath) {
    Picture picture = loadPicture(inputpaths.get(0));
    int minWidth = picture.getWidth();
    int minHeight = picture.getHeight();
    for (String inputpath : inputpaths) {
      Picture pic = loadPicture(inputpath);
      // obtaining the min width
      if (pic.getWidth() < minWidth) {
        minWidth = pic.getWidth();
      }
      // obtaining the min height
      if (pic.getHeight() < minHeight) {
        minHeight = pic.getHeight();
      }
    }
    Picture result = createPicture(minWidth, minHeight);
    // this creates a proper canvas with the min size bounded by the given
    // files in the array
    for (int i = 0; i < minWidth; i++) {
      for (int j = 0; j < minHeight; j++) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int counter = 0;
        for (String inputpath : inputpaths) {
          Picture pic = loadPicture(inputpath);
          Color color = pic.getPixel(i, j);
          // summing the colours to obtain mean for blend
          sumRed += color.getRed();
          sumGreen += color.getGreen();
          sumBlue += color.getBlue();
          counter++;
        }
        // calculating the means
        int avgRed = sumRed / counter;
        int avgGreen = sumGreen / counter;
        int avgBlue = sumBlue / counter;

        Color color = picture.getPixel(i, j);
        color.setRed(avgRed);
        color.setGreen(avgGreen);
        color.setBlue(avgBlue);

        result.setPixel(i, j, color);
      }
    }
    savePicture(result, outputpath);
  }

  /**
   * @param inputpath the path where the input is from
   * @param outputpath the path where the file is exported to
   */
  public static void blur(String inputpath, String outputpath) {
    Picture picture = loadPicture(inputpath);
    Picture pic = createPicture(picture.getWidth(), picture.getHeight());
    for (int i = 1; i < picture.getWidth() - 1; i++) {
      for (int j = 1; j < picture.getHeight() - 1; j++) {
        // produces blur for non-edge pixels by collecting the side pixel's
        // RGB values and finding the mean of each colour
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int counter = 0;
        for (int x = i - 1; x < (i + 2); x++) {
          for (int y = j - 1; y < (j + 2); y++) {
            redSum += picture.getPixel(x, y).getRed();
            greenSum += picture.getPixel(x, y).getGreen();
            blueSum += picture.getPixel(x, y).getBlue();
            counter++;
          }
        }
        int avgRed = redSum / counter;
        int avgGreen = greenSum / counter;
        int avgBlue = blueSum / counter;
        Color color = picture.getPixel(i, j);
        color.setRed(avgRed);
        color.setBlue(avgBlue);
        color.setGreen(avgGreen);
        pic.setPixel(i, j, color);
      }
    }
    for (int x = 0; x < picture.getWidth() - 1; x++) {
      // initialising the top and bottom rows
      Color top = picture.getPixel(x, 0);
      Color bottom = picture.getPixel(x, picture.getHeight() - 1);
      pic.setPixel(x, 0, top);
      pic.setPixel(x, picture.getHeight() - 1, bottom);
    }

    for (int y = 0; y < picture.getHeight(); y++) {
      // initialising the left and right columns
      Color left = picture.getPixel(0, y);
      Color right = picture.getPixel(picture.getWidth() - 1, y);
      pic.setPixel(0, y, left);
      pic.setPixel(picture.getWidth() - 1, y, right);
    }
    savePicture(pic, outputpath);
  }

  /**
   * @param inputps the path where the inputs are from
   * @param outputpath the path where the file is exported to
   */
  public static void mosaic(List<String> inputps, String outputpath) {
    Picture picture = loadPicture(inputps.get(0));
    int size = inputps.size();
    int min = 0;
    int minWidth = picture.getWidth();
    int minHeight = picture.getHeight();
    for (String inputp : inputps) {
      Picture pic = loadPicture(inputp);
      // obtaining the min width
      if (pic.getWidth() < minWidth) {
        minWidth = pic.getWidth();
      }
      // obtaining the min height
      if (pic.getHeight() < minHeight) {
        minHeight = pic.getHeight();
      }
    }
    if (minHeight < minWidth){
      min = minHeight;
    } else {
      min = minWidth;
    }
    Picture result = createPicture(min, min);

    for (int i = 0; i < min; i++) {
      for (int j = 0; j < min; j++) {
          // this allows the pixels to alternatively horizontally
          Color color = loadPicture(inputps.get((((i + j - 2) % size) + size) % size)).getPixel(i, j);
          result.setPixel(i, j, color);
      }
    }
    savePicture(result, outputpath);
  }
}