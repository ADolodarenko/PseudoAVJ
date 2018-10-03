package org.dav.pseudoavj.view;

import java.awt.*;

public class TitledComponent
{
	private Component component;
	private String titleKey;
	
	public TitledComponent(Component component, String titleKey)
	{
		this.component = component;
		this.titleKey = titleKey;
	}
	
	public Component getComponent()
	{
		return component;
	}
	
	public String getTitleKey()
	{
		return titleKey;
	}
}
