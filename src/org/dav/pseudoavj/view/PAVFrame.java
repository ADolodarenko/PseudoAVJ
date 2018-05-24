package org.dav.pseudoavj.view;

import org.dav.pseudoavj.logic.FileSearcherAdvanced;
import org.dav.pseudoavj.model.*;
import org.dav.pseudoavj.util.AttrsKeeper;
import org.dav.pseudoavj.util.PropertiesAttrsKeeper;
import org.dav.pseudoavj.ResourceManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PAVFrame extends JFrame implements ResultView<Object, List<Object>, String>, AdjustableTitles
{
	private static final int ROWS = 20;
	
	private JFileChooser fileChooser;
	
	private SwingWorker<java.util.List<Object>, Object> searcher;

	private AttrDialog attrDialog;
	private FileAttrs searchAttributes;
	private AttrsKeeper attrsKeeper;
	
	private JTextField pathTextField;
	private JButton pathButton;
	private JButton searchButton;
	private JButton paramsButton;
	private JButton localeButton;
	private DefaultListModel<String> listModel;
	private JList<String> filesList;
	
	private JPopupMenu popupMenu;
	
	private FileMetaDataTableModel tableModel;
	private JTable filesTable;
	
	private JLabel statusLine;
	
	public PAVFrame()
	{
		initComponents();

		setTitle(getComponentTitle("Main_Window_Title"));
	}
	
	private void initComponents()
	{
		initAttributes();
		initFileChooser();

		setIconImage(ResourceManager.getInstance().getImageIcon("folder_green_32.png").getImage());

		add(initCommandPanel(), BorderLayout.NORTH);
		add(initStatusBar(), BorderLayout.SOUTH);
		add(initTablePanel());
		
		//add(initListPane());
		
		initMenus();
		
		pack();
	}
	
	private void initMenus()
	{
		popupMenu = new FilesTableMenu();
	}
	
	private void initFileChooser()
	{
		fileChooser = new JFileChooser(".");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
	}
	
	private void initAttributes()
	{
		attrsKeeper = new PropertiesAttrsKeeper(ResourceManager.getInstance().getConfig());
		searchAttributes = new FileAttrs();
		
		searchAttributes.load(attrsKeeper);
	}
	
	private JPanel initStatusBar()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		statusLine = new JLabel(getComponentTitle("Status_Ready"));
		panel.add(statusLine, BorderLayout.WEST);
	
		return panel;
	}
	
	private JScrollPane initListPane()
	{
		listModel = new DefaultListModel<>();
		
		filesList = new JList<>(listModel);
		filesList.setVisibleRowCount(ROWS);
		
		return new JScrollPane(filesList);
	}
	
	private JPanel initTablePanel()
	{
		tableModel = new FileMetaDataTableModel();
		filesTable = new JTable(tableModel);
		addListeners(filesTable);
		
		TableColumnModel columnModel = filesTable.getColumnModel();
		TableCellRenderer renderer = new FormatRenderer(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
		
		for (int i = 2; i < columnModel.getColumnCount(); i++)
			columnModel.getColumn(i).setCellRenderer(renderer);
		
		JScrollPane tablePane = new JScrollPane(filesTable);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				getComponentTitle("Result_Panel_Title"),
                TitledBorder.TOP, TitledBorder.CENTER));
		tablePanel.add(tablePane, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private void addListeners(JTable table)
	{
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					Object source = e.getSource();
					if (source instanceof JTable)
					{
						JTable sourceTable = (JTable)source;
						if (sourceTable.getSelectedRowCount() > 0)
							doPopup(e);
					}
				}
			}
		});
		
	}
	
	private void doPopup(MouseEvent e)
	{
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
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
		
		JLabel label = new JLabel(getComponentTitle("Path_Label_Title"));
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
		
		searchButton = new JButton(getComponentTitle("Search_Button_Title"));
		searchButton.setIcon(ResourceManager.getInstance().getImageIcon("search01.png"));
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (searchButton.getText().equals(getComponentTitle("Search_Button_Title")))
				{
					clearResults();

					searcher = new FileSearcherAdvanced(new File(pathTextField.getText()), searchAttributes, PAVFrame.this);
					searcher.execute();

					blockControls();
				}
				else
				{
					if (searcher != null)
						searcher.cancel(true);
					else
						activateControls();
				}
			}
		});
		panel.add(searchButton);
		
		paramsButton = new JButton(getComponentTitle("Params_Button_Title"));
		paramsButton.setIcon(ResourceManager.getInstance().getImageIcon("params_16.png"));
		paramsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (attrDialog == null)
				{
					attrDialog = new AttrDialog(PAVFrame.this);
					attrDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}

				attrDialog.setAttrs(searchAttributes);
				attrDialog.setLocationRelativeTo(PAVFrame.this);
				attrDialog.setVisible(true);

				if (attrDialog.getResult() == AttrDialog.OK_OPTION)
				{
					searchAttributes = attrDialog.getAttrs();
					searchAttributes.save(attrsKeeper);
				}
			}
		});
		panel.add(paramsButton);

		localeButton = new JButton();
		localeButton.setIcon(getLocaleButtonImage());
		localeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResourceManager.getInstance().switchCurrentLocale();
				repaintGUI();
			}
		});
		panel.add(localeButton);
		
		return panel;
	}

	private void repaintGUI()
	{
		localeButton.setIcon(getLocaleButtonImage());
	}

	private Icon getLocaleButtonImage()
	{
		Icon result = null;

		if (ResourceManager.getInstance().getCurrentLocale() == Locale.US)
		    result = ResourceManager.getInstance().getImageIcon("american_16.png");
		else
		    result = ResourceManager.getInstance().getImageIcon("russian_16.png");

		return result;
	}

	@Override
	public void updateData(List<Object> chunks)
	{
		if (chunks != null && !chunks.isEmpty())
		{
			Object object = chunks.get(chunks.size() - 1);

			if (object instanceof ProgressData)
			{
				ProgressData item = (ProgressData) object;

				statusLine.setText(item.getCurrentDirectory().getAbsolutePath());

				for (Object chunk : chunks)
				{
					ProgressData element = (ProgressData) chunk;

					for (File file : element.getFiles())
						listModel.addElement(file.getAbsolutePath());
				}
			}
			else if (object instanceof ProgressDataAdvanced)
			{
				ProgressDataAdvanced item = (ProgressDataAdvanced) object;

				statusLine.setText(item.getCurrentDirectory().getAbsolutePath());
				
				for (Object chunk : chunks)
				{
					ProgressDataAdvanced element = (ProgressDataAdvanced) chunk;
					
					for (FileMetaData file : element.getFiles())
						tableModel.addRow(file);
				}
			}
		}
	}
	
	@Override
	public void showResult(List<Object> data, String message)
	{
		if (data != null && !data.isEmpty())
		{
			Object object = data.get(0);

			if (object instanceof File)
			{
				listModel.clear();

				for (Object row : data)
				{
					File element = (File) row;

					listModel.addElement(element.getAbsolutePath());
				}
			}
			else if (object instanceof FileMetaData)
			{
				tableModel.clear();
				
				for (Object row : data)
				{
					FileMetaData element = (FileMetaData) row;
					
					tableModel.addRow(element);
				}
			}
		}
		
		statusLine.setText(message);
	}

	@Override
	public void activateControls()
	{
		searchButton.setText(getComponentTitle("Search_Button_Title"));
		searchButton.setIcon(ResourceManager.getInstance().getImageIcon("search01.png"));

		setControlsEnabled(true);
	}

	@Override
	public void blockControls()
	{
		searchButton.setText(getComponentTitle("Stop_Button_Title"));
		searchButton.setIcon(ResourceManager.getInstance().getImageIcon("cancel_16.png"));

		setControlsEnabled(false);
	}

	private void setControlsEnabled(boolean enabled)
	{
		pathTextField.setEnabled(enabled);
		pathButton.setEnabled(enabled);
		paramsButton.setEnabled(enabled);
	}

	private void clearResults()
	{
		if (listModel != null)
			listModel.clear();
		
		if (tableModel != null)
			tableModel.clear();

		if (statusLine != null)
			statusLine.setText(getComponentTitle("Status_Ready"));
	}
	
	private void locateSelectedFiles()
	{
		if (filesTable != null && tableModel != null && filesTable.getSelectedRowCount() > 0)
		{
			StringBuilder builder = new StringBuilder();
			
			for (int i : filesTable.getSelectedRows())
			{
				builder.append(tableModel.getValueAt(i, 0).toString());
				builder.append('\n');
			}
			
			JOptionPane.showMessageDialog(this, builder.toString(), "Selected files",
										  JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void deleteSelectedFiles()
	{
		;
	
	}

    @Override
    public String getComponentTitle(String key)
    {
        return ResourceManager.getInstance().getBundle().getString(key);
    }

    class FilesTableMenu extends JPopupMenu
	{
		JMenuItem locateFileMenu;
		JMenuItem deleteFileMenu;
		
		FilesTableMenu()
		{
			locateFileMenu = new JMenuItem(getComponentTitle("Locate_Menu_Title"));
			locateFileMenu.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					locateSelectedFiles();
				}
			});
			add(locateFileMenu);
			
			deleteFileMenu = new JMenuItem(getComponentTitle("Delete_Menu_Title"));
			deleteFileMenu.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					deleteSelectedFiles();
				}
			});
			add(deleteFileMenu);
		}
	}
}
