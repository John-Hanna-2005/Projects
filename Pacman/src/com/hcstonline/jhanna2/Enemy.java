package com.hcstonline.jhanna2;

import java.awt.Graphics;
import java.awt.Image;

public class Enemy
{
	private Manager i = new Manager();
	private int x;
	private int y;
	private int tileSize;
	private int speed;
	private Tilemap map;
	
	private Image normalEnemy;
	private Image scaredEnemy;
	private Image scaredEnemy1;
	
	private String movingDirection;
	private int directionTimerDefault;
	private int directionTimer;
	
	private boolean flashing;
	private long flashTime;
	
	private String[] movingDirections = new String[] {"up","down","left","right"};
	
	public Enemy(int x, int y, int tileSize, int speed, int count, Tilemap map)
	{
		this.tileSize = tileSize;
		this.speed = speed;
		this.map = map;
		this.x = x;
		this.y = y;
		this.flashing = false;
		this.movingDirection = movingDirections[random(0, 3)];
		this.directionTimerDefault = random(1, 10);
		this.directionTimer = this.directionTimerDefault;
		loadEnemies(count);
	}
	
	private int random(int min, int max)
	{
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}
	
	public void loadEnemies(int count)
	{
		switch(count)
		{
			case 0:
				normalEnemy = i.getImageResources("../../../../Resources/paul.png");
				scaredEnemy = i.getImageResources("../../../../Resources/paul_s.png");
				scaredEnemy1 = i.getImageResources("../../../../Resources/paul_s1.png");
				break;
			case 1:
				normalEnemy = i.getImageResources("../../../../Resources/del.png");
				scaredEnemy = i.getImageResources("../../../../Resources/del_s.png");
				scaredEnemy1 = i.getImageResources("../../../../Resources/del_s1.png");
				break;
			case 2:
				normalEnemy = i.getImageResources("../../../../Resources/hal.png");
				scaredEnemy = i.getImageResources("../../../../Resources/hal_s.png");
				scaredEnemy1 = i.getImageResources("../../../../Resources/hal_s1.png");
				break;
			case 3:
				normalEnemy = i.getImageResources("../../../../Resources/brutal.png");
				scaredEnemy = i.getImageResources("../../../../Resources/brutal_s.png");
				scaredEnemy1 = i.getImageResources("../../../../Resources/brutal_s1.png");
				break;
			case 4:
				normalEnemy = i.getImageResources("../../../../Resources/percy.png");
				scaredEnemy = i.getImageResources("../../../../Resources/percy_s.png");
				scaredEnemy1 = i.getImageResources("../../../../Resources/percy_s1.png");
				break;
		}
		
		
		this.speed = 2;
	}
	
	public void drawEnemy(Graphics g, boolean pause, long currentTime)
	{
		if (!pause) 
		{
			this.move();
			this.changeDirection();
		}
		if(flashing)
		{
			long storeFlashTime = currentTime - flashTime;
			if(storeFlashTime <= 3000)
			{
				g.drawImage(scaredEnemy, x, y, map);
			}
			else if(storeFlashTime > 3000 && storeFlashTime <= 6000)
			{
				boolean whichFlash = (storeFlashTime / 500) % 2 == 0;
				if(whichFlash)
		    	{
					g.drawImage(scaredEnemy, x, y, map);
		    	}
		    	else
		        {
		    		g.drawImage(scaredEnemy1, x, y, map);
		        }
		    }
			else if(storeFlashTime > 6000)
			{
				flashing = false;
			}
		 }
		 else
		 {
			 g.drawImage(normalEnemy, x, y, map);
			 flashTime = System.currentTimeMillis();
		 }
	}
	
	public boolean collideWith(Pac pacman) 
	{
	    int size = 24;
	    return this.x < pacman.getX() + size && this.x + size > pacman.getX() && this.y < pacman.getY() + size && this.y + size > pacman.getY();
	}

	public void changeDirection() 
	{
		this.directionTimer--;
	    String newMoveDirection = null;
	    if (this.directionTimer == 0) 
	    {
	    	this.directionTimer = this.directionTimerDefault;
	    	newMoveDirection = movingDirections[random(0, 3)];
	    }

	    if(newMoveDirection != null && this.movingDirection != newMoveDirection)
	    {
	    	if (this.x % 36 == 0 && this.y % 36 == 0)
	    	{
	    		this.movingDirection = newMoveDirection;
	    	}
	    }
	}

	public void move() 
	{
		if (!map.didCollide(this.x, this.y, this.movingDirection)) 
	    {
	    	if(movingDirection == "up")
			{	
				this.y -= this.speed;
			}
			else if(movingDirection == "down")
			{	
				this.y += this.speed;
			}
			else if(movingDirection == "left")
			{	
				this.x -= this.speed;
			}
			else if(movingDirection == "right")
			{	
				this.x += this.speed;
			}
	
	    }
	}
	  
	public void makeFlashing()
	{
		flashing = true;
	}
	  
	public boolean isFlashing()
	{
		return flashing;
	}
	
}
