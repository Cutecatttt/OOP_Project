package scr.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import scr.models.comvehicle.ComputerVehicle;

public class PlayerVehicle {
    private int x, y;
    private int width, height;
    private int xVelocity;
    private BufferedImage sliderImage;
    private String imagePath;

    public PlayerVehicle(String imagePath, int x, int y, int width, int height, int xVelocity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xVelocity = xVelocity;
        this.imagePath = imagePath;
        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveRight() {
        if (x < 700 - width) {
            x += xVelocity;
        }
    }

    public void moveLeft() {
        if (x > 100) {
            x -= xVelocity;
        }
    }

    // Phương thức kiểm tra đi gần chướng ngại vật
    public boolean checkMoveNear(LinkedList<ComputerVehicle> comVehicle) {
        for (int i = 0; i < comVehicle.size(); i++) {
            if (x < comVehicle.get(i).getX() + comVehicle.get(i).getWidth() &&
                    x + width > comVehicle.get(i).getX() &&
                    y - 80 < comVehicle.get(i).getY() + comVehicle.get(i).getHeight() &&
                    y > comVehicle.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    // Phương thức kiểm tra va chạm với chướng ngại vật
    public boolean checkMove(LinkedList<ComputerVehicle> comVehicle, LinkedList<Tree> trees) {
        // Kiểm tra va chạm với xe của người chơi (playerVehicle)
        for (int i = 0; i < comVehicle.size(); i++) {
            if (x + 15 < comVehicle.get(i).getX() + comVehicle.get(i).getWidth() &&
                    x - 15 + width > comVehicle.get(i).getX() &&
                    y + 10 < comVehicle.get(i).getY() + comVehicle.get(i).getHeight() &&
                    y - 10 + height > comVehicle.get(i).getY()) {
                return true;
            }
        }

        // Kiểm tra va chạm với cây (Tree)
        for (int i = 0; i < trees.size(); i++) {
            if (x + 5 < trees.get(i).getX() + trees.get(i).getSize() &&
                    x + width - 5 > trees.get(i).getX() &&
                    y < trees.get(i).getY() + trees.get(i).getSize() &&
                    y + height > trees.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    // Vẽ xe người chơi
    public void draw(Graphics g) {
        if (sliderImage != null) {
            // Vẽ hình ảnh tại vị trí (x, y) và thay đổi kích thước theo width, height
            g.drawImage(sliderImage, x, y, width, height, null);
        } else {
            // Nếu không tải được hình ảnh, vẽ một hình chữ nhật tạm
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
        }
    }

    public void turn(String newimagePath) {
        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(newimagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
