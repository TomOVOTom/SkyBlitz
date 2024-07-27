package com.zby;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameWin extends JFrame {
    private Image offScreenImage = null;
    private int myPlaneX = 250, myPlaneY = 500;
    private int myPlaneHealth = 100;
    private int score = 0;
    private List<Bullet> bullets = new ArrayList<>();
    private List<EnemyPlane> enemyPlanes = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();
    private List<Explosion> explosions = new ArrayList<>();
    private boolean left, right, shoot, skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8;
    private Random random = new Random();
    private boolean gameOver = false;
    private Clip clip;
    private int level = 1;

    public GameWin() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        shoot = true;
                        break;
                    case KeyEvent.VK_1:
                        skill1 = true;
                        break;
                    case KeyEvent.VK_2:
                        skill2 = true;
                        break;
                    case KeyEvent.VK_3:
                        skill3 = true;
                        break;
                    case KeyEvent.VK_4:
                        skill4 = true;
                        break;
                    case KeyEvent.VK_5:
                        skill5 = true;
                        break;
                    case KeyEvent.VK_6:
                        skill6 = true;
                        break;
                    case KeyEvent.VK_7:
                        skill7 = true;
                        break;
                    case KeyEvent.VK_8:
                        skill8 = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (gameOver) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        shoot = false;
                        break;
                    case KeyEvent.VK_1:
                        skill1 = false;
                        break;
                    case KeyEvent.VK_2:
                        skill2 = false;
                        break;
                    case KeyEvent.VK_3:
                        skill3 = false;
                        break;
                    case KeyEvent.VK_4:
                        skill4 = false;
                        break;
                    case KeyEvent.VK_5:
                        skill5 = false;
                        break;
                    case KeyEvent.VK_6:
                        skill6 = false;
                        break;
                    case KeyEvent.VK_7:
                        skill7 = false;
                        break;
                    case KeyEvent.VK_8:
                        skill8 = false;
                        break;
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!gameOver) {
                    myPlaneX = e.getX() - GameUtils.myPlaneImg.getWidth(null) / 2;
                    myPlaneY = e.getY() - GameUtils.myPlaneImg.getHeight(null) / 2;
                }
            }
        });

        playMusic("D:\\BaiduNetdiskDownload\\硫克克琉 - 烟distance完整版.wav");
    }

    private void playMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void move() {
        if (left && myPlaneX > 0) myPlaneX -= 5; // 调整玩家移动速度
        if (right && myPlaneX < this.getWidth() - GameUtils.myPlaneImg.getWidth(null)) myPlaneX += 5; // 调整玩家移动速度
        if (shoot) bullets.add(new Bullet(myPlaneX + 25, myPlaneY));
        if (skill1) useSkill1();
        if (skill2) useSkill2();
        if (skill3) useSkill3();
        if (skill4) useSkill4();
        if (skill5) useSkill5();
        if (skill6) useSkill6();
        if (skill7) useSkill7();
        if (skill8) useSkill8();
    }

    private void useSkill1() {
        // 全屏攻击技能
        for (int i = 0; i < this.getWidth(); i += 50) {
            bullets.add(new Bullet(i, myPlaneY));
        }
    }

    private void useSkill2() {
        // 多方向攻击技能
        bullets.add(new Bullet(myPlaneX + 25, myPlaneY));
        bullets.add(new Bullet(myPlaneX + 25, myPlaneY - 10));
        bullets.add(new Bullet(myPlaneX + 25, myPlaneY - 20));
    }

    private void useSkill3() {
        // 圆圈攻击技能
        for (int angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            bullets.add(new Bullet(myPlaneX + 25 + (int) (50 * Math.cos(rad)), myPlaneY + (int) (50 * Math.sin(rad))));
        }
    }

    private void useSkill4() {
        // 十字攻击技能
        for (int i = -50; i <= 50; i += 10) {
            bullets.add(new Bullet(myPlaneX + 25 + i, myPlaneY));
            bullets.add(new Bullet(myPlaneX + 25, myPlaneY + i));
        }
    }

    private void useSkill5() {
        // 扇形全屏攻击技能
        for (int angle = -60; angle <= 60; angle += 10) {
            double rad = Math.toRadians(angle);
            bullets.add(new Bullet(myPlaneX + 25, myPlaneY, rad));
        }
    }

    private void useSkill6() {
        // 螺旋全屏攻击技能
        for (int angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            bullets.add(new SpiralBullet(myPlaneX + 25, myPlaneY, rad));
        }
    }

    private void useSkill7() {
        // 自动跟踪敌方战机的攻击技能
        synchronized (enemyPlanes) {
            for (EnemyPlane enemyPlane : enemyPlanes) {
                bullets.add(new TrackingBullet(myPlaneX + 25, myPlaneY, enemyPlane));
            }
        }
    }

    private void useSkill8() {
        // 追踪敌方子弹的技能
        synchronized (enemyBullets) {
            for (Bullet enemyBullet : enemyBullets) {
                bullets.add(new TrackingBullet(myPlaneX + 25, myPlaneY, enemyBullet));
            }
        }
    }

    private void checkCollisions() {
        synchronized (bullets) {
            synchronized (enemyPlanes) {
                // 检查我方子弹与敌机的碰撞
                Iterator<Bullet> bulletIterator = bullets.iterator();
                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    Iterator<EnemyPlane> enemyIterator = enemyPlanes.iterator();
                    while (enemyIterator.hasNext()) {
                        EnemyPlane enemyPlane = enemyIterator.next();
                        if (bullet.getBounds().intersects(enemyPlane.getBounds())) {
                            explosions.add(new Explosion(enemyPlane.x, enemyPlane.y));
                            bulletIterator.remove();
                            enemyIterator.remove();
                            score += 10;
                            if (enemyPlane instanceof BossPlane) {
                                myPlaneHealth += 20; // 打死Boss机加血量
                            }
                            break;
                        }
                    }
                }
            }
        }

        synchronized (enemyBullets) {
            // 检查敌方子弹与我方飞机的碰撞
            Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
            while (enemyBulletIterator.hasNext()) {
                Bullet bullet = enemyBulletIterator.next();
                if (bullet.getBounds().intersects(new Rectangle(myPlaneX, myPlaneY, GameUtils.myPlaneImg.getWidth(null), GameUtils.myPlaneImg.getHeight(null)))) {
                    myPlaneHealth -= 10;
                    enemyBulletIterator.remove();
                    if (myPlaneHealth <= 0) {
                        gameOver = true;
                        showGameOverDialog();
                    }
                }
            }
        }

        synchronized (bullets) {
            synchronized (enemyBullets) {
                // 检查我方子弹与敌方子弹的碰撞
                Iterator<Bullet> bulletIterator = bullets.iterator();
                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
                    while (enemyBulletIterator.hasNext()) {
                        Bullet enemyBullet = enemyBulletIterator.next();
                        if (bullet.getBounds().intersects(enemyBullet.getBounds())) {
                            bulletIterator.remove();
                            enemyBulletIterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void showGameOverDialog() {
        int option = JOptionPane.showOptionDialog(this, "游戏结束。是否重新开始？", "游戏结束",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"重新开始", "退出"}, "重新开始");
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        synchronized (bullets) {
            synchronized (enemyPlanes) {
                synchronized (enemyBullets) {
                    synchronized (explosions) {
                        myPlaneX = 250;
                        myPlaneY = 500;
                        myPlaneHealth = 100;
                        score = 0;
                        bullets.clear();
                        enemyPlanes.clear();
                        enemyBullets.clear();
                        explosions.clear();
                        gameOver = false;
                        level = 1;
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(600, 600);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        gOffScreen.clearRect(0, 0, 600, 600);
        // 绘制背景
        gOffScreen.drawImage(GameUtils.bgImg, 0, 0, this);
        // 绘制我方飞机
        gOffScreen.drawImage(GameUtils.myPlaneImg, myPlaneX, myPlaneY, this);
        // 绘制子弹
        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                bullet.move();
                gOffScreen.drawImage(GameUtils.bulletImg, bullet.x, bullet.y, this);
            }
        }
        // 绘制敌方飞机
        synchronized (enemyPlanes) {
            for (EnemyPlane enemyPlane : enemyPlanes) {
                enemyPlane.move();
                gOffScreen.drawImage(GameUtils.enemyPlaneImg, enemyPlane.x, enemyPlane.y, this);
                // 敌机发射子弹
                if (Math.random() < 0.005) { // 调整敌方子弹发射频率
                    enemyBullets.add(new Bullet(enemyPlane.x + 25, enemyPlane.y + 40));
                }
            }
        }
        // 绘制敌方子弹
        synchronized (enemyBullets) {
            for (Bullet bullet : enemyBullets) {
                bullet.moveDown();
                gOffScreen.drawImage(GameUtils.enemyBulletImg, bullet.x, bullet.y, this);
            }
        }
        // 绘制爆炸效果
        synchronized (explosions) {
            for (Explosion explosion : explosions) {
                explosion.draw(gOffScreen);
            }
        }
        // 绘制我方飞机的血量
        gOffScreen.setColor(new Color(0, 0, 0, 150)); // 半透明背景
        gOffScreen.fillRect(10, 30, 220, 40);
        gOffScreen.setColor(Color.RED);
        gOffScreen.fillRect(20, 40, myPlaneHealth * 2, 20);
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.drawRect(20, 40, 200, 20);
        // 绘制分数
        gOffScreen.setColor(Color.WHITE);
        gOffScreen.setFont(new Font("Arial", Font.BOLD, 20));
        gOffScreen.drawString("分数: " + score, 20, 70);
        graphics.drawImage(offScreenImage, 0, 0, this);
    }

    public static void main(String[] args) {
        GameWin gameWin = new GameWin();
        gameWin.launch();
    }

    // 设置窗口是否可见
    public void launch() {
        this.setVisible(true);
        // 设置窗口大小
        this.setSize(600, 600);
        // 关闭窗口立即终止程序
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 设置窗口位置
        this.setLocationRelativeTo(null);
        // 设置窗口标题
        this.setTitle("飞机大战");

        // 启动游戏循环
        new Timer(8, e -> { // 调整定时器间隔为8毫秒
            if (!gameOver) {
                move();
                checkCollisions();
                repaint();
                // 按顺序生成敌机，越到后面越难
                if (Math.random() < 0.05) { // 增加敌机生成频率
                    if (level % 5 == 0) { // 每5级生成一个Boss
                        enemyPlanes.add(new BossPlane(random.nextInt(600), 0));
                    } else {
                        enemyPlanes.add(new EnemyPlane(random.nextInt(600), 0));
                    }
                    level++;
                }
            }
        }).start();
    }
}

class Bullet {
    int x, y;
    double angle;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bullet(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void move() {
        if (angle != 0) {
            x += (int) (5 * Math.cos(angle));
            y += (int) (5 * Math.sin(angle));
        } else {
            y -= 5; // 调整子弹移动速度
        }
    }

    public void moveDown() {
        y += 1; // 调整敌方子弹移动速度
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameUtils.bulletImg.getWidth(null), GameUtils.bulletImg.getHeight(null));
    }
}

class SpiralBullet extends Bullet {
    double angle;

    public SpiralBullet(int x, int y, double angle) {
        super(x, y);
        this.angle = angle;
    }

    @Override
    public void move() {
        x += (int) (5 * Math.cos(angle));
        y += (int) (5 * Math.sin(angle));
        angle += 0.1; // 调整螺旋子弹的旋转速度
    }
}

class TrackingBullet extends Bullet {
    Object target;

    public TrackingBullet(int x, int y, Object target) {
        super(x, y);
        this.target = target;
    }

    @Override
    public void move() {
        if (target != null) {
            int targetX, targetY;
            if (target instanceof EnemyPlane) {
                targetX = ((EnemyPlane) target).x;
                targetY = ((EnemyPlane) target).y;
            } else {
                targetX = ((Bullet) target).x;
                targetY = ((Bullet) target).y;
            }
            int dx = targetX - x;
            int dy = targetY - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            x += (int) (5 * dx / distance);
            y += (int) (5 * dy / distance);
        }
    }
}

class EnemyPlane {
    int x, y;

    public EnemyPlane(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y += 1; // 调整敌机移动速度
        // 确保敌机不会超出框外
        if (x < 0) x = 0;
        if (x > 600 - GameUtils.enemyPlaneImg.getWidth(null)) x = 600 - GameUtils.enemyPlaneImg.getWidth(null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameUtils.enemyPlaneImg.getWidth(null), GameUtils.enemyPlaneImg.getHeight(null));
    }
}

class BossPlane extends EnemyPlane {
    public BossPlane(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {
        y += 0.5; // 调整Boss机移动速度
        // 确保Boss机不会超出框外
        if (x < 0) x = 0;
        if (x > 600 - GameUtils.bossPlaneImg.getWidth(null)) x = 600 - GameUtils.bossPlaneImg.getWidth(null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, GameUtils.bossPlaneImg.getWidth(null), GameUtils.bossPlaneImg.getHeight(null));
    }
}

class Explosion {
    int x, y;
    int index = 0;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (index < GameUtils.explodeImgs.size()) {
            g.drawImage(GameUtils.explodeImgs.get(index), x, y, null);
            index++;
        }
    }
}