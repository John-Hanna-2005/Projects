package com.hcstonline.jhanna2;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class Manager
{
	public Manager()
	{
		
	}
	
	public Image getImageResources(String fileName)
	{
		URL fileURL = null;
		try
		{
			Class currClass = this.getClass();
			fileURL = new URL(currClass.getResource("Tilemap.class"), fileName);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		} 
		return new ImageIcon(fileURL).getImage();
	}
	
    public void playSound(String file) 
    {
        AudioInputStream sound = null;
        try 
        {
            Class currClass = this.getClass();
            URL fileURL = new URL(currClass.getResource("TileMap.class"), file);
            sound = AudioSystem.getAudioInputStream(fileURL);
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            clip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) 
        {
            e.printStackTrace();
        }
        finally 
        {
            if(sound != null) 
            {
                try 
                {
                    sound.close();
                }
                catch(IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
