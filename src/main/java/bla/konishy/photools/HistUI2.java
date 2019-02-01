package bla.konishy.photools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class HistUI2 extends JPanel {

    private static Logger log = LoggerFactory.getLogger(HistUI2.class);

    private Histogram histogram;

    private Color[] RGB = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.GRAY};

    public HistUI2(Histogram hist) {
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
    public void paint(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        int w = getWidth() - 2;
        int barWidth = (int) ((float) w / 256.0);
        int[] reh = calcElemH();
        int elemHeight = reh[1];
        int rounds = reh[0];

        for (int i = 0; i < 256; i++) {
            int r = histogram.valuesForChannel(0)[i];
            int g = histogram.valuesForChannel(1)[i];
            int b = histogram.valuesForChannel(2)[i];
            int lum = histogram.valuesForChannel(3)[i];
            drawBar(g2, r, g, b, i* barWidth, barWidth, (float)elemHeight / (float)rounds);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(i * barWidth,
                    getHeight() - (lum * elemHeight / rounds),
                    barWidth,
                    (lum * elemHeight / rounds));

        }
    }

    private void drawBar(Graphics2D g2, int r, int g, int b, int x, int w, float elemH) {
        int max = Math.max(Math.max(r, g), b);
        int sec;
        if (max == r) {
            g2.setColor(Color.RED);
            sec = Math.max(g, b);
        } else if (max == g) {
            g2.setColor(Color.GREEN);
            sec = Math.max(r, b);
        } else {
            g2.setColor(Color.BLUE);
            sec = Math.max(r, g);
        }
        g2.drawRect(x, getHeight() - (int)(max * elemH), w, (int)(max * elemH));
        g2.setColor(Color.GRAY);
        g2.drawRect(x, getHeight() - (int)(sec * elemH), w, (int)(sec * elemH));
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
