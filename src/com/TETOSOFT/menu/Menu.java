package com.TETOSOFT.Menu;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.TETOSOFT.tilegame.GameEngine;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class Menu extends JFrame {
    private int width;
    private int height;
    private GameEngine game;
    
    /**
     * Launch the application.
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        Menu window = new Menu(800, 600);
        window.setVisible(true);
    }

    /**
     * Create the application.
     */
    public Menu(int width, int height) {
        this.width = width;
        this.height = height;
        initialize();
        game = new GameEngine();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setBounds(0, 0, width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel back = new JLabel("");
        JLabel btnPlay = new JLabel("n");

        btnPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                new Thread(() -> {
                    game.run();
                    System.exit(0);
                }).start();
            }
        });
        JLabel btnQuit = new JLabel("");

        btnQuit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                System.exit(0);
            }
        });
        JLabel btnOpt = new JLabel("");
        btnOpt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

            }
        });

        ImageIcon imgIcon = new ImageIcon("images/play.png");
        Image img = imgIcon.getImage();
        btnPlay.setIcon(imgIcon);
        btnPlay.setBounds((width - img.getWidth(null)) / 2,
            (height - img.getHeight(null)) / 6, img.getWidth(null), img.getHeight(null));
        getContentPane().add(btnPlay);

        imgIcon = new ImageIcon("images/opt.png");
        img = imgIcon.getImage();
        btnOpt.setIcon(imgIcon);
        btnOpt.setBounds((width - img.getWidth(null)) / 2,
            (height - img.getHeight(null)) / 3, img.getWidth(null), img.getHeight(null));
        getContentPane().add(btnOpt);

        imgIcon = new ImageIcon("images/over.png");
        img = imgIcon.getImage();
        btnQuit.setIcon(imgIcon);
        btnQuit.setBounds((width - img.getWidth(null)) / 2,
            (height - img.getHeight(null)) / 2, img.getWidth(null), img.getHeight(null));
        getContentPane().add(btnQuit);

        imgIcon = new ImageIcon("images/back.png");
        img = imgIcon.getImage();
        back.setIcon(imgIcon);
        back.setBounds(0, 0, img.getWidth(null), img.getHeight(null));
        getContentPane().add(back);
    }
}
