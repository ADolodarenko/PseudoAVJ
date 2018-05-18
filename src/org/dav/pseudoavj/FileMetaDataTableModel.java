package org.dav.pseudoavj;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FileMetaDataTableModel extends AbstractTableModel
{
    private List<FileMetaData> data;

    public FileMetaDataTableModel(List<FileMetaData> data)
    {
        this.data = data;
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
}
