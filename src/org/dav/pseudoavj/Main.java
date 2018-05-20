package org.dav.pseudoavj;

import org.dav.pseudoavj.view.PAVFrame;

import javax.swing.*;
import java.awt.*;

public class Main
{
	public static void main(String[] args)
	{
		new Main();
	}
	
	static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public Main()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setLookAndFeel();
				
				JFrame mainFrame = new PAVFrame();
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setLocationRelativeTo(null);
				mainFrame.setVisible(true);
			}
		});
	}
}
