package com.zby;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameUtils {
    public static Image bgImg;
    public static Image myPlaneImg;
    public static Image enemyPlaneImg;
    public static Image bulletImg;
    public static Image enemyBulletImg;
    public static Image bossPlaneImg;
    public static List<Image> explodeImgs = new ArrayList<>();

    static {
        // 使用绝对路径加载图片
        bgImg = new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\bg.jpg").getImage();
        myPlaneImg = new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\Plane.png").getImage();
        enemyPlaneImg = new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\enemy.png").getImage();
        // 交换子弹图片路径
        bulletImg = new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\shell.png").getImage();
        enemyBulletImg = new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\bullet.png").getImage();
        bossPlaneImg = new ImageIcon("D:\\BaiduNetdiskDownload\\1.游戏图片\\imgs\\boss.png").getImage();

        // 加载爆炸效果图片
        for (int i = 1; i <= 16; i++) {
            explodeImgs.add(new ImageIcon("C:\\Users\\GeekGuru\\Downloads\\java-swing-master\\untitled\\src\\com\\zby\\imgs\\explode\\e" + i + ".gif").getImage());
        }
    }
}