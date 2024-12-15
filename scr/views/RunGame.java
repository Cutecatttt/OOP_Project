package scr.views;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.LinkedList;

import javax.swing.*;

import scr.controller.GameSound;
import scr.main.GameMenu;
import scr.utils.Map;
import scr.models.*;

import java.awt.*;

public class RunGame extends JPanel {
    private Random random = new Random();
    private PlayerVehicle playerVehicle;
    private LinkedList<ComputerVehicle> comVehicle = new LinkedList<>();
    private LinkedList<Tree> trees = new LinkedList<>();
    private Map map;

    private int numVehicle;
    private int CId;
    private double Coe = 2;
    public double SpCoe = 1;
    private static String[][] imgPath = {
            { "resources/images/mb1.png", "resources/images/mb2.png", "resources/images/mb3.png" }, // xe máy
            { "resources/images/carblue.png", "resources/images/carpink.png", "resources/images/carwhite.png" }, // oto
            { "resources/images/hat.png", "resources/images/personleft.png" }, // người qua đường trái
            { "resources/images/hat.png", "resources/images/personright.png" }, // người qua đường phải
            { "resources/images/police4.png", "resources/images/police2.png" }, // cảnh sát
            { "resources/images/carblueright.png", "resources/images/carpinkright.png", "resources/images/carwhiteright.png" },
            // oto sang đường bên phải
            { "resources/images/carblueleft.png", "resources/images/carpinkleft.png", "resources/images/carwhiteleft.png" },
            // oto sang đường bên trái
            { "resources/images/main1.png", "resources/images/main1left.png", "resources/images/main1right.png" }, // nhân vật chính
            { "resources/images/main2.png", "resources/images/main2left.png", "resources/images/main2right.png" }
    };

    private boolean volumeOn;
    private GameSound backgroundMusic = new GameSound("resources/sound/bgm.wav");
    private GameSound hornMusic = new GameSound("resources/sound/horn.wav"); // còi xe

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private PauseDialog pauseDialog;
    private JButton pauseButton;
    private JFrame frame;
    private Timer timer;
    public GameMenu GMenu;
    public double playerScore = 0;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    private int screenHeight = Math.min(screenSize.height, 1000);
    private int screenWidth = 800;

