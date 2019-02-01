package bla.konishy.photools;

import com.bulenkov.darcula.DarculaLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class HistUI extends JPanel {

    private static Logger log = LoggerFactory.getLogger(HistUI.class);

    private Histogram histogram;

    private Color[] RGB = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.GRAY};

    public HistUI(Histogram hist) {
        this.histogram = hist;
        setBorder(BorderFactory.createTitledBorder(hist.getTitle()));
    }

    public void setHistogram(Histogram hist) {
        histogram = hist;
        setBorder(BorderFactory.createTitledBorder(hist.getTitle()));
        revalidate();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paintBorder(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth() - 2;
        int barWidth = (int) ((float) w / 256.0);
        int[] reh = calcElemH();
        int elemHeight = reh[1];
        int rounds = reh[0];
        int[] channels = new int[]{3, 0, 1, 2};
        for (int channel : channels) {
            for (int i = 0; i < 256; i++) {
                int val = histogram.valuesForChannel(channel)[i];
                g2.setColor(RGB[channel]);
                if (channel == 3)
                    g2.drawRect(i * barWidth + 2,
                            getHeight() - (val * elemHeight / rounds) - 6,
                            1,
                            (val * elemHeight / rounds));
                else
                    g2.drawOval(i * barWidth + 2,
                            getHeight() - (val * elemHeight / rounds) - 6, 2, 2);
//                g2.setColor(Color.BLACK);
//                g2.drawRect(i * barWidth,
//                        getHeight() - (val * elemHeight / rounds),
//                        barWidth,
//                        (val * elemHeight / rounds));
            }
        }
    }

    private int[] calcElemH() {
        float max = (float) histogram.getMax();
        float h = (float) (getHeight() * .9);
        int elemHeight = (int) (h / max);
        int rounds = 1;
        while (elemHeight == 0) {
            float newMax = max / rounds;
            elemHeight = (int) (h / newMax);
            rounds++;
        }
        log.info("max(h) = {}, h(pnl) = {}, h(elem) = {}, rounds = {}.", max, h, elemHeight, rounds);
        return new int[]{rounds, elemHeight};
    }

}
