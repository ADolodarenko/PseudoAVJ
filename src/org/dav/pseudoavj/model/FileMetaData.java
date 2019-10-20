package org.dav.pseudoavj.model;

import java.util.Date;

public class FileMetaData
{
    public static final String FILE_NAME_STRING = "Column_File_Name_Title";
    public static final String HIDDEN_STRING = "Column_Hidden_Title";
    public static final String CREATED_STRING = "Column_Created_Title";
    public static final String LAST_MODIFIED_STRING = "Column_Last_Modified_Title";
    public static final String LAST_ACCESSED_STRING = "Column_Last_Accessed_Title";

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
