package com.gmail.jhanna;

import javax.swing.JFrame;

public class FlappyBird
{
	public FlappyBird()
	{
		JFrame frame = new JFrame();
		frame.add(new Drawer(frame));
		frame.setTitle("Flappy Bird");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	public static void main(String []args)
	{
		new FlappyBird();

	}
}