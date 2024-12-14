package scr.views;
import javax.swing.*;

import scr.main.GameMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseDialog extends JDialog {
    private boolean volumeOn;
    private String lastButtonClicked;

    public PauseDialog(JFrame frame, boolean volumeOn) {
        super(frame, "Tạm ngưng", true);
        setUndecorated(true); // Loại bỏ khung viền
        setSize(500, 400);
        setLocationRelativeTo(frame);

        this.volumeOn = volumeOn;
        // Tạo nội dung giao diện
        setContentPane(new CustomPanel());
    }

    // Panel tùy chỉnh để vẽ giao diện
    private class CustomPanel extends JPanel implements ActionListener {
        private JButton volumeButton;
        private JButton resumeButton;
        private JButton homeButton;

        // Hình ảnh cho các nút
        private ImageIcon volumeOnIcon = new ImageIcon("resources/button/_button_volume_on.png");
        private ImageIcon volumeOffIcon = new ImageIcon("resources/button/_button_volume_off.png");
        private ImageIcon resumeIcon = new ImageIcon("resources/button/_button_continue.png");
        private ImageIcon homeIcon = new ImageIcon("resources/button/button_home.png");

        public CustomPanel() {
            setLayout(null);

            // Nút âm lượng
            volumeButton = createStyledButton(resizeIcon((volumeOn ? volumeOnIcon : volumeOffIcon), 100, 100));
            volumeButton.setBounds(50, 200, 100, 100);
            add(volumeButton);

            // Nút tiếp tục
            resumeButton = createStyledButton(resizeIcon(resumeIcon, 100, 100));
            resumeButton.setBounds(200, 200, 100, 100);
            add(resumeButton);

            // Nút thoát
            homeButton = createStyledButton(resizeIcon(homeIcon, 100, 100));
            homeButton.setBounds(350, 200, 100, 100);
            add(homeButton);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Vẽ nền trong suốt
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Vẽ nhãn "Tạm ngưng"
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("Trò chơi đã tạm ngưng", 100, 150);
        }

        private JButton createStyledButton(ImageIcon icon) {
            JButton button = new JButton(icon);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.addActionListener(this);
            return button;
        }

        private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
            Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == volumeButton) {
                lastButtonClicked = "Volume Button";
                // Chuyển đổi trạng thái âm lượng
                volumeOn = !volumeOn;
                volumeButton
                        .setIcon(volumeOn ? resizeIcon(volumeOnIcon, 100, 100) : resizeIcon(volumeOffIcon, 100, 100));
            } else if (e.getSource() == resumeButton) {
                lastButtonClicked = "Resume Button";
                dispose();
            } else if (e.getSource() == homeButton) {
                lastButtonClicked = "Home Button";
                JDialog parentDialog = (JDialog) SwingUtilities.getWindowAncestor(this);
                JFrame parentFrame = (JFrame) parentDialog.getOwner();
                parentFrame.getContentPane().removeAll();
                parentFrame.add(new GameMenu(parentFrame, volumeOn));
                parentFrame.revalidate();
                parentFrame.repaint();
                volumeOn = false;
                dispose();
            }
        }
    }

    public boolean getVolume() {
        return volumeOn;
    }

    public String getLastButtonClicked() {
        return lastButtonClicked;
    }
}