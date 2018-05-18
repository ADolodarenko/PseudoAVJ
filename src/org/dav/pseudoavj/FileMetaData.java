package org.dav.pseudoavj;

import java.util.Date;

public class FileMetaData
{
    public static final String FILE_NAME = "File name";
    public static final String HIDDEN = "Hidden file";
    public static final String CREATED = "File created at";
    public static final String LAST_MODIFIED = "Date of last modify";
    public static final String LAST_ACCESSED = "Date of last access";

    public static final int FIELD_QUANTITY = 5;

    private String name;
    private boolean hidden;
    private Date created;
    private Date lastModified;
    private Date lastAccessed;

    public FileMetaData(String name, boolean hidden, Date created, Date lastModified, Date lastAccessed)
    {
        this.name = name;
        this.hidden = hidden;
        this.created = created;
        this.lastModified = lastModified;
        this.lastAccessed = lastAccessed;
    }

    public String getName()
    {
        return name;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public Date getCreated()
    {
        return created;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public Date getLastAccessed()
    {
        return lastAccessed;
    }
}
