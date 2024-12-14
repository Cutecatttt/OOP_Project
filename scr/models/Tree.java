package scr.models;

import java.awt.*;

import javax.swing.ImageIcon;

public class Tree {
    private int x, y;
    private int size;
    private int velocity;
    private Image sliderImage;
    private String imgBGPath = "resources/images/tree0.png";

    public Tree(int x, int y) {
        this.x = x;
        this.y = y;
        size = (int) (Math.random() * 10) + 40;
        if (x > 500)
            this.x -= size;
        velocity = 2;
        sliderImage = new ImageIcon(imgBGPath).getImage();
    }

    public void move(double Coe, double SpCoe) {
        y += (velocity * Coe) * SpCoe;
    }

    // Phương thức vẽ cây
    public void draw(Graphics g) {
        g.drawImage(sliderImage, (int) x - size, (int) y - size, size * 3, size * 3, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}