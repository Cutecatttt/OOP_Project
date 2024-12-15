package scr.utils;
import java.awt.*;
import java.util.Random;

import javax.swing.ImageIcon;

public class Map {
    private int y;
    private int width, height;
    private int velocity;
    private Image img1, img2;
    private String[] imgBGPath = { "resources/images/road2.png", "resources/images/ngatu2.png" };
    private int cntroad, roadtype; // roadtype = 0 -> duong thang, 1 -> nga tu

    public Map() {
        y = 0; width = 800; height = 1000;
        velocity = 2;
        img1 = new ImageIcon(imgBGPath[0]).getImage();
        img2 = new ImageIcon(imgBGPath[1]).getImage();
        cntroad = 2; roadtype = 0;
    }

    public void move(double Coe, double SpChange) {
        y += velocity * Coe + SpChange;
        Random random = new Random();
        if (y > 1000) {
            y = 0;
            if (cntroad >= 4) {
                roadtype = random.nextInt(2);
                if (roadtype == 1)
                    cntroad = 0;
                else
                    cntroad++;
            } else {
                cntroad++;
                roadtype = 0;
            }
        }
    }

    // Phương thức vẽ đường
    public void draw(Graphics g) {
        if (roadtype == 0) {
            g.drawImage(img1, 0, y - 1000, width, height, null);
            if (cntroad == 1)
                g.drawImage(img2, 0, y, width, height, null);
            else
                g.drawImage(img1, 0, y, width, height, null);
        } else {
            g.drawImage(img2, 0, y - 1000, width, height, null);
            g.drawImage(img1, 0, y, width, height, null);
        }
    }

    public int getRoadType() {
        return roadtype;
    }

    public int getY() {
        return y;
    }
}