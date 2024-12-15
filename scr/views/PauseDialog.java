package scr.views;

import javax.swing.*;
import scr.main.GameMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseDialog extends JDialog {
    private boolean volumeOn;
    private String lastButtonClicked;
    private GameMenu gMenu;

    public PauseDialog(JFrame frame, boolean volumeOn, GameMenu gMenu) {
        super(frame, "Tạm ngưng", true);
        setUndecorated(true); // Loại bỏ khung viền
        setSize(500, 400);
        setLocationRelativeTo(frame);

        this.gMenu = gMenu;
        this.volumeOn = volumeOn;

        // Đảm bảo dialog nhận focus khi hiển thị
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                getContentPane().requestFocusInWindow();
            }
        });

        // Tạo nội dung giao diện
        setContentPane(new CustomPanel());
    }

    private class CustomPanel extends JPanel implements ActionListener {
        private JButton volumeButton;
        private JButton resumeButton;
        private JButton homeButton;

        private ImageIcon volumeOnIcon = new ImageIcon("resources/button/_button_volume_on.png");
        private ImageIcon volumeOffIcon = new ImageIcon("resources/button/_button_volume_off.png");
        private ImageIcon resumeIcon = new ImageIcon("resources/button/_button_continue.png");
        private ImageIcon homeIcon = new ImageIcon("resources/button/button_home.png");

        public CustomPanel() {
            setLayout(null);

            // Đảm bảo panel có thể nhận focus
            setFocusable(true);
            requestFocusInWindow();

            // Nút âm lượng
            volumeButton = createStyledButton(resizeIcon((volumeOn ? volumeOnIcon : volumeOffIcon), 100, 100));
            volumeButton.setBounds(50, 200, 100, 100);
            volumeButton.setFocusable(false); // Ngăn cản focus
            add(volumeButton);

            // Nút tiếp tục
            resumeButton = createStyledButton(resizeIcon(resumeIcon, 100, 100));
            resumeButton.setBounds(200, 200, 100, 100);
            resumeButton.setFocusable(false); // Ngăn cản focus
            add(resumeButton);

            // Nút thoát
            homeButton = createStyledButton(resizeIcon(homeIcon, 100, 100));
            homeButton.setBounds(350, 200, 100, 100);
            homeButton.setFocusable(false); // Ngăn cản focus
            add(homeButton);

            // Gán phím Space để đóng dialog
            InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = getActionMap();

            inputMap.put(KeyStroke.getKeyStroke("SPACE"), "closeDialog");
            actionMap.put("closeDialog", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Đóng dialog
                }
            });
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
                volumeOn = !volumeOn;
                volumeButton.setIcon(volumeOn ? resizeIcon(volumeOnIcon, 100, 100) : resizeIcon(volumeOffIcon, 100, 100));
                gMenu.volumeOn = volumeOn;
                gMenu.drawVolumeButton();
            } else if (e.getSource() == resumeButton) {
                lastButtonClicked = "Resume Button";
                dispose(); // Đóng dialog để tiếp tục trò chơi
            } else if (e.getSource() == homeButton) {
                lastButtonClicked = "Home Button";
                JDialog parentDialog = (JDialog) SwingUtilities.getWindowAncestor(this);
                JFrame parentFrame = (JFrame) parentDialog.getOwner();
                parentFrame.getContentPane().removeAll();
                parentFrame.add(gMenu);
                parentFrame.revalidate();
                parentFrame.repaint();
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
