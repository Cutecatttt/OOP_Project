package scr.models.comvehicle;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Motorbike extends ComputerVehicle {
    public Motorbike (String imagePath) {
        super(imagePath);
        setup();
        try {
            // Tải hình ảnh từ tệp tin
            sliderImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        Random random = new Random();
        this.x = 180 + random.nextInt(10) * 44;
        this.y = 1000;
        this.width = 60;
        this.height = 100;
        this.xVelocity = 0;
        this.yVelocity = -2 + random.nextDouble();
        this.xVelocityOld = this.xVelocity;
        this.yVelocityOld = this.yVelocity;
    }

    @Override
    public void check(LinkedList<ComputerVehicle> comVehicles, int j, Boolean roadType) {
        boolean moveDown = false;
        boolean moveLeft = false;
        boolean moveRight = false;

        // Dừng trước ngã tư
        if (!roadType) {
            yVelocity = 2;
            xVelocity = 0;
            return;
        }

        double yV = 0;
        for (int i = 0; i < comVehicles.size(); i++) {
            if (i == j)
                continue;
            // Nếu phía trước có xe
            ComputerVehicle cV = comVehicles.get(i);
            if (y < cV.getY() + cV.getHeight() + 150 && y > cV.getY()) {
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
            
            //Nếu xe đó quá gần
            if (y < cV.getY() + cV.getHeight() + 10 &&
                    y > cV.getY() + cV.getHeight() &&
                    x < cV.getX() + cV.getWidth() &&
                    x + width > cV.getX()) {
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
            xVelocity = -3;
            yVelocity = -0.625;
        } else if (moveRight) {
            xVelocity = 3;
            yVelocity = -0.625;
        } else if (xVelocity != 0 && yVelocity >= -0.625) {
                xVelocity = 0;
                yVelocity = yVelocityOld;
        }
    }

}
