package scr.models.comvehicle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ComputerVehicle {
    protected double x, y;
    protected int width, height;
    protected double xVelocity, yVelocity;
    protected double xVelocityOld, yVelocityOld;
    protected BufferedImage sliderImage; 

    // Phương thức khởi tạo xe
    public ComputerVehicle(String imagePath) {
        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void check(LinkedList<ComputerVehicle> comVehicles, int j, Boolean roadType) {
        return;
    }

    public void move(double Coe, double SpChange) {
        y += yVelocity * Coe + SpChange;
        x += xVelocity;
    }

    // Phương thức vẽ vật
    public void draw(Graphics g) {
        g.drawImage(sliderImage, (int) x, (int) y, width, height, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }
}