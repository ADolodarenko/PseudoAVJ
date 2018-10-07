package org.dav.pseudoavj.model;

import org.dav.pseudoavj.view.Title;

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
                return Title.getTitleString(FileMetaData.FILE_NAME_STRING);
            case 1:
                return Title.getTitleString(FileMetaData.HIDDEN_STRING);
            case 2:
                return Title.getTitleString(FileMetaData.CREATED_STRING);
            case 3:
                return Title.getTitleString(FileMetaData.LAST_MODIFIED_STRING);
            case 4:
                return Title.getTitleString(FileMetaData.LAST_ACCESSED_STRING);
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
    
    public FileMetaData getRow(int rowIndex)
    {
        return data.get(rowIndex);
    }

    public void remove(FileMetaData row)
    {
        int index = data.indexOf(row);
        if (index >= 0)
        {
            data.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void clear()
    {
        int index1 = data.size()-1;
        data.clear();
        if (index1 >= 0)
            fireTableRowsDeleted(0, index1);
    }
}
