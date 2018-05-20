package org.dav.pseudoavj.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileMetaDataTableModel extends AbstractTableModel
{
    private List<FileMetaData> data;
    
    public FileMetaDataTableModel()
    {
        this.data = new ArrayList<>();
    }

    public FileMetaDataTableModel(List<FileMetaData> data)
    {
        this.data = data;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount()
    {
        if (this.data != null)
            return this.data.size();
        else
            return 0;
    }
    
    @Override
    public int getColumnCount()
    {
        return FileMetaData.FIELD_QUANTITY;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object result = null;

        if (data != null  && rowIndex < getRowCount())
        {
            FileMetaData row = data.get(rowIndex);

            if (row != null)
            {
                switch (columnIndex)
                {
                    case 0:
                        result = row.getName();
                        break;
                    case 1:
                        result = row.isHidden();
                        break;
                    case 2:
                        result = row.getCreated();
                        break;
                    case 3:
                        result = row.getLastModified();
                        break;
                    case 4:
                        result = row.getLastAccessed();
                        break;
                }
            }
        }

        return result;
    }
    
    @Override
    public String getColumnName(int column)
    {
        switch (column)
        {
            case 0:
                return FileMetaData.FILE_NAME;
            case 1:
                return FileMetaData.HIDDEN;
            case 2:
                return FileMetaData.CREATED;
            case 3:
                return FileMetaData.LAST_MODIFIED;
            case 4:
                return FileMetaData.LAST_ACCESSED;
                default:
                    return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            default:
                return Date.class;
        }
    }
    
    public void addRow(FileMetaData row)
    {
        int index = data.size();
        data.add(row);
        fireTableRowsInserted(index, index);
    }
    
    public void clear()
    {
        int index1 = data.size()-1;
        data.clear();
        if (index1 >= 0)
            fireTableRowsDeleted(0, index1);
    }
}
