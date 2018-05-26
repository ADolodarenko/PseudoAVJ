package org.dav.pseudoavj.model;

import org.dav.pseudoavj.ResourceManager;
import org.dav.pseudoavj.util.AttrsKeeper;
import org.dav.pseudoavj.view.AdjustableTitles;

import javax.swing.*;
import java.nio.file.attribute.FileTime;

public class FileAttrs implements Attrs, AdjustableTitles
{
	private String nameMask;
	private FileVisibility visibility;
	private FileTimePeriod fileTimePeriod;

	public FileAttrs(){}
	
	public FileAttrs(String nameMask, FileVisibility visibility,
					 Long fileTimePeriodFrom, Long fileTimePeriodTo)
	{
		this.nameMask = nameMask;
		this.visibility = visibility;
		
		setFileTimePeriod(fileTimePeriodFrom, fileTimePeriodTo);
	}

	public void setNameMask(String nameMask)
	{
		this.nameMask = nameMask;
	}

	public void setVisibility(FileVisibility visibility)
	{
		this.visibility = visibility;
	}

	public void setFileTimePeriod(Long from, Long to)
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
	
	@Override
	public void save(AttrsKeeper keeper)
	{
		boolean saved = false;

		if (keeper != null)
			saved = keeper.save(this);

		if (!saved) JOptionPane.showMessageDialog(null, getComponentTitle("Fail_Save_Search_Attrs"),
				getComponentTitle("Warning"), JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void load(AttrsKeeper keeper)
	{
		boolean loaded = false;
		
		if (keeper != null)
			loaded = keeper.load(this);
		
		if (!loaded) JOptionPane.showMessageDialog(null, getComponentTitle("Fail_Load_Search_Attrs"),
												   getComponentTitle("Warning"), JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public String getComponentTitle(String key)
	{
		return ResourceManager.getInstance().getBundle().getString(key);
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
