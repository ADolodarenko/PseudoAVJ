package org.dav.pseudoavj.view;

import org.dav.pseudoavj.ResourceManager;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

public class Title implements TitleGetter
{
	private String key;
	private List<Object> params;
	
	public Title(String key)
	{
		this.key = key;
		this.params = new LinkedList<>();
	}
	
	public Title(String key, Object... params)
	{
		this(key);
		
		for (Object param : params)
			this.params.add(param);
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getText()
	{
		String text = getComponentTitle(key);
		
		if (text == null)
			return key;
		else if (params.isEmpty())
			return text;
		else
			return String.format(text, params.toArray());
	
	}
	
	@Override
	public String getComponentTitle(String titleKey)
	{
		String result = null;
		
		try
		{
			result = ResourceManager.getInstance().getBundle().getString(titleKey);
		}
		catch (MissingResourceException e)
		{}
		
		return result;
	}
}
