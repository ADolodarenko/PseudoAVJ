package org.dav.pseudoavj;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class FileSearcher extends SwingWorker<List<File>, ProgressData>
{
	private Queue<File> directories;
	private PAVFrame frame;
	
	public FileSearcher(File initDirectory, PAVFrame frame)
	{
		this.directories = new LinkedList<>();
		this.directories.add(initDirectory);
		
		this.frame = frame;
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
						if (isAccurate(item))
						{
							files.add(item);
						
							data.addFile(item);
						}
					
			publish(data);
		}
		
		return files;
	}
	
	private boolean isAccurate(File item)
	{
		return true;
	}
	
	@Override
	protected void process(List<ProgressData> chunks)
	{
		if (isCancelled()) return;
		
		frame.updateData(chunks);
	}
	
	@Override
	protected void done()
	{
		try
		{
			frame.fillData(get());
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
	}
}
