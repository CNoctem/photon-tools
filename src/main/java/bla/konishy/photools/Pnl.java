package bla.konishy.photools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class Pnl extends JPanel {

    private static Logger log = LoggerFactory.getLogger(Pnl.class);

    private Image image;
    private Image currentImage;

    public Pnl(Image img) {
        image = img;
        currentImage = img;
        ResizeTimer timer = new ResizeTimer(this);
        timer.start();
    }

    public void setImg(Image img) {
        image = img;
        currentImage = img;
        doResize();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(currentImage,
                (getWidth() - currentImage.getWidth(null)) / 2,
                (getHeight() - currentImage.getHeight(null)) / 2,
                null);
    }

    public void doResize() {
        currentImage = Util.resizeBestFit(image, getSize());
        revalidate();
        repaint();
    }

    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(3);
        Image i = ImageIO.read(new File("/home/ekovger/Pictures/Izland2019/temp/jpeg/DSC_0210.jpg"));
        f.setContentPane(new Pnl(i));

        f.setSize(800, 600);
        f.setVisible(true);
    }

}

class ResizeTimer extends Thread {

    private int waitTime = 10;
    private int nWait = 2;
    private Pnl owner;

    private boolean timerRunning;
    private boolean countdownActive;

    public ResizeTimer(Pnl owner) {
        this.owner = owner;
        owner.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                startOrRestart();
            }
        });
        timerRunning = true;
    }

    public synchronized void stopThread() {
        timerRunning = false;
    }

    private void startOrRestart() {
        countdownActive = true;
        nWait = 10;
    }

    @Override
    public void run() {
        while(timerRunning) {
            if (countdownActive) nWait--;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (nWait == 0) owner.doResize();
        }
    }

}
