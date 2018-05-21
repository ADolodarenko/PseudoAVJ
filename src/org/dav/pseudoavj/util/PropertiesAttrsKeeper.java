package org.dav.pseudoavj.util;

import org.dav.pseudoavj.model.FileAttrs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesAttrsKeeper implements AttrsKeeper
{
	private File file;
	
	public PropertiesAttrsKeeper(File file)
	{
		this.file = file;
	}
	
	@Override
	public boolean load(FileAttrs attrs)
	{
		boolean result = false;
		
		if (file.exists())
		{
			Properties properties = new Properties();
			
			try (FileInputStream inputStream = new FileInputStream(file))
			{
				properties.load(inputStream);
				
				String nameMask = properties.getProperty("FileNameMask");
				
				String visibilityString = properties.getProperty("FileVisibility");
				FileAttrs.FileVisibility visibility = null;
				if ("HIDDEN".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.HIDDEN;
				else if ("VISIBLE".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.VISIBLE;
				else if ("ANY".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.ANY;
				
				String from = properties.getProperty("FromDate");
				Long fromValue = null;
				if (from != null)
					fromValue = Long.decode(from);
				
				String to = properties.getProperty("ToDate");
				Long toValue = null;
				if (to != null)
					toValue = Long.decode(to);
				
				attrs = new FileAttrs(nameMask, visibility, fromValue, toValue);
				
				result = true;
			}
			catch (IOException e){}
		}
		
		return result;
	}
	
	@Override
	public boolean save(FileAttrs attrs)
	{
		return false;
	}
}
