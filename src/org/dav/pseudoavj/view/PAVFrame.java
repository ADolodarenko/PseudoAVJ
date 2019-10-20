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
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PAVFrame extends JFrame implements ResultView<Object, List<Object>, IntPair>
{
	private static final int MIN_WIDTH = 400;
	private static final int MIN_HEIGHT = 300;
	private static final int ROWS = 20;
	
	private boolean mustRestart;
	
	private Properties properties;
	
	private WindowAttrs windowAttributes;
	
	private JFileChooser fileChooser;
	private Desktop desktop;
	
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
	
	private JPopupMenu popupMenu;
	
	private FileMetaDataTableModel tableModel;
	private JTable filesTable;
	
	private JLabel statusLine;
	
	private TitleAdjuster titleAdjuster;
	
	public PAVFrame()
	{
		initComponents();
	}
	
	private void initComponents()
	{
		loadProperties();
		
		initAttributes();
		initFileChooser();
		initDesktop();

		setIconImage(ResourceManager.getInstance().getImageIcon("folder_green_32.png").getImage());

		add(initCommandPanel(), BorderLayout.NORTH);
		add(initStatusBar(), BorderLayout.SOUTH);
		add(initTablePanel());
		
		initMenus();

		titleAdjuster.registerComponent(this, new Title("Main_Window_Title"));

		titleAdjuster.resetComponents();

		pack();
		
		initFrame();
	}
	
	private void loadProperties()
	{
		properties = new Properties();
		
		try (FileInputStream stream = new FileInputStream(ResourceManager.getInstance().getConfig()))
		{
			properties.load(stream);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, Title.getTitleString("Fail_Load_Properties"),
										  Title.getTitleString("Warning"), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void saveProperties()
	{
		try (FileWriter writer = new FileWriter(ResourceManager.getInstance().getConfig()))
		{
			properties.store(writer, null);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, Title.getTitleString("Fail_Save_Properties"),
										  Title.getTitleString("Warning"), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void initFrame()
	{
		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				Dimension currentDim = PAVFrame.this.getSize();
				Dimension minimumDim = PAVFrame.this.getMinimumSize();
				
				if (currentDim.width < minimumDim.width)
					currentDim.width = minimumDim.width;
				if (currentDim.height < minimumDim.height)
					currentDim.height = minimumDim.height;
				
				PAVFrame.this.setSize(currentDim);
			}
		});
		
		if (windowAttributes != null)
		{
			if (windowAttributes.isMaximized())
				setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
			else
				setBounds(windowAttributes.getLeftTopCorner().x,
					  windowAttributes.getLeftTopCorner().y,
					  windowAttributes.getMeasurements().width,
					  windowAttributes.getMeasurements().height);
		}
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				cancelSearch();
				setWindowAttributes();
				saveProperties();
				
				if (mustRestart)
					restart();
			}
		});
	}
	
	private void restart()
	{
		String javaCommand = System.getProperty("java.home") +
				File.separator + "bin" + File.separator + "java";
		File currentJar = null;
		
		try
		{
			currentJar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch (URISyntaxException e){}
		
		if (currentJar != null && currentJar.getName().endsWith(".jar"))
		{
			List<String> command = new ArrayList<>();
			command.add(javaCommand);
			command.add("-jar");
			command.add(currentJar.getPath());
			
			ProcessBuilder builder = new ProcessBuilder(command);
			try
			{
				builder.start();
			}
			catch (IOException e){}
		}
	}
	
	private void cancelSearch()
	{
		if (searcher != null && !searcher.isDone() && !searcher.isCancelled())
			searcher.cancel(false);
	}
	
	private void setWindowAttributes()
	{
		if (windowAttributes != null)
		{
			windowAttributes.setLocale(ResourceManager.getInstance().getCurrentLocale());
			windowAttributes.setMaximized(getExtendedState() == JFrame.MAXIMIZED_BOTH);
			windowAttributes.setLeftTopCorner(getBounds().getLocation());
			windowAttributes.setMeasurements(getSize());
			
			windowAttributes.save(attrsKeeper);
		}
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
	
	private void initDesktop()
	{
		if (Desktop.isDesktopSupported())
		{
			desktop = Desktop.getDesktop();
			
			if (!desktop.isSupported(Desktop.Action.BROWSE))
				desktop = null;
		}
	}
	
	private void initAttributes()
	{
		attrsKeeper = new PropertiesAttrsKeeper(properties);
		
		searchAttributes = new FileAttrs();
		searchAttributes.load(attrsKeeper);
		
		windowAttributes = new WindowAttrs();
		windowAttributes.load(attrsKeeper);
		
		ResourceManager.getInstance().setCurrentLocale(windowAttributes.getLocale());
		
		titleAdjuster = new TitleAdjuster();
		
		mustRestart = false;
	}
	
	private JPanel initStatusBar()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		statusLine = new JLabel();
		titleAdjuster.registerComponent(statusLine, new Title("Status_Ready"));
		panel.add(statusLine, BorderLayout.WEST);
	
		return panel;
	}
	
	private JPanel initTablePanel()
	{
		tableModel = new FileMetaDataTableModel();
		filesTable = new JTable(tableModel);
		addListeners(filesTable);
		
		setTableCellsFormat();
		
		JScrollPane tablePane = new JScrollPane(filesTable);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"",
                TitledBorder.TOP, TitledBorder.CENTER));
		titleAdjuster.registerComponent(tablePanel, new Title("Result_Panel_Title"));
		tablePanel.add(tablePane, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private void setTableCellsFormat()
	{
		TableColumnModel columnModel = filesTable.getColumnModel();
		TableCellRenderer renderer = new FormatRenderer(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
		
		for (int i = 2; i < columnModel.getColumnCount(); i++)
			columnModel.getColumn(i).setCellRenderer(renderer);
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
		
		JLabel label = new JLabel();
		titleAdjuster.registerComponent(label, new Title("Path_Label_Title"));
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
		
		searchButton = new JButton();
		titleAdjuster.registerComponent(searchButton, new Title("Search_Button_Title"));
		searchButton.setIcon(ResourceManager.getInstance().getImageIcon("search01.png"));
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (searchButton.getText().equals(Title.getTitleString("Search_Button_Title")))
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
		
		paramsButton = new JButton();
		titleAdjuster.registerComponent(paramsButton, new Title("Params_Button_Title"));
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
				
				//mustRestart = true;
				//PAVFrame.this.dispatchEvent(new WindowEvent(PAVFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(localeButton);
		
		return panel;
	}

	private void repaintGUI()
	{
		titleAdjuster.resetComponents();
		
		tableModel.fireTableStructureChanged();
		setTableCellsFormat();
		
		localeButton.setIcon(getLocaleButtonImage());
		
		validate();
	}

	private Icon getLocaleButtonImage()
	{
		Icon result = null;

		if (ResourceManager.getInstance().getCurrentLocale() == ResourceManager.ENG_LOCALE)
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
				
				titleAdjuster.changeComponentTitle(statusLine, new Title(item.getCurrentDirectory().getAbsolutePath()));

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
				
				titleAdjuster.changeComponentTitle(statusLine, new Title(item.getCurrentDirectory().getAbsolutePath()));
				
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
	public void showResult(List<Object> data, IntPair pair)
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
		
		titleAdjuster.changeComponentTitle(statusLine, new Title("Result_Statistics",
																 pair.getFirstInt(), pair.getSecondInt()));
	}

	@Override
	public void activateControls()
	{
		titleAdjuster.changeComponentTitle(searchButton, new Title("Search_Button_Title"));
		searchButton.setIcon(ResourceManager.getInstance().getImageIcon("search01.png"));

		setControlsEnabled(true);
	}

	@Override
	public void blockControls()
	{
		titleAdjuster.changeComponentTitle(searchButton, new Title("Stop_Button_Title"));
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
			titleAdjuster.changeComponentTitle(statusLine, new Title("Status_Ready"));
	}
	
	private void locateSelectedFiles()
	{
		if (filesTable != null && tableModel != null && filesTable.getSelectedRowCount() > 0)
		{
			for (int i : filesTable.getSelectedRows())
			{
				int rowIndex = filesTable.convertRowIndexToModel(i);
				FileMetaData row = tableModel.getRow(rowIndex);
				
				if (row != null)
				{
					URI dir = Paths.get(row.getName()).getParent().toUri();
					
					try
					{
						desktop.browse(dir);
					}
					catch (IOException e)
					{}
				}
			}
		}
	}
	
	private void deleteSelectedFiles()
	{
		if (filesTable != null && tableModel != null && filesTable.getSelectedRowCount() > 0)
		{
			List<FileMetaData> deadRows = new ArrayList<>();
			
			for (int i : filesTable.getSelectedRows())
			{
				int rowIndex = filesTable.convertRowIndexToModel(i);
				FileMetaData row = tableModel.getRow(rowIndex);
				
				if (row != null)
				{
					File file = new File(row.getName());
					
					if (file.delete())
						deadRows.add(row);
				}
			}
			
			for (FileMetaData row : deadRows)
				tableModel.remove(row);
		}
	}

    class FilesTableMenu extends JPopupMenu
	{
		JMenuItem locateFileMenu;
		JMenuItem deleteFileMenu;
		
		FilesTableMenu()
		{
			locateFileMenu = new JMenuItem();
			titleAdjuster.registerComponent(locateFileMenu, new Title("Locate_Menu_Title"));
			locateFileMenu.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					locateSelectedFiles();
				}
			});
			add(locateFileMenu);

			deleteFileMenu = new JMenuItem();
			titleAdjuster.registerComponent(deleteFileMenu, new Title("Delete_Menu_Title"));
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
