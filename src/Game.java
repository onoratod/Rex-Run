/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * Donato Onorato
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {

		// Top-level frame in which game components live
		final JFrame frame = new JFrame("REX RUN");
		frame.setLocation(300, 300);
		frame.setResizable(false);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Press Start");
		status_panel.add(status);

		
		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);
		

		//score label
		final JLabel score = new JLabel("Score: " + 0);
		control_panel.add(score);
		
		//start button
		final JButton start = new JButton("Start");
		
		// Main playing area
		final GameCourt court = new GameCourt(status, score, start, frame);
		frame.add(court, BorderLayout.CENTER);
		//pause, instructions, and high scores buttons
		final JButton pause = new JButton("Pause");
		final JButton info = new JButton("Info");
		final JButton hScores = new JButton("High Scores");
		control_panel.add(info);
		control_panel.add(hScores);
		
		//action listener that shows the games, instructions
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!court.playing) {
					JOptionPane.showMessageDialog(frame,
						    "                     Welcome to Rex Run    		   \n"
						  + "=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=\n"
							+ "The game is pretty staightforward. You have to\n"
						    + "avoid obstacles by pressing the SPACE bar to jump.\n"
							+ "The longer you run without getting hit, the higher your\n"
						    + "score. Some logistics: you can only reset the game if it\n"
							+ "is paused or the game has ended. To begin playing just\n"
						    + "press start. You can pause and resume your game by\n"
						    + "pressing the pause/resume button.\n"
						    + "Please enjoy!\n"
						  + "=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=", "Rules", 
						  JOptionPane.PLAIN_MESSAGE);
				}
				court.getFocus();//give the game court focus	
			}
		});
		
		//dispalys the high scores
		hScores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!court.playing) {

					JOptionPane.showMessageDialog(frame, court.lead.constructHighScoreTable(), 
							"Leader Board", JOptionPane.PLAIN_MESSAGE); 
				}
				court.getFocus(); //give back focus to the court
			}
		});
	
		//start button, starts the game, and resets it
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(court.playing == true) {
					//do nothing
				} else {
				   court.playing = true;
				   court.reset();
                   court.started = true;
                   court.gameOver = false;
                   status.setText("Running");
                   start.setText("Reset");
                   pause.setText("Pause");
                   
				}
			}

		});
				//pause action listener, that pauses, and resumes the game
				pause.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					if (!court.started || court.gameOver) { 
							// do nothing
						    court.getFocus();
					} else {
						if(court.playing == true) {
							pause.setText("Resume");
							court.playing = false;
							court.getFocus();
							status.setText("Paused");
						} else {
							pause.setText("Pause");
							court.playing = true;
							court.getFocus();
							status.setText("Running");
						}
					}
				}	
				});
	    
	    
		//add buttons to panel
		control_panel.add(pause);
		control_panel.add(start);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		court.reset();
	}
	
	

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
