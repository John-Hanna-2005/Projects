package com.hcstonline.jhanna2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tilemap extends JPanel implements KeyListener, ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	private Timer clock;
	private final int FPS = 60;
	private final int tileSize = 36;

	private Image jailcell;
	private Image rat;
	private Image pellet;
	private Image chair;
	private Image john;
	private Image will;
	private Image title;
	private JButton startBtn;

	
	private boolean gameStarted;
	private boolean gamePaused;
	private boolean gameWin;
	private boolean gameOver;
	private boolean soundPlayed;
	
	private final int speed = 2;
	private int lives;
	public int score;
	private int johnSpeed;
	private double willSpeed;
	private int willY;
	private int johnY;
	private int dist;
	
	private Pac player;
	public List<Enemy> enemiesList = new ArrayList<Enemy>(5);
	String font = "Segoe UI Black";

	
	private Manager i = new Manager();
	private Maps maps = new Maps();
	private int[][] map;
	
	
	// 0 = pellet
	// 1 = wall
	// 2 = rat
	// 4 = pacman
	// 5 = blank
	// 6 = enemy



	public Tilemap(JFrame frame)
	{
		this.setPreferredSize(new Dimension(1400, 800));
		this.setBackground(new Color(200, 200, 200));
		this.setLayout(null);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		startGame();
	}
	
	public void startGame()
	{
		if(clock != null)
		{
			clock.stop();
		}
		map = maps.getMap();
		
		startBtn = new JButton("Start");
		this.add(startBtn);
		startBtn.setBounds(600, 500, 150, 50);
		startBtn.addActionListener(this);
		startBtn.setBackground(new Color(248, 248, 211));
		startBtn.setFont(new Font(font, Font.BOLD, 20));
		startBtn.setBorder(BorderFactory.createRaisedBevelBorder());
		
		
		this.jailcell = i.getImageResources("../../../../Resources/jailcell.jpg");
		this.rat = i.getImageResources("../../../../Resources/rat.png");
		this.pellet = i.getImageResources("../../../../Resources/pellet1.png");
		this.chair = i.getImageResources("../../../../Resources/chair.jpg");
		this.john = i.getImageResources("../../../../Resources/john.png");
		this.will = i.getImageResources("../../../../Resources/will.png");
		this.title = i.getImageResources("../../../../Resources/title.png");
		
		player = getPacman(speed);
		enemiesList = getEnemies(speed);

		gameStarted = false;
		gamePaused = true;
		gameWin = false;
		gameOver = false;
		soundPlayed = false;
		
		lives = 100;
		score = 0;
		dist = 300;
		willSpeed = dist/dotsLeft() + 1;
		johnSpeed = 400/lives;
		
		willY = 600;
		johnY = 600;
		
		clock = new Timer(1000 / FPS, this);
		clock.start();
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(gameStarted)
		{
			if(!soundPlayed)
			{
				i.playSound("../../../../Resources/gameWin.wav");
				soundPlayed = true;
			}
			startBtn.setVisible(false);
			
			
			drawMap(g);
			drawChair(g);
			if(!gameOver || !gameWin)
			{
				player.move(gamePaused);
				player.eatDot();
				if(this.eatPowerDot(player.getX(), player.getY()))
				{
					for(Enemy enemy : enemiesList)
					{
						enemy.makeFlashing();
					}
				}
				player.eatGhost(enemiesList, player);
				minusLife();
				drawJohn(g);
				drawWill(g);
			}
			drawEnemies(g);
			checkGameOver();
			checkGameWon();
			
			g.setFont(new Font(font, Font.BOLD, 25));
			g.setColor(new Color(140, 0, 0));
			g.drawString("Health: " + lives, 20, 100);
			g.setColor(new Color(3, 99, 0));
			g.drawString("Score: " + score, 20, 150);
			
			if(gameWin)
			{
				g.setFont(new Font(font, Font.BOLD, 35));
				g.setColor(new Color(3, 99, 0));
				g.drawString("You Won!", 1050, 750);
			}
			else if(gameOver)
			{
				g.setFont(new Font(font, Font.BOLD, 35));
				g.setColor(new Color(140, 0, 0));
				g.drawString("You Lost...", 1050, 750);
			}
			
			drawPac(g);	
		}
		else if(!gameStarted)
		{
			g.setColor(new Color(144, 255, 140));
			g.fillRect(0, 0, 1400, 800);
			g.drawImage(title, 100, 0, this);
			g.setFont(new Font(font, Font.BOLD, 50));
			g.setColor(new Color(4, 100, 0));
			g.drawString("The Green Mile Edition", 400, 450);
			g.setFont(new Font(font, Font.BOLD, 30));
			g.setColor(new Color(140, 9, 0));
		}
		
	}
	
	public void playAgain()
	{
		startGame();
	}
	
	public void checkGameOver()
	{
		if(!gameOver)
		{
			gameOver = isGameOver();
			if(gameOver)
			{
				i.playSound("../../../../Resources/gameOver.wav");
				gamePaused = true;
			}
		}
	}
	
	public void minusLife()
	{
		if(enemiesList.stream().anyMatch(enemy -> !player.getIsPowerDotActive() && enemy.collideWith(player)))
		{
			
			if(lives > 0)
			{
				i.playSound("../../../../Resources/bonk.wav");
				lives--;
				johnY -= johnSpeed;
			}
		}
	}
	
	public boolean isGameOver()
	{
		if(lives == 0)
		{
			return true;
		}
		return false;
	}
	
	public void checkGameWon()
	{
		if(!gameWin)
		{
			gameWin = isGameWon();
			if(gameWin)
			{
				i.playSound("../../../../Resources/gameWin.wav");
				gamePaused = true;
			}
		}
	}
	
	public boolean isGameWon()
	{
		return dotsLeft() == 0;
	}
	
	public int dotsLeft()
	{
		int dots = 0;
		int tile;
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
			{
				tile = map[c][r];
				if(tile == 0)
				{
					dots++;
				}
			}
		}
		return dots;
	}
	
	public void drawMap(Graphics g)
	{
		int tile;
		g.setColor(new Color(0, 0, 0));
		g.fillRect(214, 106, tileSize*16, tileSize*16);
		
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
			{
				tile = map[c][r];
				if(tile == 1)
				{
					drawWall(g, r, c, tileSize);
				}
				else if(tile == 0)
				{
					drawPellet(g, r, c, tileSize);
				}
				else if(tile == 2)
				{
					drawRat(g, r, c, tileSize);
				}
			}
		}
	}
	
	
	public void drawWall(Graphics g, int row, int col, int tileSize)
	{
		g.drawImage(jailcell, row * tileSize + 178, col * tileSize + 70, this);
	}
	
	public void drawPellet(Graphics g, int row, int col, int tileSize)
	{
		g.drawImage(pellet, row * tileSize + 190, col * tileSize + 82, this);
	}
	
	public void drawRat(Graphics g, int row, int col, int tileSize)
	{
		g.drawImage(rat, row * tileSize + 184, col * tileSize + 76, this);
	}
	
	public void drawChair(Graphics g)
	{
		g.drawImage(chair, 1000, 80, this);
	}

	public void drawWill(Graphics g)
	{
		g.setColor(new Color(3, 99, 0));
		g.fillRect(950, willY + 90, 135, 600-willY);
		g.drawImage(will, 950, willY, this);
	}
	
	public void drawJohn(Graphics g)
	{
		g.setColor(new Color(196, 26, 26));
		g.fillRect(1165, johnY + 90, 125, 600-johnY);
		g.drawImage(john, 1150, johnY, this);
	}
	
	
	
	public void drawPac(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;

		int size = this.tileSize/2 - 1;
		g2D.translate(player.getX() + size, player.getY() + size);
		g2D.rotate((player.getPacmanRotation() * 90 * Math.PI)/180);
		g2D.drawImage(player.getPhase(), -size, -size, this);
		
	}
	
	public void drawEnemies(Graphics g)
	{
		for(int i = 0; i < enemiesList.size(); i++)
		{
			enemiesList.get(i).drawEnemy(g, gamePaused, System.currentTimeMillis());
		}
	}
	
	public Pac getPacman(int speed)
	{
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
			{
				int tile = map[c][r];
				if(tile == 4)
				{
					map[c][r] = 0;
					return new Pac(r*tileSize + 180, c*tileSize + 72, tileSize, speed, this);
				}
			}
		}
		return null;
	}
	
	public List<Enemy> getEnemies(int speed)
	{
		int count = 0;
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
			{
				int tile = map[c][r];
				if(tile == 6)
				{
					map[c][r] = 0;
					enemiesList.add(count, new Enemy(r*tileSize + 180, c*tileSize + 72, tileSize, speed, count, this));
					count++;
				}  
			}
		}
		return enemiesList;
	}
	
	public boolean didCollide(int x, int y, String newMoveDirection)
	{
		if(newMoveDirection == null)
		{
			return false;
		}
		int col = 0;
		int row = 0;
		int nextCol = 0;
		int nextRow = 0;
		int tile;
		if(x % 36 == 0 && y % 36 == 0)
		{
			if(newMoveDirection == "right")
			{
				nextCol = x + this.tileSize - 180;
				col = nextCol/this.tileSize;
				row = (y - 72)/this.tileSize;
			}
			else if(newMoveDirection == "left")
			{
				nextCol = x - this.tileSize - 180;
				col = nextCol/this.tileSize;
				row = (y - 72)/this.tileSize;
			}
			else if(newMoveDirection == "up")
			{
				nextRow = y - this.tileSize - 72;
				row = nextRow/this.tileSize;
				col = (x - 180)/this.tileSize;
			}
			else if(newMoveDirection == "down")
			{
				nextRow = y + this.tileSize - 72;
				row = nextRow/this.tileSize;
				col = (x - 180)/this.tileSize;
			}
		
			tile = map[row][col];
			if(tile == 1)
			{
				return true;
			}
			
		}
		return false;
	}
	
	public boolean eatDot(int x, int y)
	{
		int row = (y - 55) / this.tileSize;
		int col = (x - 160) / this.tileSize;

		if(map[row][col] == 0)
		{
			map[row][col] = 5;
			willY-= willSpeed;
			score += 2;
			return true;
		}
		return false;
	}
	
	public boolean eatPowerDot(int x, int y)
	{
		int row = (y - 55) / this.tileSize;
		int col = (x - 160) / this.tileSize;

		if(map[row][col] == 2)
		{
			map[row][col] = 5;
			i.playSound("../../../../Resources/power_dot.wav");
			return true;
		}
		return false; 
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
		if(!gameStarted)
		{
			if(e.getSource() == startBtn)
			{
				gameStarted = true;
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(gameStarted && !gameOver && !gameWin)
		{
			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				player.moveUp();
				gamePaused = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				player.moveDown();
				gamePaused = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				player.moveLeft();
				gamePaused = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				player.moveRight();
				gamePaused = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
