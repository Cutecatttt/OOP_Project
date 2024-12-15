package scr.main;

import scr.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton characterButton;
    private JButton difButton;
    private JButton rankingButton;
    private JButton guideButton;
    private JButton volumeButton;

    private JFrame frame;
    private String difficulty = "Easy";
    private int selectedCharacterIndex = 0, DifId = 1;
    public boolean volumeOn;
    ImageIcon backgroundImage = new ImageIcon("resources/images/menubackground.png");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    private int screenHeight = Math.min(screenSize.height, 1000);
    private int rankNo1 = 144202, rankNo2 = 122320, rankNo3 = 0;

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    ImageIcon startIcon = new ImageIcon("resources/button/button_start.png");
    ImageIcon characterIcon = new ImageIcon("resources/button/button_option.png");
    ImageIcon rankIcon = new ImageIcon("resources/button/button_rank.png");
    ImageIcon difIcon = new ImageIcon("resources/button/button_level.png");
    ImageIcon guideIcon = new ImageIcon("resources/button/button_guide.png");
    ImageIcon volumeOnIcon = new ImageIcon("resources/button/button_volume_on.png");
    ImageIcon volumeOffIcon = new ImageIcon("resources/button/button_volume_off.png");
    
    public GameMenu(JFrame frame, boolean volumeOn) {
        this.frame = frame;
        this.volumeOn = volumeOn;
        setLayout(null);

        startButton = createStyledButton(resizeIcon(startIcon, 180, 66));
        characterButton = createStyledButton(resizeIcon(characterIcon, 180, 66));
        rankingButton = createStyledButton(resizeIcon(rankIcon, 180, 66));
        guideButton = createStyledButton(resizeIcon(guideIcon, 66, 66));
        difButton = createStyledButton(resizeIcon(difIcon, 180, 66));
        // Nút âm lượng
        volumeButton = createStyledButton(resizeIcon((volumeOn ? volumeOnIcon : volumeOffIcon), 66, 66));
        volumeButton.setBounds(620, 20, 66, 66);
        add(volumeButton);

        characterButton.setBounds(310, screenHeight - 370, 180, 66);
        add(characterButton);

        startButton.setBounds(310, screenHeight - 450, 180, 66);
        add(startButton);

        rankingButton.setBounds(310, screenHeight - 210, 180, 66);
        add(rankingButton);        

        guideButton.setBounds(700, 20, 66, 66);
        add(guideButton);

        difButton.setBounds(310, screenHeight - 290, 180, 66);
        add(difButton);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createStyledButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            frame.getContentPane().removeAll();
            RunGame game = new RunGame(frame, this);
            frame.add(game);
            frame.revalidate();
            frame.repaint();
            game.requestFocusInWindow();
        } else if (e.getSource() == difButton) {
            String[] difficulties = { "Chill guy", "Medium", "Hard", "Asian", "Are you sure!?"};
            difficulty = (String) JOptionPane.showInputDialog(this, "Chọn độ khó:", "Độ khó",
                    JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulty);
            if (difficulty != null) {
                switch (difficulty) {
                    case "Chill guy":
                        DifId = 1;
                        break;
                    case "Medium":
                        DifId = 2;
                        break;
                    case "Hard":
                        DifId = 3;
                        break;
                    case "Asian":
                        DifId = 4;
                    case "Are you sure!?":
                        DifId = 5;
                }
                // difButton.setText("Chọn độ khó: " + difficulty);
            }
        } else if (e.getSource() == characterButton) {
            frame.getContentPane().removeAll();
            frame.add(new CharacterSelectionScreen(frame, this));
            frame.revalidate();
            frame.repaint();
        } else if (e.getSource() == guideButton) {
            showGuideDialog();
        } else if (e.getSource() == volumeButton) {
            // Chuyển đổi trạng thái âm lượng
            volumeOn = !volumeOn;
            volumeButton
                    .setIcon(volumeOn ? resizeIcon(volumeOnIcon, 66, 66) : resizeIcon(volumeOffIcon, 66, 66));
        } else if (e.getSource() == rankingButton) {
            showRank();
        }
    }

    public void drawVolumeButton() {
        volumeButton
                    .setIcon(volumeOn ? resizeIcon(volumeOnIcon, 66, 66) : resizeIcon(volumeOffIcon, 66, 66));
    }

    private void showGuideDialog() {
        JDialog guideDialog = new JDialog(frame, "Hướng dẫn", true);
        guideDialog.setSize(400, 650);
        guideDialog.setLocationRelativeTo(frame);
        guideDialog.setLayout(new BorderLayout());

        // Thêm ảnh vào dialog
        ImageIcon guideImage = new ImageIcon("resources/images/img_guide.png");
        JLabel guideLabel = new JLabel(
                new ImageIcon(guideImage.getImage().getScaledInstance(400, 600, Image.SCALE_SMOOTH)));
        guideDialog.add(guideLabel, BorderLayout.CENTER);

        // Nút "OK"
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> guideDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        guideDialog.add(buttonPanel, BorderLayout.SOUTH);

        guideDialog.setVisible(true);
    }

    private void showRank() {
        JDialog guideDialog = new JDialog(frame, "Rank", true);
        guideDialog.setSize(600, 700);
        guideDialog.setLocationRelativeTo(frame);
        guideDialog.setLayout(new BorderLayout());

        // Tạo JLabel hiển thị ảnh
        ImageIcon rank = new ImageIcon("resources/images/img_rank.png");
        JLabel backgroundLabel = new JLabel(resizeIcon(rank, 600, 600)); 
        backgroundLabel.setBounds(0, 0, 600, 600); //

        JLabel scoreNo1 = new JLabel(String.valueOf(rankNo1));
        scoreNo1.setFont(new Font("Arial", Font.BOLD, 30));
        scoreNo1.setForeground(Color.decode("#333")); // Màu chữ
        scoreNo1.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
        scoreNo1.setBounds(270, 302, 200, 40); // Đặt vị trí chữ trên ảnh

        JLabel scoreNo2 = new JLabel(String.valueOf(rankNo2));
        scoreNo2.setFont(new Font("Arial", Font.BOLD, 30));
        scoreNo2.setForeground(Color.decode("#333")); // Màu chữ
        scoreNo2.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
        scoreNo2.setBounds(270, 392, 200, 40);

        JLabel scoreNo3 = new JLabel(String.valueOf(rankNo3));
        scoreNo3.setFont(new Font("Arial", Font.BOLD, 30));
        scoreNo3.setForeground(Color.decode("#333")); // Màu chữ
        scoreNo3.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
        scoreNo3.setBounds(270, 482, 200, 40);

        
        guideDialog.add(scoreNo1);
        guideDialog.add(scoreNo2);
        guideDialog.add(scoreNo3);
        guideDialog.add(backgroundLabel); 

        // Nút "OK"
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> guideDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        guideDialog.add(buttonPanel, BorderLayout.SOUTH);

        guideDialog.setVisible(true);
    }

    public void resetRank(int newScore) {
        if (newScore > rankNo1) {
            rankNo3 = rankNo2;
            rankNo2 = rankNo1;
            rankNo1 = newScore;
        } else if (newScore > rankNo2) {
            rankNo3 = rankNo2;
            rankNo2 = newScore;
        } else if (newScore > rankNo3) {
            rankNo3 = newScore;
        }
    }

    public void setSelectedCharacterIndex(int index) {
        this.selectedCharacterIndex = index;
    }

    public int getCId() {
        return selectedCharacterIndex;
    }

    public int getDif() {
        return DifId;
    }

    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = Math.min(screenSize.height, 1000);
        int screenWidth = 800;
        JFrame frame = new JFrame("Chạy đi chờ chi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.add(new GameMenu(frame, true));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}