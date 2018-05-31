package org.dav.pseudoavj.logic;

import org.dav.pseudoavj.model.FileAttrs;
import org.dav.pseudoavj.model.FileMetaData;
import org.dav.pseudoavj.model.IntPair;
import org.dav.pseudoavj.model.ProgressDataAdvanced;
import org.dav.pseudoavj.view.ResultView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class FileSearcherAdvanced extends SwingWorker<List<Object>, Object>
{
    private Queue<File> directories;
    private FileAttrs searchFileAttrs;
    private ResultView<Object, List<Object>, IntPair> view;

    private String fileNameMask;

    private int scannedDirsCount;
    private int filesFound;

    public FileSearcherAdvanced(File initDirectory, FileAttrs searchFileAttrs, ResultView<Object, List<Object>, IntPair> view)
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
    protected List<Object> doInBackground() throws Exception
    {
        List<Object> files = new ArrayList<>();

        while (!isCancelled() && !this.directories.isEmpty())
        {
            File currentDirectory = this.directories.remove();

            ProgressDataAdvanced data = new ProgressDataAdvanced(currentDirectory);

            File[] items = currentDirectory.listFiles();

            if (items != null)
                for (File item : items)
                    if (item.isDirectory())
                        this.directories.add(item);
                    else
                    {
                        FileMetaData file = accept(item);

                        if (file != null)
                        {
                            files.add(file);
                            data.addFile(file);

                            filesFound++;
                        }
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

    private FileMetaData accept(File file)
    {
        String fileName = file.getName();
        boolean hidden = file.isHidden();
        FileTime created = null;
        FileTime lastModified = null;
        FileTime lastAccessed = null;

        BasicFileAttributes attributes = getFileAttributes(file);
        if (attributes != null)
        {
            created = attributes.creationTime();
            lastModified = attributes.lastModifiedTime();
            lastAccessed = attributes.lastAccessTime();
        }

        FileMetaData result = null;

        if (checkName(fileName) && checkVisibility(hidden) && checkFileTimes(attributes))
            result = new FileMetaData(file.getAbsolutePath(), hidden,
                    created != null ? new Date(created.toMillis()) : null,
                    lastModified != null ? new Date(lastModified.toMillis()) : null,
                    lastAccessed != null ? new Date(lastAccessed.toMillis()) : null);


        return result;
    }

    private boolean checkName(String name)
    {
        if (fileNameMask != null)
            return name.contains(fileNameMask);
        else
            return true;
    }

    private boolean checkVisibility(boolean hidden)
    {
        boolean result = true;

        FileAttrs.FileVisibility visibility = searchFileAttrs.getVisibility();

        if (visibility != null)
        {
            switch (visibility)
            {
                case HIDDEN:
                    result = hidden;
                    break;
                case VISIBLE:
                    result = !hidden;
                    break;
            }
        }

        return result;
    }

    private boolean checkFileTimes(BasicFileAttributes fileAttributes)
    {
        FileAttrs.FileTimePeriod period = searchFileAttrs.getFileTimePeriod();

        if (period == null)
            return true;
        else
        {
            if (fileAttributes == null)
                return false;
            else
                return checkFileTime(period, fileAttributes.creationTime()) ||
                    checkFileTime(period, fileAttributes.lastModifiedTime()) ||
                    checkFileTime(period, fileAttributes.lastAccessTime());
        }
    }

    private boolean checkFileTime(FileAttrs.FileTimePeriod period, FileTime time)
    {
        if (period != null)
            return time.compareTo(period.getFrom()) >=0 && time.compareTo(period.getTo()) <= 0;
        else
            return true;
    }

    private BasicFileAttributes getFileAttributes(File file)
    {
        Path filePath = file.toPath();
        BasicFileAttributes result = null;

        try
        {
            result = Files.readAttributes(filePath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        }
        catch (IOException e)
        {}

        return result;
    }

    @Override
    protected void process(List<Object> chunks)
    {
        if (isCancelled()) return;

        view.updateData(chunks);
    }

    @Override
    protected void done()
    {
        view.activateControls();

        List<Object> result = null;

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

        view.showResult(result, new IntPair(scannedDirsCount, filesFound));
    }
}
