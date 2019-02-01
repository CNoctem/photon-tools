package bla.konishy.photools;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Histogram {

    private BufferedImage image;
    private int[][] values = new int[4][256];
    private int max = 0;
    private String title;

    public Histogram(BufferedImage img, String title) {
        image = img;
        this.title = title;
        init();
        readImage();
    }

    public String getTitle() {
        return title;
    }

    public int[] valuesForChannel(int channel) {
        return values[channel];
    }

    public int getMax() {
        return max;
    }

    private void init() {
        for (int c = 0; c < 3; c++)
            for (int v = 0; v < 256; v++)
                values[c][v] = 0;
    }

    private void readImage() {
        int w = image.getWidth();
        int h = image.getHeight();
        for (int r = 0; r < w; r++) {
            for (int c = 0; c < h; c++) {
                int pixel[] = Util.int2argb(image.getRGB(r, c));
                for (int v = 0; v < 4; v++) {
                    int value = values[v][pixel[v]];
                    if (value > max) max = value;
                    values[v][pixel[v]] = value + 1;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.getFont("Label.font");
                UIManager.setLookAndFeel(new DarculaLaf());
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(3);
                f.setContentPane(new JLabel("NewLabel"));
                f.pack();
                f.setVisible(true);
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
    }


}

/*
Luminance (standard for certain colour spaces): (0.2126*R + 0.7152*G + 0.0722*B) [1]
Luminance (perceived option 1): (0.299*R + 0.587*G + 0.114*B) [2]
Luminance (perceived option 2, slower to calculate): sqrt( 0.241*R^2 + 0.691*G^2 + 0.068*B^2 ) → sqrt( 0.299*R^2 + 0.587*G^2 + 0.114*B^2 )
 */
