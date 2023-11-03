package com.hcstonline.jhanna2;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 
public class Pac
{
	private Manager i = new Manager();
	private Image pac1;
	private Image pac2;
	private Image pac3;
	private int index;
	private Image[] phases;
	private int x;
	private int y;
	private int tileSize;
	private int speed;
	private Tilemap map;
	
	private String[] movingDirections = new String[] {"up","down","left","right"};
	private String currentMovingDirection;
	private String requestedMovingDirection;
	
	private int[] rotation = new int[] {3, 1, 2, 0};
	private int pacmanRotation;
	
	private int pacmanAnimationTimerDefault = 3;
	private int pacmanAnimationTimer = -1;
	
	
	private boolean powerDotActive;
	private boolean powerDotAboutToExpire;
	
	List<Enemy> collidedEnemies = new ArrayList<Enemy>(5);
	    
	
	public Pac(int x, int y, int tileSize, int speed, Tilemap map)
	{
		this.x = x;
		this.y = y;
		this.tileSize = tileSize;
		this.speed = speed;
		this.map = map;
		this.pacmanRotation = rotation[3];
		this.powerDotActive = false;
	    this.powerDotAboutToExpire = false;
	    
		loadPacman();
	}
	
	public void loadPacman()
	{
		pac1 = i.getImageResources("../../../../Resources/pacm0.png");
		pac2 = i.getImageResources("../../../../Resources/pacm1.png");
		pac3 = i.getImageResources("../../../../Resources/pacm2.png");
		this.speed = 3;
		phases = new Image[]{pac1, pac2, pac3, pac2};
		
		index = 0;
	}
	
	
	public Image getPhase()
	{
		return phases[index];
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int getPacmanRotation()
	{
		return pacmanRotation;
	}
	
	public void moveUp()
	{
		if(currentMovingDirection == movingDirections[1])
		{
			currentMovingDirection = movingDirections[0];
		}
		requestedMovingDirection = movingDirections[0];
	}
	
	public void moveDown()
	{
		if(this.currentMovingDirection == movingDirections[0])
		{
			this.currentMovingDirection = movingDirections[1];
		}
		this.requestedMovingDirection = movingDirections[1];
	}
	
	public void moveLeft()
	{
		if(this.currentMovingDirection == movingDirections[3])
		{
			this.currentMovingDirection = movingDirections[2];
		}
		this.requestedMovingDirection = movingDirections[2];
	}
	
	public void moveRight()
	{
		if(this.currentMovingDirection == movingDirections[2])
		{
			this.currentMovingDirection = movingDirections[3];
		}
		this.requestedMovingDirection = movingDirections[3];
	}
	
	public void move(boolean paused)
	{
		if(!paused)
		{
			if(this.currentMovingDirection != this.requestedMovingDirection)
			{
				if(this.x % 36 == 0 && this.y % 36 == 0)
				{
					if(!map.didCollide(x, y, requestedMovingDirection))
					{
						this.currentMovingDirection = this.requestedMovingDirection;
					}	
				}
			}
			
			if(map.didCollide(this.x, this.y, currentMovingDirection))
			{
				this.pacmanAnimationTimer = -1;
				index = 1;
				return;
			}
			else if(this.currentMovingDirection != null && this.pacmanAnimationTimer == -1)
			{
				this.pacmanAnimationTimer = this.pacmanAnimationTimerDefault;
			}
			
			if(currentMovingDirection == "up")
			{	
				this.y -= this.speed;
				this.pacmanRotation = rotation[0];
			}
			else if(currentMovingDirection == "down")
			{	
				this.y += this.speed;
				this.pacmanRotation = rotation[1];
			}
			else if(currentMovingDirection == "left")
			{	
				this.x -= this.speed;
				this.pacmanRotation = rotation[2];
			}
			else if(currentMovingDirection == "right")
			{	
				this.x += this.speed;
				this.pacmanRotation = rotation[3];
			}
			animate();
		}	
	}
	
	public void animate()
	{
		if(this.pacmanAnimationTimer == -1)
		{
			return;
		}
		this.pacmanAnimationTimer--;

		if(this.pacmanAnimationTimer == 0)
		{
			this.pacmanAnimationTimer = this.pacmanAnimationTimerDefault;
			index++;
			if(index == 4)
			{
				index = 0;
			}
		}
	}
	
	public void eatDot()
	{
		if(map.eatDot(this.x, this.y))
		{
			i.playSound("../../../../Resources/waka.wav");
		}
	}
	
	public void eatGhost(List<Enemy> enemiesList, Pac player) 
	{
		if(enemiesList.size() != 0)
		{
			if(enemiesList.get(0).isFlashing())
			{
				Iterator<Enemy> enemyIterator = map.enemiesList.iterator();
				while(enemyIterator.hasNext())
				{
					if(enemyIterator.next().collideWith(player))
					{
						enemyIterator.remove();
						map.score += 100;
					    i.playSound("../../../../Resources/eat_ghost.wav");
					}
				}	
			}
		}
	}

	
	public boolean getIsPowerDotActive()
	{
		return powerDotActive;
	}

	public boolean getIsPowerDotAboutToExpire()
	{
		return powerDotAboutToExpire;
	}
}