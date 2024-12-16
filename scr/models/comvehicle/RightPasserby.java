package scr.models.comvehicle;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class RightPasserby extends ComputerVehicle {
    public RightPasserby (String imagePath) {
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
        this.x = random.nextInt(100) + 600;
        this.y = -40;
        this.width = 40;
        this.height = 40;
        this.xVelocity = -1;
        this.yVelocity = 2;
    }

    @Override
    public void check(LinkedList<ComputerVehicle> comVehicles, int j, Boolean roadType) {
        boolean moveDown = false;

        for (int i = 0; i < comVehicles.size(); i++) {
            if (i == j)
                continue;
            ComputerVehicle cV = comVehicles.get(i);
            // nếu có xe ngay trước mặt
            if (x - 20 < cV.getX() + cV.getWidth() &&
                    x + width + 5 > cV.getX() &&
                    y - 5 < cV.getY() + cV.getHeight() &&
                    y + 5 > cV.getY()) {
                moveDown = true;
            }
        }

        if (moveDown) {
            xVelocity = 0;
        } else {
            // Trở về vận tốc cũ
            xVelocity = -1;
        }
        return;
    }
}
