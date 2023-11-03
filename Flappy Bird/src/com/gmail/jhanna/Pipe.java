package com.gmail.jhanna;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Pipe extends Rectangle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7579601173128547958L;

	private Image pipe;

	
	
	public Pipe(int x, int y, Image pipe)
	{
		super(x, y , 90, 500);
		this.pipe = pipe;
	}
	

	public void pipeMaker(Graphics g)
	{
		g.drawImage(pipe, x, y, width, height, null);
		
		
		// height = 150, gap = 140
		// -250, -430
	}
	
	

	


	
	
}
