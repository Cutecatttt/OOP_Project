package scr.models.comvehicle;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class RightCar extends ComputerVehicle {
    public RightCar (String imagePath) {
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
        this.x = -random.nextInt(150) + 700;
        this.y = -80;
        this.width = 150;
        this.height = 80;
        this.xVelocity = -2;
        this.yVelocity = 2;
    }
}