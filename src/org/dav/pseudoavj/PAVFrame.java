package org.dav.pseudoavj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PAVFrame extends JFrame
{
	private static final int ROWS = 20;
	
	private JFileChooser fileChooser;
	
	private SwingWorker<java.util.List<File>, File> searcher;
	
	private JTextField pathTextField;
	private JButton pathButton;
	private JButton searchButton;
	private JButton paramsButton;
	private JList<String> filesList;
	private JLabel statusLine;
	
	public PAVFrame()
	{
		setTitle("Pseudo AV in Java");
		
		initFileChooser();
		
		add(initCommandPanel(), BorderLayout.NORTH);
		add(initStatusBar(), BorderLayout.SOUTH);
		add(initListPane());
		
		pack();
	}
	
	private void initFileChooser()
	{
		fileChooser = new JFileChooser(".");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
	}
	
	private JPanel initStatusBar()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		statusLine = new JLabel("Ready.");
		panel.add(statusLine, BorderLayout.WEST);
	
		return panel;
	}
	
	private JScrollPane initListPane()
	{
		filesList = new JList<>();
		filesList.setVisibleRowCount(ROWS);
		
		return new JScrollPane(filesList);
	}
	
	private JPanel initCommandPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		//Path text field panel
		panel.add(initPathPanel(), BorderLayout.CENTER);
		
		//Panel for control
		panel.add(initButtonPanel(), BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel initPathPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		JLabel label = new JLabel("Path: ");
		panel.add(label, BorderLayout.WEST);
		
		pathTextField = new JTextField(new File(".").getAbsolutePath());
		panel.add(pathTextField, BorderLayout.CENTER);
		
		label.setLabelFor(pathTextField);
		
		pathButton = new JButton("...");
		pathButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					pathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
		panel.add(pathButton, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel initButtonPanel()
	{
		JPanel panel = new JPanel();
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				searcher = new FileSearcher(new File(pathTextField.getText()), statusLine);
				searcher.execute();
			}
		});
		panel.add(searchButton);
		
		paramsButton = new JButton("Parameters...");
		paramsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				;
			}
		});
		panel.add(paramsButton);
		
		return panel;
	}
}
