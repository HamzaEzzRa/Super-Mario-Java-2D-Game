package com.TETOSOFT.Menu;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.TETOSOFT.tilegame.GameEngine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Menu extends JFrame {


	private GameEngine game;

	/**
	 * Launch the application.
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

					Menu window = new Menu();
					window.setVisible(true);
	}



	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
		game=new GameEngine();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setBounds(100, 100,1001 , 786);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		JLabel back = new JLabel("");
		JLabel btnPlay = new JLabel("n");

		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {


			        	  Thread t = new Thread(new Runnable() {
			        		    @Override
			        		    public void run() {
			        		     try {
									game.run();

								} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			        		    }

			        		   });
			        		t.start();

			}

		});
		JLabel bntQuit = new JLabel("");

		bntQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

			        	      System.exit(1);

			}

		});
    	JLabel btnOpt = new JLabel("");

		btnOpt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {



			}

		});


		btnPlay.setIcon(new ImageIcon("images/play.png"));
		btnPlay.setBounds(535, 200, 569, 96);
		this.getContentPane().add(btnPlay);


		btnOpt.setIcon(new ImageIcon("images/opt.png"));
		btnOpt.setBounds(500, 300, 569, 96);
		getContentPane().add(btnOpt);



		bntQuit.setIcon(new ImageIcon("images/over.png"));
		bntQuit.setBounds(520, 400, 569, 96);
		getContentPane().add(bntQuit);


		back.setIcon(new ImageIcon("images/back.jpg"));
		back.setBounds(0, 0, 983, 739);
		this.getContentPane().add(back);




	}
}
