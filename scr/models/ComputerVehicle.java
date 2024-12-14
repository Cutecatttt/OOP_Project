package scr.models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class ComputerVehicle {
    private double x, y;
    private int width, height;
    private double xVelocity, yVelocity;
    private double xVelocityOld, yVelocityOld;
    private int type;
    private BufferedImage sliderImage; // thêm 1 biến hình ảnh lúc sang trái hoặc sang phải với xe máy

    // Phương thức khởi tạo xe
    public ComputerVehicle(int type, String imagePath) {
        Random random = new Random();
        double x;
        double v;
        if (type == 0) {
            // xe máy
            x = 180 + random.nextInt(10) * 44;
            v = -2 + random.nextDouble();
            reset(x, 1000, 60, 100, 0, v, 0);
        } else if (type == 1) {
            // oto đi thẳng
            x = 165  + random.nextInt(3) * 160 + random.nextInt(75);
            v = 0.3 + random.nextDouble() * 0.5;
            reset(x, -150, 80, 150, 0, v, 1);
        } else if (type == 2) {
            // người qua đường bên trái
            x = random.nextInt(50) + 125;
            reset(x, -50, 40, 40, 1, 2, 2);
        } else if (type == 3) {
            // người qua đường bên phải
            x = random.nextInt(50) + 625;
            reset(x, -50, 40, 40, -1, 2, 2);
        } else if (type == 4) {
            // cảnh sát
            double t = random.nextInt(2) - 0.5;
            x = random.nextInt(50) + 375 - 400 * t;
            reset(x, -50, 40, 60, 0, 2, 2);
        } else if (type == 5) {
            // oto qua đường từ bên phải
            x = random.nextInt(50) + 625;
            reset(x, -80, 150, 80, -2, 2, 3);
        } else if (type == 6) {
            // oto qua đường từ bên trái
            x = random.nextInt(50) + 125;
            reset(x, -80, 150, 80, 2, 2, 4);
        }

        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức thay đổi giá trị thuộc tính
    public void reset(double x, double y, int width, int height, double xVelocity, double yVelocity, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.xVelocityOld = xVelocity;
        this.yVelocityOld = yVelocity;
        this.type = type;
    }

    // Phương thức kiểm tra va chạm của ComputerVehicle
    public void check(LinkedList<ComputerVehicle> comVehicles, int j, Boolean roadType) {
        if (type == 4 || type == 5 || type == 6)
            return;
        Random random = new Random();
        boolean moveDown = false;
        boolean moveLeft = false;
        boolean moveRight = false;

        // dừng trước ngã tư với xe đi thẳng
        if (!roadType && type < 2) {
            yVelocity = 2;
            return;
        }

        // kiểm tra va chạm với người đi ngang và xe oto
        if (type != 0) {
            for (int i = 0; i < comVehicles.size(); i++) {
                if (i == j)
                    continue;
                ComputerVehicle cV = comVehicles.get(i);
                // nếu i ở ngay trước j
                if (x - 5 < cV.getX() + cV.getWidth() &&
                        x + width + 5 > cV.getX() &&
                        y - 20 < cV.getY() + cV.getHeight() &&
                        y + 5 > cV.getY()) {
                    if (type == 1 && yVelocity < cV.getYVelocity()) {
                        yVelocity = cV.getYVelocity();
                    }
                    moveDown = true;
                }
            }

            if (moveDown) {
                xVelocity = 0;
            } else {
                // trở về vận tốc cũ
                if (type == 1)
                    yVelocity = yVelocityOld;
                else
                    xVelocity = xVelocityOld;
            }
            return;
        }

        // Phương thức di chuyển với xe máy
        double yV = 0;
        for (int i = 0; i < comVehicles.size(); i++) {
            if (i == j)
                continue;

            ComputerVehicle cV = comVehicles.get(i);
            if (y < cV.getY() + cV.getHeight() + 120 && y > cV.getY() && type == 0) {
                if (x - 5 < cV.getX() && x + width + 5 > cV.getX()) {
                    moveLeft = true;
                    yV = Math.max(yV, cV.getYVelocity());
                } else if (x - 5 < cV.getX() + cV.getWidth() && x + width > cV.getX() + cV.getWidth()) {
                    moveRight = true;
                    yV = Math.max(yV, cV.getYVelocity());
                } else if (x >= cV.getX() && x + width <= cV.getX() + cV.getWidth()) {
                    if (x > 400)
                        moveLeft = true;
                    else
                        moveRight = true;
                    yV = Math.max(yV, cV.getYVelocity());
                }
            }

            if (y < cV.getY() + cV.getHeight() + 10 &&
                    y > cV.getY() + cV.getHeight() &&
                    x < cV.getX() + cV.getWidth() &&
                    x + width > cV.getX() && type == 0) {
                moveDown = true;
            }
        }
        if (x + width > 580)
            moveLeft = true;
        if (x < 180)
            moveRight = true;

        if (moveDown || (moveLeft && moveRight)) {
            yVelocity = yV;
            xVelocity = 0;
        } else if (moveLeft) {
            xVelocity = -1.8;
            yVelocity = -0.5;
            /////// thay hình ảnh
        } else if (moveRight) {
            xVelocity = 1.8;
            yVelocity = -0.5;
            //////// thay hình ảnh
        } else {
            if (xVelocity != 0 && yVelocity >= -0.5) {
                xVelocity = 0;
                yVelocity = -2 + random.nextDouble();
                /////// Quay trờ lại hình cũ
            }
        }
    }

    public void move(double Coe, double SpChange) {
        if (type == 0) {
            if (x < 180 && x + width < 580) {
                xVelocity = 0;
            }
        }
        y += yVelocity * Coe + SpChange;
        x += xVelocity;
    }

    // Phương thức vẽ vật
    public void draw(Graphics g) {
        g.drawImage(sliderImage, (int) x, (int) y, width, height, null);
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
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

    public int getType() {
        return type;
    }
}