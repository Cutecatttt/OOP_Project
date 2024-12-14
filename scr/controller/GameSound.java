package scr.controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameSound {
    private Clip clip;

    public GameSound(String filePath) {
        try {
            // Mở tệp âm thanh và đọc vào AudioInputStream
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Tạo Clip và mở luồng âm thanh vào đó
            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Phương thức để bắt đầu phát nhạc nền
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    // Phương thức để dừng nhạc nền
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}