    public RunGame(JFrame frame, GameMenu gMenu) {
        this.frame = frame;
        this.GMenu = gMenu;
        this.volumeOn = gMenu.volumeOn;

        setSize(screenWidth, screenHeight);
        setLayout(null);
        setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        pauseDialog = new PauseDialog(frame, volumeOn, GMenu);

        // Initialize the pause button
        pauseButton = new JButton();
        pauseButton.setBounds(700, 50, 66, 66); // Set position and size
        pauseButton.setFocusPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.addActionListener(e -> pauseGame());

        // Add the pause button
        add(pauseButton);

        // Initialize PlayerVehicle
        CId = gMenu.getCId();
        playerVehicle = new PlayerVehicle(imgPath[7 + CId][0], 400, 450, 60, 100, 3);

        // Initialize comVehicle
        if (gMenu.getDif() == 1)
            numVehicle = 4;
        else if (gMenu.getDif() == 2) {
            numVehicle = 5;
            Coe *= 1.5;
        } else if(gMenu.getDif() == 3) {
            numVehicle = 6;
            Coe *= 2;
        } else if(gMenu.getDif() == 4) {
            numVehicle = 7;
            Coe *= 2.5;
        } else {
            numVehicle = 7;
            Coe *= 3;
        }

        map = new Map();
        addVehicle();

        // Initialize trees
        for (int i = 0; i < 3; i++) {
            Tree treeLeft = new Tree(125, -100 + i * 350);
            Tree treeRight = new Tree(675, -100 + i * 350);
            trees.add(treeLeft);
            trees.add(treeRight);
        }

        setFocusable(true);
        setupKeyBindings();

        // Timer for game updates
        timer = new Timer(15, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coe *= 1.0002;
                map.setCoe(Coe);
                map.setSpCoe(SpCoe);
                map.move();
                setSound();
                addVehicle();
                addTree();
                moveComputerVehicle();
                moveTree();
                movePlayerVehicle();
                checkScore();
                repaint();
            }
        });
        timer.start();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Define actions for key press
        inputMap.put(KeyStroke.getKeyStroke("UP"), "upPressed");
        inputMap.put(KeyStroke.getKeyStroke("released UP"), "upReleased");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
        inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
        inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
        inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "spacePressed");
        inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "spaceReleased");
    
        // Define corresponding actions
        actionMap.put("upPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = true;
            }
        });

        actionMap.put("upReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });

        actionMap.put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = true;
            }
        });

        actionMap.put("downReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });

        actionMap.put("leftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = true;
                playerVehicle.turn(imgPath[7 + CId][1]);
            }
        });

        actionMap.put("leftReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
                playerVehicle.reset();
            }
        });

        actionMap.put("rightPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = true;
                playerVehicle.turn(imgPath[7 + CId][2]);
            }
        });

        actionMap.put("rightReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
                playerVehicle.reset();
            }
        });

        actionMap.put("spacePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });
    }

    public void setSound() {
        if (volumeOn) {
            backgroundMusic.play();
            backgroundMusic.loop();
        } else {
            backgroundMusic.stop();
        }
    }

    // Phương thức tạm dừng game
    public void pauseGame() {
        timer.stop(); // Dừng game
        backgroundMusic.stop();
        hornMusic.stop();
        pauseDialog.setVisible(true);
        volumeOn = pauseDialog.getVolume();
        if (pauseDialog.getLastButtonClicked() != "Home Button")
            timer.start(); // Tiếp tục game khi đóng hộp thoại
    }

    // Phương thức tránh chồng chất các xe mới và cũ
    public boolean checkComVehicle(ComputerVehicle newCV) {
        for (int i = 0; i < comVehicle.size(); i++) {
            ComputerVehicle cV = comVehicle.get(i);
            if (cV.getX() - 10 < newCV.getX() + newCV.getWidth() &&
                    cV.getX() + cV.getWidth() + 10 > newCV.getX() &&
                    cV.getY() - 10 < newCV.getY() + newCV.getHeight() &&
                    cV.getY() + cV.getHeight() + 10 > newCV.getY()) {
                return false;
            }
        }
        return true;
    }

    // Phương thức di chuyển của comVehicle
    public void moveComputerVehicle() {
        for (int i = 0; i < comVehicle.size(); i++) {
            Boolean checkBoolean = true;
            if (map.getRoadType() == 1 && map.getY() - 80 > comVehicle.get(i).getY())
                checkBoolean = false;
            double getSpChange = (2 * Coe) * SpCoe - (2 * Coe);
            comVehicle.get(i).check(comVehicle, i, checkBoolean);
            comVehicle.get(i).move(Coe, getSpChange);
        }
    }

    // Phương thức di chuyển của cây
    public void moveTree() {
        for (int i = 0; i < trees.size(); i++) {
            trees.get(i).move(Coe, SpCoe);
        }
    }

    // Phương thức khởi tạo lại xe
    public void addVehicle() {
        for (int i = 0; i < comVehicle.size(); i++) {
            double y = comVehicle.get(i).getY();
            if (y < -200 || y > 1000) {
                comVehicle.remove(i);
            }
        }

        if (map.getRoadType() == 1) {
            int y = map.getY();
            if (comVehicle.size() < numVehicle + 5) {
                int type = 0;
                int t = random.nextInt(5); // random loại người theo tỉ lệ

                if (comVehicle.size() < numVehicle + 2 && y > 100 && y < 200) {
                    if (t < 2)
                        type = 2;
                    else if (t < 4)
                        type = 3;
                    else
                        type = 4;
                } else if (comVehicle.size() < numVehicle + 4) {
                    if (y > 300 && y < 490 - 80) /////////////////////////////////////
                        type = 5;
                    else if (y > 510 && y < 700 - 80)
                        type = 6;
                } else if (y < 900 && y > 800) {
                    if (t < 2)
                        type = 2;
                    else if (t < 4)
                        type = 3;
                    else
                        type = 4;
                } else
                    return;

                int k = random.nextInt(imgPath[type].length);
                ComputerVehicle cV = new ComputerVehicle(type, imgPath[type][k]);
                if (checkComVehicle(cV)) {
                    comVehicle.add(cV);
                }
            }
        } else if (comVehicle.size() < numVehicle) {
            int type = random.nextInt(100);
            if (type < 55)
                type = 0;
            else if (type < 90)
                type = 1;
            else if (type < 94)
                type = 2;
            else if (type < 98)
                type = 3;
            else
                type = 4;

            int k = random.nextInt(imgPath[type].length);
            ComputerVehicle cV = new ComputerVehicle(type, imgPath[type][k]);
            if (checkComVehicle(cV)) {
                comVehicle.add(cV);
            }
        }
    }

    // Phương thức khởi tạo lại cây
    public void addTree() {
        if (!trees.isEmpty() && trees.getFirst().getY() > 1000) {
            trees.removeFirst();
            trees.removeFirst();
        }
        if (map.getRoadType() == 1) return;
        if (trees.isEmpty() || trees.getLast().getY() > 350) {
            Tree treeLeft = new Tree(125, -100);
            Tree treeRight = new Tree(675, -100);
            trees.add(treeLeft);
            trees.add(treeRight);
        }
    }

    // Phương thức kiểm tra điểm số
    public void checkScore() {
        playerScore += Coe * SpCoe * 3;
    }

    // Phương thức di chuyển xe của người chơi dựa trên phím
    public void movePlayerVehicle() {
        if (upPressed) {
            if (SpCoe < 2)
                SpCoe += 0.0625;
        } else if (downPressed) {
            if (SpCoe > 0.6)
                SpCoe -= 0.125;
        } else if (!upPressed && !downPressed && SpCoe != 1) {
            if (SpCoe < 1)
                SpCoe += 0.0625;
            else
                SpCoe -= 0.0625;
        }
        if (leftPressed) {
            playerVehicle.moveLeft();
        } else if (rightPressed) {
            playerVehicle.moveRight();
        }

        if (playerVehicle.checkMoveNear(comVehicle) && volumeOn)
            hornMusic.play();
        else
            hornMusic.stop();

        if (playerVehicle.checkMove(comVehicle, trees)) {
            endGame();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw road lines
        if (map != null) {
            map.draw(g);
        }

        // Draw player vehicle
        if (playerVehicle != null) {
            playerVehicle.draw(g);
        }

        // Draw computer vehicles
        if (comVehicle != null) {
            for (ComputerVehicle v : comVehicle) {
                v.draw(g);
            }
        }

        // Draw trees
        for (Tree t : trees) {
            t.draw(g);
        }

        // Display score and speed coefficient
        g.setColor(Color.WHITE); // White for better visibility on a black background
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player Score: " + (int) playerScore, 50, 30);
        g.drawString("Speed Coefficient: " + (int)(SpCoe*Coe*10) + "km/h", 50, 50);

        // Draw pause button icon
        ImageIcon pauseIcon = new ImageIcon("resources/button/button_pause.png");
        g.drawImage(pauseIcon.getImage(), 700, 50, 66, 66, null);
    }

    // Phương thức kết thúc trò chơi
    public void endGame() {
        timer.stop();
        hornMusic.stop();
        backgroundMusic.stop();
        GMenu.volumeOn = volumeOn;
        GMenu.resetRank((int)playerScore);
        frame.getContentPane().removeAll();
        frame.add(new GameOverScreen(frame, (int) (playerScore), GMenu));
        frame.revalidate();
        frame.repaint();
    }
}