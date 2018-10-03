package org.dav.pseudoavj.view;

import org.dav.pseudoavj.model.FileAttrs;
import org.dav.pseudoavj.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

public class AttrDialog extends JDialog implements TitleGetter
{
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;

    private int result;
    private FileAttrs attributes;
	
	private JCheckBox maskCheck;
	private JTextField maskTextField;
	private JCheckBox visibilityCheck;
	private JComboBox<FileAttrs.FileVisibility> visibilityCombo;
	private JCheckBox fileTimeCheck;
	private JSpinner dateTimeSpinnerFrom;
	private JSpinner dateTimeSpinnerTo;

	private JButton saveButton;
	private JButton cancelButton;
	
	
	public AttrDialog(JFrame owner)
	{
		super(owner, "", true);

		initComponents();

		addWindowListeners();
	}

	private void addWindowListeners()
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				buildAttrs();
			}

			@Override
			public void windowActivated(WindowEvent e)
			{
				buildInterfaceValues();

				if (maskTextField != null)
					maskTextField.requestFocusInWindow();

				result = CANCEL_OPTION;
			}
		});
	}

    public int getResult()
    {
        return result;
    }

    private void initComponents()
	{
		setTitle(getComponentTitle("Attrs_Dialog_Title"));

		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		GridBagLayout dataLayout = new GridBagLayout();
		dataPanel.setLayout(dataLayout);

		GridBagConstraints constraints = new GridBagConstraints();

		maskCheck = new JCheckBox(getComponentTitle("Attrs_Check_Mask_Title"));
		maskCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (maskTextField != null)
					maskTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;

		dataPanel.add(maskCheck, constraints);

		maskTextField = new JTextField(20);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.8;

		dataPanel.add(maskTextField, constraints);

		maskCheck.setSelected(true);

		//Visibility
		visibilityCheck = new JCheckBox(getComponentTitle("Attrs_Check_Visibility_Title"));
		visibilityCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (visibilityCombo != null)
					visibilityCombo.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;
		dataPanel.add(visibilityCheck, constraints);

		visibilityCombo = new JComboBox<>(FileAttrs.FileVisibility.values());
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.8;
		dataPanel.add(visibilityCombo, constraints);

		visibilityCheck.setSelected(false);
		visibilityCombo.setEnabled(false);

		//File time
		fileTimeCheck = new JCheckBox(getComponentTitle("Attrs_Check_DateTime_Title"));
		fileTimeCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (dateTimeSpinnerFrom != null)
					dateTimeSpinnerFrom.setEnabled(e.getStateChange() == ItemEvent.SELECTED);

				if (dateTimeSpinnerTo != null)
					dateTimeSpinnerTo.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;

		dataPanel.add(fileTimeCheck, constraints);

		Date date = new Date();

		SpinnerDateModel modelFrom = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
		dateTimeSpinnerFrom = new JSpinner(modelFrom);
		JSpinner.DateEditor editorFrom = new JSpinner.DateEditor(dateTimeSpinnerFrom, "dd-MM-yyyy HH:mm:ss");
		dateTimeSpinnerFrom.setEditor(editorFrom);

		SpinnerDateModel modelTo = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
		dateTimeSpinnerTo = new JSpinner(modelTo);
		JSpinner.DateEditor editorTo = new JSpinner.DateEditor(dateTimeSpinnerTo, "dd-MM-yyyy HH:mm:ss");
		dateTimeSpinnerTo.setEditor(editorTo);

		JPanel timePanel = new JPanel(new GridLayout(1, 2));

		JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fromPanel.add(new JLabel(getComponentTitle("Attrs_Check_DateTime_From")));
		fromPanel.add(dateTimeSpinnerFrom);

		timePanel.add(fromPanel);

		JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toPanel.add(new JLabel(getComponentTitle("Attrs_Check_DateTime_To")));
		toPanel.add(dateTimeSpinnerTo);

		timePanel.add(toPanel);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.8;
		dataPanel.add(timePanel, constraints);

		fileTimeCheck.setSelected(true);

		add(dataPanel, BorderLayout.CENTER);

		//
		JPanel commandPanel = new JPanel();

		saveButton = new JButton(getComponentTitle("Attrs_Save_Button_Title"));
		saveButton.setIcon(ResourceManager.getInstance().getImageIcon("ok_16.png"));
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				result = OK_OPTION;

				AttrDialog.this.dispatchEvent(new WindowEvent(AttrDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		commandPanel.add(saveButton);

		cancelButton = new JButton(getComponentTitle("Attrs_Cancel_Button_Title"));
		cancelButton.setIcon(ResourceManager.getInstance().getImageIcon("cancel_16.png"));
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AttrDialog.this.dispatchEvent(new WindowEvent(AttrDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		commandPanel.add(cancelButton);

		add(commandPanel, BorderLayout.SOUTH);

		pack();
		setResizable(false);
	}

	private void buildAttrs()
	{
		String nameMask = maskTextField.isEnabled() ? maskTextField.getText() : null;
		FileAttrs.FileVisibility visibility = visibilityCombo.isEnabled() ?
				visibilityCombo.getItemAt(visibilityCombo.getSelectedIndex()) : null;
		
		Long from = null, to = null;
		
		if (dateTimeSpinnerFrom.isEnabled())
		{
			from = ((SpinnerDateModel) dateTimeSpinnerFrom.getModel()).getDate().getTime();
			to = ((SpinnerDateModel) dateTimeSpinnerTo.getModel()).getDate().getTime();
		}

		attributes = new FileAttrs(nameMask, visibility, from, to);
	}

	private void buildInterfaceValues()
	{
		if (attributes != null)
		{
			String nameMask = attributes.getNameMask();
			maskCheck.setSelected(nameMask != null);
			maskTextField.setText(nameMask != null ? nameMask : "");

			FileAttrs.FileVisibility visibility = attributes.getVisibility();
			visibilityCheck.setSelected(visibility != null);
			if (visibility != null)
				visibilityCombo.setSelectedItem(visibility);
			
			FileAttrs.FileTimePeriod period = attributes.getFileTimePeriod();
			fileTimeCheck.setSelected(period != null);
			if (period != null)
			{
				dateTimeSpinnerFrom.getModel().setValue(new Date(period.getFrom().toMillis()));
				dateTimeSpinnerTo.getModel().setValue(new Date(period.getTo().toMillis()));
			}
		}
	}
	
	public FileAttrs getAttrs()
	{
	    return attributes;
	}
	
	public void setAttrs(FileAttrs attrs)
	{
		attributes = attrs;
	}

	@Override
	public String getComponentTitle(String key)
	{
		return ResourceManager.getInstance().getBundle().getString(key);
	}
}
