package org.dav.pseudoavj.model;

import org.dav.pseudoavj.model.FileMetaData;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProgressDataAdvanced
{
    private File currentDirectory;
    private List<FileMetaData> files;

    public ProgressDataAdvanced(File currentDirectory)
    {
        this.currentDirectory = currentDirectory;
        this.files = new LinkedList<>();
    }

    public File getCurrentDirectory()
    {
        return currentDirectory;
    }

    public void addFile(FileMetaData file)
    {
        files.add(file);
    }

    public List<FileMetaData> getFiles()
    {
        return Collections.unmodifiableList(files);
    }
}
