package bla.konishy.photools;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HistImgPnl extends JPanel {

    private File[] imgs;
    private Pnl pnl;
    private HistUI histUi;
    private int idx;

    public HistImgPnl(File dir) throws IOException {
        imgs = dir.listFiles();
        init();
    }

    private void init() throws IOException {
        BufferedImage img = Util.read(imgs[0]);
        idx = 0;
        pnl = new Pnl(img);
        histUi = new HistUI(new Histogram(img, imgs[0].getName()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        histUi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//        add(Box.createRigidArea(new Dimension(20, Integer.MAX_VALUE)));
        add(histUi);
        add(pnl);
        add(Box.createRigidArea(new Dimension(20, Integer.MAX_VALUE)));
    }

    public void showNext() throws IOException {
        BufferedImage img = Util.read(imgs[++idx]);
        pnl.setImg(img);
        histUi.setHistogram(new Histogram(img, imgs[idx].getName()));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.getFont("Label.font");
                UIManager.setLookAndFeel(new DarculaLaf());
                File dir = new File("/home/ekovger/Pictures/Izland2019/temp/jpeg");

                HistImgPnl pane = new HistImgPnl(dir);

                JFrame f = new JFrame();
                f.setContentPane(pane);
                f.setDefaultCloseOperation(3);
                f.setSize(600, 400);
                f.setVisible(true);

                KeyEventDispatcher keyEventDispatcher = e -> {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            try {
                                pane.showNext();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    return false;
                };
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
            } catch (IOException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
    }


}
