package scr.views;
import javax.swing.*;

import scr.main.GameMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterSelectionScreen extends JPanel implements ActionListener {
    private JButton leftArrowButton;
    private JButton rightArrowButton;
    private JButton selectButton;
    private JLabel characterLabel;
    private JFrame frame;
    private static String[] characters = { "T2UH", "T3U" }; // Replace with actual character names or paths
    private static String[] imgPath = { "resources/images/character_t2uh.png", "resources/images/character_t3u.png" };
    private int currentIndex = 0;
    private GameMenu gameMenu;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    private int screenHeight = Math.min(screenSize.height, 1000);

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public CharacterSelectionScreen(JFrame frame, GameMenu gameMenu) {
        this.frame = frame;
        this.gameMenu = gameMenu;

        setLayout(null); // Use null layout for custom positioning

        // Display current character
        characterLabel = new JLabel(characters[currentIndex], SwingConstants.CENTER);
        characterLabel.setFont(new Font("Arial", Font.BOLD, 50));
        characterLabel.setForeground(Color.black); // Text color for visibility
        characterLabel.setBounds(300, 50, 200, 50); // Adjust position and size
        add(characterLabel);

        // Left and Right arrows
        leftArrowButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_left.png"), 66, 66));
        leftArrowButton.setBounds(50, 400, 66, 66); // Adjust position and size
        leftArrowButton.setContentAreaFilled(false);
        leftArrowButton.setBorderPainted(false);
        leftArrowButton.setFocusPainted(false);
        leftArrowButton.addActionListener(this);
        add(leftArrowButton);

        rightArrowButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_right.png"), 66, 66));
        rightArrowButton.setBounds(750 - 66, 400, 66, 66); // Adjust position and size
        rightArrowButton.setContentAreaFilled(false);
        rightArrowButton.setBorderPainted(false);
        rightArrowButton.setFocusPainted(false);
        rightArrowButton.addActionListener(this);
        add(rightArrowButton);

        // Select button
        selectButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_ok2.png"), 88, 66));
        selectButton.setBounds(356, screenHeight - 150, 88, 66); // Adjust position and size
        selectButton.setContentAreaFilled(false);
        selectButton.setBorderPainted(false);
        selectButton.setFocusPainted(false);
        selectButton.addActionListener(this);
        add(selectButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image backgroundImage = new ImageIcon(imgPath[currentIndex]).getImage();
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == leftArrowButton) {
            // Move to the previous character
            currentIndex = (currentIndex - 1 + characters.length) % characters.length;
            updateCharacterDisplay();
        } else if (e.getSource() == rightArrowButton) {
            // Move to the next character
            currentIndex = (currentIndex + 1) % characters.length;
            updateCharacterDisplay();
        } else if (e.getSource() == selectButton) {
            // Confirm the selected character and return to the main menu
            gameMenu.setSelectedCharacterIndex(currentIndex);
            frame.getContentPane().removeAll();
            frame.add(gameMenu);
            frame.revalidate();
        }
        frame.repaint();
    }

    // Update with the character name or image
    private void updateCharacterDisplay() {
        characterLabel.setText(characters[currentIndex]);
    }
}
