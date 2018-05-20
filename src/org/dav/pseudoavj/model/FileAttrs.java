package org.dav.pseudoavj.model;

import java.nio.file.attribute.FileTime;

public class FileAttrs
{
	private String nameMask;
	private FileVisibility visibility;
	private FileTimePeriod fileTimePeriod;

	public FileAttrs(String nameMask, FileVisibility visibility,
					 Long fileTimePeriodFrom, Long fileTimePeriodTo)
	{
		this.nameMask = nameMask;
		this.visibility = visibility;
		
		setFileTimePeriod(fileTimePeriodFrom, fileTimePeriodTo);
	}
	
	private void setFileTimePeriod(Long from, Long to)
	{
		if (from != null || to != null)
		{
			long fromValue = (from != null)?from.longValue():Long.MIN_VALUE;
			long toValue = (to != null)?to.longValue():Long.MAX_VALUE;
			
			if (toValue < fromValue)
				toValue = Long.MAX_VALUE;
			
			fileTimePeriod = new FileTimePeriod(fromValue, toValue);
		}
	}
	
	public String getNameMask()
	{
		return nameMask;
	}
	
	public FileVisibility getVisibility()
	{
		return visibility;
	}
	
	public FileTimePeriod getFileTimePeriod()
	{
		return fileTimePeriod;
	}
	
	public class FileTimePeriod
	{
		private FileTime from;
		private FileTime to;
		
		public FileTimePeriod(long fromValue, long toValue)
		{
			from = FileTime.fromMillis(fromValue);
			to = FileTime.fromMillis(toValue);
		}
		
		public FileTime getFrom()
		{
			return from;
		}
		
		public FileTime getTo()
		{
			return to;
		}
	}
	
	public enum FileVisibility
	{
		HIDDEN, VISIBLE, ANY
	}
}
