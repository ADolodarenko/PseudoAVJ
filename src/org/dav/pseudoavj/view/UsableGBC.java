package org.dav.pseudoavj.view;

import java.awt.*;

public class UsableGBC extends GridBagConstraints
{
	public UsableGBC()
	{
		super();
	}
	
	public UsableGBC(int gridX, int gridY)
	{
		this.gridx = gridX;
		this.gridy = gridY;
	}
	
	public UsableGBC(int gridX, int gridY, int gridWidth, int gridHeight)
	{
		this(gridX, gridY);
		
		this.gridwidth = gridWidth;
		this.gridheight = gridHeight;
	}
	
	public UsableGBC setGridX(int gridX)
	{
		this.gridx = gridX;
		
		return this;
	}
	
	public UsableGBC setGridY(int gridY)
	{
		this.gridy = gridY;
		
		return this;
	}
	
	public UsableGBC setGridWidth(int gridWidth)
	{
		this.gridwidth = gridWidth;
		
		return this;
	}
	
	public UsableGBC setGridHeight(int gridHeight)
	{
		this.gridheight = gridHeight;
		
		return this;
	}
	
	public UsableGBC setWeightX(double weightX)
	{
		this.weightx = weightX;
		
		return this;
	}
	
	public UsableGBC setWeightY(double weightY)
	{
		this.weighty = weightY;
		
		return this;
	}
	
	public UsableGBC setAnchor(int anchor)
	{
		this.anchor = anchor;
		
		return this;
	}
	
	public UsableGBC setFill(int fill)
	{
		this.fill = fill;
		
		return this;
	}
	
	public UsableGBC setInsets(Insets insets)
	{
		this.insets = insets;
		
		return this;
	}
	
	public UsableGBC setIpadX(int ipadX)
	{
		this.ipadx = ipadX;
		
		return this;
	}
	
	public UsableGBC setIpadY(int ipadY)
	{
		this.ipady = ipadY;
		
		return this;
	}
}
