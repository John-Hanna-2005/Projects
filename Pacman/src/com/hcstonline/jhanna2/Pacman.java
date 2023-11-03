package com.hcstonline.jhanna2;

import javax.swing.JFrame;

public class Pacman
{
	public Pacman()
	{
		JFrame frame = new JFrame();
		frame.add(new Tilemap(frame));
		frame.setTitle("The Green Mile Pacman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new Pacman();

	}

}
