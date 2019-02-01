package bla.konishy.photools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {

    private static Logger log = LoggerFactory.getLogger(Util.class);

    public static int[] int2argb(int val) {
        int r = (val>>16)&0xFF;
        int g = (val>>8)&0xFF;
        int b = (val)&0xFF;
        int lumi = (int) (0.2126 * (float)r + 0.7152 * (float)g + 0.0722 * (float)b);
        return new int[] {r,g,b, lumi};
    }

    public static BufferedImage read(File file) throws IOException {
        return toBufferedImage(ImageIO.read(file));
    }

    public static BufferedImage read(String filename) throws IOException {
        return read(new File(filename));
    }

    public static BufferedImage toBufferedImage(Image img) {
        BufferedImage bimg = new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
        bimg.getGraphics().drawImage(img, 0, 0, null);
        return bimg;
    }

    public static Image resizeSetWidth(Image original, int newWidth) {
        float a = getAspect(original);
        int newHeight = (int) ((float)newWidth / a);
        return resize(original, newWidth, newHeight);
    }

    public static Image resizeSetHeight(Image original, int newHeight) {
        float a = getAspect(original);
        int newWidth = (int) (a * newHeight);
        return resize(original, newWidth, newHeight);
    }

    public static Image resizeBestFit(Image original, Dimension wrapperSize) {
        float wa = getAspect(wrapperSize.width, wrapperSize.height);
        float ia = getAspect(original);
        if (wa > 1) {
            if (ia > 1) return resizeSetHeight(original, wrapperSize.height);
            else resizeSetWidth(original, wrapperSize.width);
        } else {
            if (ia > 1) return resizeSetWidth(original, wrapperSize.width);
            else return resizeSetHeight(original, wrapperSize.height);
        }
        throw new IllegalArgumentException("How???");
    }

    public static Image resize(Image original, int w, int h) {
        long t0 = System.nanoTime();
        Image img = original.getScaledInstance(w, h, BufferedImage.SCALE_FAST);;
        log.info("t(resize) = {}", (System.nanoTime() - t0));
        return img;
    }

    public static float getAspect(Image img) {
        return getAspect(img.getWidth(null), img.getHeight(null));
    }

    public static float getAspect(int w, int h) {
        return (float) w / (float) h;
    }

}
