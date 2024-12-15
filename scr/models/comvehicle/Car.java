package scr.models.comvehicle;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Car extends ComputerVehicle {
    public Car (String imagePath) {
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
        this.x = 165  + random.nextInt(3) * 160 + random.nextInt(75);
        this.y = -150;
        this.width = 80;
        this.height = 150;
        this.xVelocity = 0;
        this.yVelocity = 0.3 + random.nextDouble() * 0.5;
        this.xVelocityOld = this.xVelocity;
        this.yVelocityOld = this.yVelocity;
    }

    @Override
    public void check(LinkedList<ComputerVehicle> comVehicles, int j, Boolean roadType) {
        boolean moveDown = false;

        // dừng trước ngã tư
        if (!roadType) {
            yVelocity = 2;
            return;
        }

        for (int i = 0; i < comVehicles.size(); i++) {
            if (i == j)
                continue;
            ComputerVehicle cV = comVehicles.get(i);
            // nếu có xe ngay trước mặt
            if (x - 5 < cV.getX() + cV.getWidth() &&
                    x + width + 5 > cV.getX() &&
                    y - 50 < cV.getY() + cV.getHeight() &&
                    y + 5 > cV.getY()) {
                if (yVelocity < cV.getYVelocity()) {
                    yVelocity = cV.getYVelocity();
                }
                moveDown = true;
            }
        }

        if (moveDown) {
            xVelocity = 0;
        } else {
            // Trở về vận tốc cũ
            yVelocity = yVelocityOld;
        }
        return;
    }
}

