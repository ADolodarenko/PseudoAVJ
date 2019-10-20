package org.dav.pseudoavj.view;

import org.dav.pseudoavj.model.FileAttrs;
import org.dav.pseudoavj.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

public class AttrDialog extends JDialog
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
	
	private TitleAdjuster titleAdjuster;
	
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
				titleAdjuster.resetComponents();
				validate();
				
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
		titleAdjuster = new TitleAdjuster();
		
		titleAdjuster.registerComponent(this, new Title("Attrs_Dialog_Title"));

		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		GridBagLayout dataLayout = new GridBagLayout();
		dataPanel.setLayout(dataLayout);

		maskCheck = new JCheckBox();
		titleAdjuster.registerComponent(maskCheck, new Title("Attrs_Check_Mask_Title"));
		maskCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (maskTextField != null)
					maskTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		dataPanel.add(maskCheck, new UsableGBC(0, 0, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.2));

		maskTextField = new JTextField(20);
		
		dataPanel.add(maskTextField, new UsableGBC(1, 0, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.8));

		maskCheck.setSelected(true);

		//Visibility
		visibilityCheck = new JCheckBox();
		titleAdjuster.registerComponent(visibilityCheck, new Title("Attrs_Check_Visibility_Title"));
		visibilityCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (visibilityCombo != null)
					visibilityCombo.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		dataPanel.add(visibilityCheck, new UsableGBC(0, 1, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.2));

		visibilityCombo = new JComboBox<>(FileAttrs.FileVisibility.values());
		
		dataPanel.add(visibilityCombo, new UsableGBC(1, 1, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.8));

		visibilityCheck.setSelected(false);
		visibilityCombo.setEnabled(false);

		//File time
		fileTimeCheck = new JCheckBox();
		titleAdjuster.registerComponent(fileTimeCheck, new Title("Attrs_Check_DateTime_Title"));
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
		
		dataPanel.add(fileTimeCheck, new UsableGBC(0, 2, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.2));

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
		JLabel label = new JLabel();
		titleAdjuster.registerComponent(label, new Title("Attrs_Check_DateTime_From"));
		fromPanel.add(label);
		fromPanel.add(dateTimeSpinnerFrom);

		timePanel.add(fromPanel);

		JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel();
		titleAdjuster.registerComponent(label, new Title("Attrs_Check_DateTime_To"));
		toPanel.add(label);
		toPanel.add(dateTimeSpinnerTo);

		timePanel.add(toPanel);

		dataPanel.add(timePanel, new UsableGBC(1, 2, 1, 1).
				setFill(GridBagConstraints.HORIZONTAL).setWeightX(0.8));

		fileTimeCheck.setSelected(true);

		add(dataPanel, BorderLayout.CENTER);

		//
		JPanel commandPanel = new JPanel();

		saveButton = new JButton();
		titleAdjuster.registerComponent(saveButton, new Title("Attrs_Save_Button_Title"));
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

		cancelButton = new JButton();
		titleAdjuster.registerComponent(cancelButton, new Title("Attrs_Cancel_Button_Title"));
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
		
		titleAdjuster.resetComponents();
		
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
}
