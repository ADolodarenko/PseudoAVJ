package org.dav.pseudoavj;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileSearcher extends SwingWorker<List<File>, File>
{
	private Queue<File> directories;
	private JLabel statusLabel;
	
	public FileSearcher(File initDirectory, JLabel statusLabel)
	{
		this.directories = new LinkedList<>();
		this.directories.add(initDirectory);
		
		this.statusLabel = statusLabel;
	}
	
	@Override
	protected List<File> doInBackground() throws Exception
	{
		List<File> files = new ArrayList<>();
		
		while (!isCancelled() && !this.directories.isEmpty())
		{
			File currentDirectory = this.directories.remove();
			
			File[] items = currentDirectory.listFiles();
			for (File item : items)
			{
				if (item.isDirectory())
					this.directories.add(item);
				//else do checking here
			}
			
			publish(currentDirectory);
			
			Thread.sleep(100);
		}
		
		return files;
	}
	
	@Override
	protected void process(List<File> chunks)
	{
		this.statusLabel.setText(chunks.get(chunks.size() - 1).getAbsolutePath());
	}
	
	@Override
	protected void done()
	{
		this.statusLabel.setText("Ready.");
	}
}
