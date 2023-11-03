package com.gmail.jhanna;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;


public class Bird extends Rectangle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1971454444061121562L;
	
	private double speed = 0;
	private double acc = 0.9;
	private double theta = 0;
	private Image birdim;

	
	public Bird(int y, int width, int height, Image birdim)
	{
		super(100, y, width, height);
		
		this.birdim = birdim;

	}
	
	
	public double getXcord()
	{
		return this.getX();
	}
	
	
	public double getYcord()
	{
		return this.getY(); 
	}
	
	
	public Image getImageResources(String fileName)
	{
		URL fileURL = null;
		try
		{
			Class currClass = this.getClass();
			fileURL = new URL(currClass.getResource("Drawer.class"), fileName);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		} 
		return new ImageIcon(fileURL).getImage();
	}
	
	public void birdMaker(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		if(speed < 0)
		{
			theta = -30;
			double rx = x + (width / 2), ry = y + (height / 2);
			g2D.translate(rx, ry);
			g2D.rotate(Math.toRadians(theta));
			g2D.drawImage(birdim, -width / 2, -height / 2, width, height, null);
		}
		else if(speed == 0)
		{
			g2D.drawImage(birdim, x, y, width, height, null);
		}
		else
		{
			theta = theta < 90 ? theta + 8 : 90;
			double rx = x + (width / 2), ry = y + (height / 2);
			g2D.translate(rx, ry);
			g2D.rotate(Math.toRadians(theta));
			g.drawImage(birdim, -width / 2, -height / 2, width, height, null);
		}
	}
	public void setYcord(double y)
	{
		if(this.getY() >= 400)
		{
			this.y = 400;
		}
		else
		{
			this.y = (int) y;
		}
	}
	
	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	
	public double getAcc()
	{
		return acc;
	}
	
	
	public void jump()
	{
		speed = -10;
	}
	

}
