package org.dav.pseudoavj;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProgressData
{
	private File currentDirectory;
	private List<File> files;
	
	public ProgressData(File currentDirectory)
	{
		this.currentDirectory = currentDirectory;
		this.files = new LinkedList<>();
	}
	
	public File getCurrentDirectory()
	{
		return currentDirectory;
	}
	
	public void addFile(File file)
	{
		files.add(file);
	}
	
	public List<File> getFiles()
	{
		return Collections.unmodifiableList(files);
	}
}
