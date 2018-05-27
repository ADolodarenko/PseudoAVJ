package org.dav.pseudoavj.util;

import org.dav.pseudoavj.ResourceManager;
import org.dav.pseudoavj.model.FileAttrs;
import org.dav.pseudoavj.model.WindowAttrs;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class PropertiesAttrsKeeper implements AttrsKeeper
{
	private final static String MASK_STR = "FileNameMask";
	private final static String VISIBILITY_STR = "FileVisibility";
	private final static String FROM_STR = "FromDate";
	private final static String TO_STR = "ToDate";

	private Properties properties;
	
	public PropertiesAttrsKeeper(Properties properties)
	{
		this.properties = properties;
	}
	
	@Override
	public boolean load(FileAttrs attrs)
	{
		boolean result = false;
		
		if (attrs != null && properties != null)
		{
			attrs.setNameMask(properties.getProperty("FileNameMask"));
			
			String visibilityString = properties.getProperty("FileVisibility");
			FileAttrs.FileVisibility visibility = null;
			if ("HIDDEN".equalsIgnoreCase(visibilityString)) visibility = FileAttrs.FileVisibility.HIDDEN;
			else if ("VISIBLE".equalsIgnoreCase(visibilityString)) visibility = FileAttrs.FileVisibility.VISIBLE;
			else if ("ANY".equalsIgnoreCase(visibilityString)) visibility = FileAttrs.FileVisibility.ANY;
			
			attrs.setVisibility(visibility);
			
			String from = properties.getProperty("FromDate");
			Long fromValue = null;
			if (from != null) fromValue = Long.decode(from);
			
			String to = properties.getProperty("ToDate");
			Long toValue = null;
			if (to != null) toValue = Long.decode(to);
			
			if (fromValue != null || toValue != null) attrs.setFileTimePeriod(fromValue, toValue);
			
			result = true;
		}
		
		return result;
	}
	
	@Override
	public boolean load(WindowAttrs attrs)
	{
		boolean result = false;
		
		if (attrs != null && properties != null)
		{
			String localeString = properties.getProperty("ApplicationLocale");
			if ("RU".equalsIgnoreCase(localeString))
				attrs.setLocale(ResourceManager.RUS_LOCALE);
			else
				attrs.setLocale(ResourceManager.ENG_LOCALE);
			
			String maximizedString = properties.getProperty("WindowMaximized");
			if ("true".equalsIgnoreCase(maximizedString))
				attrs.setMaximized(true);
			else
				attrs.setMaximized(false);
			
			int x = 0, y = 0;
			String xString = properties.getProperty("WindowX");
			String yString = properties.getProperty("WindowY");
			
			try
			{
				if (xString != null) x = Integer.parseInt(xString);
				if (yString != null) y = Integer.parseInt(yString);
			}
			catch (NumberFormatException e){}
			
			attrs.setLeftTopCorner(new Point(x, y));
			
			int width = 400, height = 300;
			String widthString = properties.getProperty("WindowWidth");
			String heightString = properties.getProperty("WindowHeight");
			
			try
			{
				if (widthString != null) width = Integer.parseInt(widthString);
				if (heightString != null) height = Integer.parseInt(heightString);
			}
			catch (NumberFormatException e){}
			
			attrs.setMeasurements(new Dimension(width, height));
			
			result = true;
		}
		
		return result;
	}
	
	@Override
	public boolean save(FileAttrs attrs)
	{
		boolean result = false;

		if (attrs != null && properties != null)
		{
			properties.remove(MASK_STR);
			String nameMask = attrs.getNameMask();
			if (nameMask != null)
				properties.setProperty(MASK_STR, nameMask);

			properties.remove(VISIBILITY_STR);
			FileAttrs.FileVisibility visibility = attrs.getVisibility();
			if (visibility != null)
				properties.setProperty(VISIBILITY_STR, visibility.toString());

			properties.remove(FROM_STR);
			properties.remove(TO_STR);
			FileAttrs.FileTimePeriod period = attrs.getFileTimePeriod();
			if (period != null)
			{
				Long from = period.getFrom().toMillis();
				Long to = period.getTo().toMillis();

				properties.setProperty(FROM_STR, String.valueOf(from));
				properties.setProperty(TO_STR, String.valueOf(to));
			}
			
			result = true;
		}

		return result;
	}
	
	@Override
	public boolean save(WindowAttrs attrs)
	{
		boolean result = false;
		
		if (attrs != null && properties != null)
		{
			properties.setProperty("ApplicationLocale",
								   attrs.getLocale() == ResourceManager.RUS_LOCALE ? "RU" : "EN");
			
			properties.setProperty("WindowMaximized", String.valueOf(attrs.isMaximized()));
			
			properties.setProperty("WindowX", String.valueOf(attrs.getLeftTopCorner().x));
			properties.setProperty("WindowY", String.valueOf(attrs.getLeftTopCorner().y));
			
			properties.setProperty("WindowWidth", String.valueOf(attrs.getMeasurements().width));
			properties.setProperty("WindowHeight", String.valueOf(attrs.getMeasurements().height));
			
			result = true;
		}
		
		return result;
	}
}
