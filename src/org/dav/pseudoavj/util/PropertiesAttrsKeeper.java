package org.dav.pseudoavj.util;

import org.dav.pseudoavj.model.FileAttrs;

import java.io.*;
import java.util.Properties;

public class PropertiesAttrsKeeper implements AttrsKeeper
{
	private final static String MASK_STR = "FileNameMask";
	private final static String VISIBILITY_STR = "FileVisibility";
	private final static String FROM_STR = "FromDate";
	private final static String TO_STR = "ToDate";

	private File file;
	
	public PropertiesAttrsKeeper(File file)
	{
		this.file = file;
	}
	
	@Override
	public boolean load(FileAttrs attrs)
	{
		boolean result = false;
		
		if (attrs != null && file.exists())
		{
			Properties properties = new Properties();
			
			try (FileInputStream stream = new FileInputStream(file))
			{
				properties.load(stream);
				
				attrs.setNameMask(properties.getProperty("FileNameMask"));
				
				String visibilityString = properties.getProperty("FileVisibility");
				FileAttrs.FileVisibility visibility = null;
				if ("HIDDEN".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.HIDDEN;
				else if ("VISIBLE".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.VISIBLE;
				else if ("ANY".equalsIgnoreCase(visibilityString))
					visibility = FileAttrs.FileVisibility.ANY;

				attrs.setVisibility(visibility);
				
				String from = properties.getProperty("FromDate");
				Long fromValue = null;
				if (from != null)
					fromValue = Long.decode(from);
				
				String to = properties.getProperty("ToDate");
				Long toValue = null;
				if (to != null)
					toValue = Long.decode(to);

				if (fromValue != null || toValue != null)
					attrs.setFileTimePeriod(fromValue, toValue);
				
				result = true;
			}
			catch (IOException e){}
		}
		
		return result;
	}
	
	@Override
	public boolean save(FileAttrs attrs)
	{
		boolean result = false;

		if (attrs != null)
		{
			Properties properties = new Properties();

			String nameMask = attrs.getNameMask();
			if (nameMask != null)
				properties.setProperty(MASK_STR, nameMask);

			FileAttrs.FileVisibility visibility = attrs.getVisibility();
			if (visibility != null)
				properties.setProperty(VISIBILITY_STR, visibility.toString());

			FileAttrs.FileTimePeriod period = attrs.getFileTimePeriod();
			if (period != null)
			{
				Long from = period.getFrom().toMillis();
				Long to = period.getTo().toMillis();

				properties.setProperty(FROM_STR, String.valueOf(from));
				properties.setProperty(TO_STR, String.valueOf(to));
			}

			try (FileWriter writer = new FileWriter(file))
			{
				properties.store(writer, null);

				result = true;
			}
			catch (IOException e) {}
		}

		return result;
	}
}
