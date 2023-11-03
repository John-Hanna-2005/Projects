package com.gmail.jhanna;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;


public class Drawer extends JPanel implements KeyListener, ActionListener, MouseListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -244779921517989757L;
	private final int WIDTH = 800;
	private final int HEIGHT = 500;
	private Bird bird;
	private PipeGenerator pipe;
	private boolean gameStarted;
	private boolean gameOver;
	private boolean gameClicked;
	private boolean gamePaused;
	private JButton startBt;
	private JButton restartBt;
	private Timer clock;
	private final int FPS = 60;
	private Image gameBg;
	private int score;
	String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	int rand = (int)(Math.random() * fonts.length - 1) + 0;
	String font = "Britannic Bold";

	public Drawer(JFrame frame)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(new Color(65, 160, 210));
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
		startBt = new JButton("Start");
		this.add(startBt);
		startBt.setBounds(320, 300, 150, 50);
		startBt.addActionListener(this);
		startBt.setBackground(new Color(248, 248, 211));
		startBt.setFont(new Font(font, Font.BOLD, 20));
		startBt.setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.gameBg = getImageResources("../../../../Resources/bg.png");
		
		restartBt = new JButton("Restart");
		this.add(restartBt);
		restartBt.setBounds(320, 300, 140, 40);
		restartBt.addActionListener(this);
		restartBt.setBackground(new Color(65, 160, 255));
		restartBt.setForeground(new Color(0, 0, 0));
		restartBt.setFont(new Font(font, Font.BOLD, 20));
		restartBt.setVisible(false);
		restartBt.setBorder(BorderFactory.createRaisedBevelBorder());
		
		gameStarted = false;
		gameOver = false;
		gameClicked = false;
		gamePaused = false;
		score = 0;

		bird = new Bird(HEIGHT / 2, 60, 40, getImageResources("../../../../Resources/bird.png"));
		pipe = new PipeGenerator();
		
		clock = new Timer(1000 / FPS, this);
		clock.start();
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
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(gameStarted)
		{
			g.drawImage(gameBg, 0, 0, null);
			startBt.setVisible(false);
			pipe.drawPipe(g);
			if(gameOver)
			{
				g.setColor(new Color(200, 35, 35));
				g.setFont(new Font(font, Font.BOLD, 100)); 
				g.drawString("Game Over", 150, 200);
			
			}
			else if(!gameClicked)
			{
				g.setColor(new Color(70, 82, 0));
				g.setFont(new Font(font, Font.BOLD, 50)); 
				g.drawString("SPACE --> Start", 220, 160);
				g.drawString("P --> Pause", 250, 210);
				
			}
			else if(gamePaused)
			{
				g.setColor(new Color(240, 230, 5));
				g.setFont(new Font(font, Font.BOLD, 50)); 
				g.drawString("Press P to Continue", 150, 200);
			}
			
			g.setColor(new Color(224, 184, 38));
			g.fillRect(0, 450, 800, 50);
			
			g.setColor(new Color(140, 9, 0));
			g.setFont(new Font(font, Font.BOLD, 30));
			bird.birdMaker(g.create());
			g.drawString("Score:", 30, 50);
			g.drawString(score + "", 130, 50);

		}
		else if(!gameStarted)
		{
			g.setFont(new Font(font, Font.BOLD, 100));
			g.drawString("Flappy Bird", 150, 200);
			g.setFont(new Font(font, Font.BOLD, 30));
			g.setColor(new Color(140, 9, 0));
			g.drawString("By: John Hanna", 540, 460);
			
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(gameStarted && !gameOver)
		{
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				gameClicked = true;
				bird.jump();
				
//				System.out.println(bird.getXcord() + " <--x   y--> " + bird.getYcord());
			}
			else if(e.getKeyCode() == KeyEvent.VK_P)
			{
				if(gamePaused)
				{
					gamePaused = false;
				}
				else
				{
					gamePaused = true;
				}
			}
		
			
		} 
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{

		if(!gameStarted)
		{
			if(e.getSource() == startBt)
			{
				gameStarted = true;
				
			}
		}
		else if(gameStarted)
		{
			if(gameClicked && !gameOver && !gamePaused)
			{
				bird.setSpeed(bird.getSpeed() + bird.getAcc());
				bird.setYcord(bird.getYcord() + bird.getSpeed());
				pipe.spawnPipes();
				pipe.movePipe();
				if(bird.y == 400.0 || pipe.checkCollision(bird))
				{ 
					restartBt.setVisible(true);
					gameOver = true;
				}
				else if(pipe.checkScore(bird))
				{
					score++;
				}	
			}
			
		}
		if(gameOver)
		{
			if(e.getSource() == restartBt)
			{
				restartBt.setVisible(false);
				startGame();
				
			}
		}
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		
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
}
