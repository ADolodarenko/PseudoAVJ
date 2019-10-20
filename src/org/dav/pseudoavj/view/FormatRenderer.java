package org.dav.pseudoavj.view;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;
import java.text.Format;

public class FormatRenderer extends DefaultTableCellRenderer
{
	public static FormatRenderer getDateTimeRenderer()
	{
		return new FormatRenderer(DateFormat.getDateTimeInstance());
	}
	
	public static FormatRenderer getTimeRenderer()
	{
		return new FormatRenderer(DateFormat.getTimeInstance());
	}
	
	private Format formatter;
	
	public FormatRenderer(Format formatter)
	{
		this.formatter = formatter;
	}
	
	public void setValue(Object value)
	{
		//  Format the Object before setting its value in the renderer
		
		try
		{
			if (value != null)
				value = formatter.format(value);
		}
		catch(IllegalArgumentException e) {}
		
		super.setValue(value);
	}
}
