package scr.views;
import javax.swing.*;

import scr.main.GameMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverScreen extends JPanel implements ActionListener {
    private JButton mainMenuButton;
    private JButton exitButton;
    private JButton retryButton;
    private JFrame frame;
    private Image backgroundImage; // Background image
    private static String[] imgPath = { "resources/images/End.png", "resources/images/End2.png" };
    private GameMenu GMenu;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    private int screenHeight = Math.min(screenSize.height, 1000);

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    boolean volumeOn;

    public GameOverScreen(JFrame frame, int score, GameMenu gMenu) {
        this.frame = frame;
        this.GMenu = gMenu;
        this.volumeOn = gMenu.volumeOn;

        // Load the background image
        backgroundImage = new ImageIcon(imgPath[gMenu.getCId()]).getImage();

        setLayout(null); // Custom layout for absolute positioning

        // Display score
        JLabel scoreLabel = new JLabel("SCORE: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(200, 150, 400, 50); // Adjust position and size
        add(scoreLabel);

        // Main menu button
        mainMenuButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_gamemenu.png"), 235, 55));
        mainMenuButton.setBounds(425, screenHeight - 150, 235, 55);// Adjust position and size
        mainMenuButton.setContentAreaFilled(false);
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.addActionListener(this);
        add(mainMenuButton);

        // Retry button
        retryButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_newgame.png"), 235, 55));
        retryButton.setBounds(140, screenHeight - 150, 235, 55); // Adjust position and size
        retryButton.setContentAreaFilled(false);
        retryButton.setBorderPainted(false);
        retryButton.setFocusPainted(false);
        retryButton.addActionListener(this);
        add(retryButton);

        // Exit button
        exitButton = new JButton(resizeIcon(new ImageIcon("resources/button/button_gameover.png"), 376, 88));
        exitButton.setBounds(212, 50, 376, 88); // Adjust position and size
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(true);
        exitButton.addActionListener(this);
        add(exitButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainMenuButton) {
            // Return to main menu
            frame.getContentPane().removeAll();
            frame.add(new GameMenu(frame, volumeOn));
            frame.revalidate();
            frame.repaint();
        } else if (e.getSource() == exitButton) {
            // Exit the game
            System.exit(0);
        } else if (e.getSource() == retryButton) {
            // Retry the game
            frame.getContentPane().removeAll();
            RunGame game = new RunGame(frame, GMenu);
            frame.add(game);
            frame.revalidate();
            frame.repaint();
            game.requestFocusInWindow();
        }
    }
}
