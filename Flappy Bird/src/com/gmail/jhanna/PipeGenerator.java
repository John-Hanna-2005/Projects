package com.gmail.jhanna;

import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PipeGenerator
{
	private ArrayList<Pipe> topPipes;
	private ArrayList<Pipe> bottomPipes;
	private final int STARTX = 880;
	

	public PipeGenerator()
	{
		topPipes = new ArrayList<Pipe>();
		bottomPipes = new ArrayList<Pipe>();
	}

	public void spawnPipes()
	{
		if(topPipes.size() == 0 && bottomPipes.size() == 0) 
		{
			int randY = (int) (Math.random() * 180 -430);
			topPipes.add(new Pipe(STARTX, randY,  getImageResources("../../../../Resources/pipe2.jpg")));
			bottomPipes.add(new Pipe(STARTX, randY + topPipes.get(0).height + 140,  getImageResources("../../../../Resources/pipe.png")));
		}
		else
		{
			if(topPipes.get(topPipes.size()-1).x < STARTX - 300)
			{
				int randY = (int) (Math.random() * 180 -430);
				topPipes.add(new Pipe(STARTX, randY,  getImageResources("../../../../Resources/pipe2.jpg")));
				bottomPipes.add(new Pipe(STARTX, randY + topPipes.get(0).height + 140,  getImageResources("../../../../Resources/pipe.png")));
			}
		}
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
	
	public void movePipe()
	{
		for(int i = 0; i < topPipes.size(); i++)
		{
			topPipes.get(i).x -= 5;
			bottomPipes.get(i).x -= 5;
		}
		if(topPipes.get(0).x < -90)
		{
			topPipes.remove(0);
			bottomPipes.remove(0);
		}
	}
	
	public void drawPipe(Graphics g)
	{
		for(int i = 0; i < topPipes.size(); i++)
		{
			topPipes.get(i).pipeMaker(g);
			bottomPipes.get(i).pipeMaker(g);
		}
	}
	
	public boolean checkCollision(Bird bird)
	{
		if(bird.y < topPipes.get(0).y + 500  || bird.y > bottomPipes.get(0).y - 40)
		{
			if(topPipes.get(0).x > 55 && topPipes.get(0).x < 145)
			{
				return true;
			}
		}
//		return bird.intersects(topPipes.get(0)) || bird.intersects(bottomPipes.get(0));
		
		return false;
	}
	
	public boolean checkScore(Bird bird)
	{
		if(bird.y > topPipes.get(0).y + 500  || bird.y < bottomPipes.get(0).y - 40)
		{
			if(topPipes.get(0).x == 100)
			{
				return true;
			}
		}
		return false;
	}
	


	// spawn pipes, move pipes, draw pipes, check for collision, check if bird is passed curr pipe

}
