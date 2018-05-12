package org.dav.pseudoavj;

import java.nio.file.attribute.FileTime;

public class FileAttrs
{
	private String nameMask;
	private FileVisibility visibility;
	private FileTimePeriod created;
	private FileTimePeriod lastModified;
	private FileTimePeriod lastAccessed;
	
	public FileAttrs(String nameMask, FileVisibility visibility,
					 Long createdFrom, Long createdTo,
					 Long lastModifiedFrom, Long lastModifiedTo,
					 Long lastAccessedFrom, Long lastAccessedTo)
	{
		this.nameMask = nameMask;
		this.visibility = visibility;
		
		setFileTimePeriod(created, createdFrom, createdTo);
		setFileTimePeriod(lastModified, lastModifiedFrom, lastModifiedTo);
		setFileTimePeriod(lastAccessed, lastAccessedFrom, lastAccessedTo);
	}
	
	private void setFileTimePeriod(FileTimePeriod period, Long from, Long to)
	{
		if (from != null || to != null)
		{
			long fromValue = (from != null)?from.longValue():Long.MIN_VALUE;
			long toValue = (to != null)?to.longValue():Long.MAX_VALUE;
			
			if (toValue < fromValue)
				toValue = Long.MAX_VALUE;
			
			period = new FileTimePeriod(fromValue, toValue);
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
	
	public FileTimePeriod getCreated()
	{
		return created;
	}
	
	public FileTimePeriod getLastModified()
	{
		return lastModified;
	}
	
	public FileTimePeriod getLastAccessed()
	{
		return lastAccessed;
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
