package org.dav.pseudoavj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

public class AttrsFrame extends JFrame
{
	private JTextField maskTextField;
	private JComboBox<FileAttrs.FileVisibility> visibilityCombo;
	private JSpinner dateTimeSpinner;
	
	private JButton saveButton;
	private JButton cancelButton;
	
	
	public AttrsFrame()
	{
		initComponents();
	}
	
	private void initComponents()
	{
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		GridLayout dataLayout = new GridLayout(3, 2);
		dataPanel.setLayout(dataLayout);
		
		//Mask
		JCheckBox maskCheck = new JCheckBox("File name pattern: ");
		maskCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (maskTextField != null)
					maskTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		dataPanel.add(maskCheck);
		
		maskTextField = new JTextField();
		dataPanel.add(maskTextField);
		
		maskCheck.setSelected(true);
		
		//Visibility
		JCheckBox visibilityCheck = new JCheckBox("Visibility: ");
		visibilityCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (visibilityCombo != null)
					visibilityCombo.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		dataPanel.add(visibilityCheck);
		
		visibilityCombo = new JComboBox<>(FileAttrs.FileVisibility.values());
		dataPanel.add(visibilityCombo);
		
		visibilityCheck.setSelected(false);
		visibilityCombo.setEnabled(false);
		
		//File time
		JCheckBox fileTimeCheck = new JCheckBox("Creation time: ");
		fileTimeCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (dateTimeSpinner != null)
					dateTimeSpinner.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		dataPanel.add(fileTimeCheck);
		
		Date date = new Date();
		SpinnerDateModel model = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
		dateTimeSpinner = new JSpinner(model);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinner, "HH:mm:ss");
		dateTimeSpinner.setEditor(editor);
		dataPanel.add(dateTimeSpinner);
		
		fileTimeCheck.setSelected(true);
		
		add(dataPanel, BorderLayout.CENTER);
		
		//
		JPanel commandPanel = new JPanel();
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//
			}
		});
		commandPanel.add(saveButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AttrsFrame.this.dispatchEvent(new WindowEvent(AttrsFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		commandPanel.add(cancelButton);
		
		add(commandPanel, BorderLayout.SOUTH);
		
		pack();
	}
	
	public FileAttrs getAttrs()
	{
		return null;
	}
	
	public void setAttrs(FileAttrs attrs)
	{
	
	}
}
