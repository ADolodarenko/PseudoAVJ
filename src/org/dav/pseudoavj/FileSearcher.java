package org.dav.pseudoavj;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class FileSearcher extends SwingWorker<List<File>, ProgressData>
{
	private Queue<File> directories;
	private FileAttrs searchFileAttrs;
	private ResultView<ProgressData, List<File>, String> view;

	private String fileNameMask;
	
	private int scannedDirsCount;
	private int filesFound;
	
	public FileSearcher(File initDirectory, FileAttrs searchFileAttrs, ResultView<ProgressData, List<File>, String> view)
	{
		this.directories = new LinkedList<>();
		this.directories.add(initDirectory);
		
		this.searchFileAttrs = searchFileAttrs;
		this.view = view;
		
		prepareNamePattern();
		
		scannedDirsCount = 0;
		filesFound = 0;
	}
	
	@Override
	protected List<File> doInBackground() throws Exception
	{
		List<File> files = new ArrayList<>();
		
		while (!isCancelled() && !this.directories.isEmpty())
		{
			File currentDirectory = this.directories.remove();
			
			ProgressData data = new ProgressData(currentDirectory);
			
			File[] items = currentDirectory.listFiles();
			
			if (items != null)
				for (File item : items)
					if (item.isDirectory())
						this.directories.add(item);
					else
						if (accept(item))
						{
							files.add(item);
							data.addFile(item);
							
							filesFound++;
						}
					
			publish(data);
			
			scannedDirsCount++;
		}
		
		return files;
	}
	
	private void prepareNamePattern()
	{
		if (searchFileAttrs != null)
		{
			fileNameMask = searchFileAttrs.getNameMask();

			if (fileNameMask != null)
			{
				fileNameMask = fileNameMask.trim();

				if ("".equals(fileNameMask))
					fileNameMask = null;
			}
		}
	}
	
	private boolean accept(File item)
	{
		boolean result = true;
		
		if (searchFileAttrs != null)
		{
			result = checkName(item);
			
			if (result)
			{
				result = checkVisibility(item);
				
				if (result)
					result = checkFileTimes(item);
			}
		}
		
		return result;
	}
	
	private boolean checkName(File file)
	{
		if (fileNameMask != null)
			return file.getName().contains(fileNameMask);
		else
			return true;
	}
	
	private boolean checkVisibility(File file)
	{
		boolean result = true;
		
		FileAttrs.FileVisibility visibility = searchFileAttrs.getVisibility();
		
		if (visibility != null)
		{
			switch (visibility)
			{
				case HIDDEN:
					result = file.isHidden();
					break;
				case VISIBLE:
					result = !file.isHidden();
					break;
			}
		}
		
		return result;
	}
	
	private boolean checkFileTimes(File file)
	{
		Path filePath = file.toPath();
		BasicFileAttributes fileAttributes = null;
		
		try
		{
			fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
		}
		catch (IOException e)
		{
			return false;
		}
		
		return checkFileTime(searchFileAttrs.getFileTimePeriod(), fileAttributes.creationTime()) ||
				checkFileTime(searchFileAttrs.getFileTimePeriod(), fileAttributes.lastModifiedTime()) ||
				checkFileTime(searchFileAttrs.getFileTimePeriod(), fileAttributes.lastAccessTime());
	}
	
	private boolean checkFileTime(FileAttrs.FileTimePeriod period, FileTime time)
	{
		if (period != null)
			return time.compareTo(period.getFrom()) >=0 && time.compareTo(period.getTo()) <= 0;
		else
			return true;
	}
	
	@Override
	protected void process(List<ProgressData> chunks)
	{
		if (isCancelled()) return;
		
		view.updateData(chunks);
	}
	
	@Override
	protected void done()
	{
		view.activateControls();

		String message = scannedDirsCount + " dir(s) scanned; " + filesFound + " file(s) found. Ready.";

		List<File> result = null;

		if (!isCancelled())
			try
			{
				result = get();
			}
			catch (InterruptedException e)
			{}
			catch (ExecutionException e)
			{}
			catch (CancellationException e)
			{}

		view.showResult(result, message);
	}
}
