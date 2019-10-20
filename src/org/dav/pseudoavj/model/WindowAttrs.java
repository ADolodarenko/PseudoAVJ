package org.dav.pseudoavj.model;

import org.dav.pseudoavj.util.AttrsKeeper;
import org.dav.pseudoavj.view.Title;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class WindowAttrs implements Attrs
{
	private Locale locale;
	private boolean maximized;
	private Point leftTopCorner;
	private Dimension measurements;
	
	public WindowAttrs(){}
	
	public WindowAttrs(Locale locale, boolean maximized, Point leftTopCorner, Dimension measurements)
	{
		this.locale = locale;
		this.maximized = maximized;
		this.leftTopCorner = leftTopCorner;
		this.measurements = measurements;
	}

	public Locale getLocale()
	{
		return locale;
	}
	
	public boolean isMaximized()
	{
		return maximized;
	}
	
	public Point getLeftTopCorner()
	{
		return leftTopCorner;
	}
	
	public Dimension getMeasurements()
	{
		return measurements;
	}
	
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
	
	public void setMaximized(boolean maximized)
	{
		this.maximized = maximized;
	}
	
	public void setLeftTopCorner(Point leftTopCorner)
	{
		this.leftTopCorner = leftTopCorner;
	}
	
	public void setMeasurements(Dimension measurements)
	{
		this.measurements = measurements;
	}
	
	@Override
	public void load(AttrsKeeper keeper)
	{
		boolean loaded = false;
		
		if (keeper != null)
			loaded = keeper.load(this);
		
		if (!loaded)
			JOptionPane.showMessageDialog(null,
										  Title.getTitleString("Fail_Load_Window_Attrs"),
										  Title.getTitleString("Warning"),
										  JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void save(AttrsKeeper keeper)
	{
		boolean saved = false;
		
		if (keeper != null)
			saved = keeper.save(this);
		
		if (!saved)
			JOptionPane.showMessageDialog(null,
										  Title.getTitleString("Fail_Save_Window_Attrs"),
										  Title.getTitleString("Warning"),
										  JOptionPane.WARNING_MESSAGE);
	}
}
