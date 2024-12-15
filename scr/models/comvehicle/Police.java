package scr.models.comvehicle;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Police extends ComputerVehicle {
    public Police (String imagePath) {
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
        this.x = random.nextInt(50) + 375 - 400 * (random.nextInt(2) - 0.5);
        this.y = -50;
        this.width = 50;
        this.height = 50;
        this.xVelocity = 0;
        this.yVelocity = 2;
    }
}
