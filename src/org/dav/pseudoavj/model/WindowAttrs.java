package org.dav.pseudoavj.model;

import org.dav.pseudoavj.ResourceManager;
import org.dav.pseudoavj.util.AttrsKeeper;
import org.dav.pseudoavj.view.AdjustableTitles;

import javax.swing.*;
import java.awt.*;

public class WindowAttrs implements Attrs, AdjustableTitles
{
	private boolean maximized;
	private Point leftTopCorner;
	private Dimension measurements;
	
	public WindowAttrs(){}
	
	public WindowAttrs(boolean maximized, Point leftTopCorner, Dimension measurements)
	{
		this.maximized = maximized;
		this.leftTopCorner = leftTopCorner;
		this.measurements = measurements;
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
			JOptionPane.showMessageDialog(null, getComponentTitle("Fail_Load_Window_Attrs"),
								   getComponentTitle("Warning"), JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void save(AttrsKeeper keeper)
	{
		boolean saved = false;
		
		if (keeper != null)
			saved = keeper.save(this);
		
		if (!saved)
			JOptionPane.showMessageDialog(null, getComponentTitle("Fail_Save_Window_Attrs"),
								  getComponentTitle("Warning"), JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public String getComponentTitle(String key)
	{
		return ResourceManager.getInstance().getBundle().getString(key);
	}
}
