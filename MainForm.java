package edu.fortLee.finalProject.swing;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.SwingConstants;

public class MainForm extends JFrame {

	private JPanel contentPane;
	private JButton btnSelectFiles;
	DefaultListModel<Object> listboxModel = new DefaultListModel<Object>();
	ArrayList<String> javaFiles = new ArrayList<String>();
	DefaultTableModel tableModel = new DefaultTableModel(); 
	private JTable table;
	private JButton btnGrade;
	private JTextField txtGrade;
	private JButton btnExit;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					MainForm frame = new MainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void infoBox(String infoMessage, String titleBar) {
	    JOptionPane.showMessageDialog(null, infoMessage,  titleBar, JOptionPane.INFORMATION_MESSAGE);
	  }
	
	private void pickFiles()
	{
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select one or more java files:");
		jfc.setMultiSelectionEnabled(true);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("java files", "java");
		jfc.addChoosableFileFilter(filter);
			
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File[] files = jfc.getSelectedFiles();

			Arrays.asList(files).forEach(x -> {
			   	if (x.isFile()) {
					// System.out.println(x.getPath());
					if (listboxModel.contains(x.getName()) == false)
					{
						listboxModel.addElement(x.getName().toString());
						javaFiles.add(x.getPath());						
					}				
				}
			});
		}
	}
	
	private void addRow()
	{ 
		
		String keyword= JOptionPane.showInputDialog("Please enter a new keyword ");
		if (keyword != null) { //user did not click cancel
			if(!keyword.trim().contentEquals(""))
				tableModel.addRow(new Object[]{keyword, new Integer(0)});
		}
	}
	
	private void findGrade()
	{
		
		if(validateInput())
		{ 
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				tableModel.setValueAt(0, i, 2); //reset occurrence found in case of re run
			}
			Grader grader = new Grader(); 
			grader.calculateGrade(javaFiles,tableModel ); 
			txtGrade.setText(String.format( "%.2f", grader.getGrade()));		
		}
		
	}
	
	private boolean validateInput() 
	{ 
		if (listboxModel.getSize() == 0)
		{
			infoBox("Please select one or more java files", "Grader Application");
			return false;
		}
		
		for (int i = 0; i < tableModel.getRowCount(); i++) {

			if (!isValidOccurence(tableModel.getValueAt(i, 1).toString()))
			{			
				infoBox("Occurences required must be a number >= 0", "Grader Application");
				return false;						
			}
		}
		
		return true; 
	}
	
	public static boolean isValidOccurence(String strNum) {
		Integer d = 0;
	    try {
	        d = Integer.parseInt(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    
	    if (d < 0)
	    	return false;
	    
	    return true;
	}
	
	/**
	 * Create the frame.
	 */
	public MainForm() {
		setTitle("JAVA Grader Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 753, 487);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JList listFiles = new JList(listboxModel);
		listFiles.setBorder(new LineBorder(new Color(0, 0, 0)));
		listFiles.setBounds(39, 115, 194, 207);
		contentPane.add(listFiles);
		
		btnSelectFiles = new JButton("Select Java File(s)");
		btnSelectFiles.setBounds(39, 77, 190, 25);
		btnSelectFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pickFiles();		
			}
		});
		contentPane.add(btnSelectFiles);
		
		tableModel = new DefaultTableModel() {

		    @Override
		    public boolean isCellEditable(int row, int column) {
		       if (column == 0 || column == 2)
		    	   return false;  //user can not edit first and third column
		       else
		    	   return true;
		    }
		};
		
		tableModel.addColumn("Keywords"); 
		tableModel.addColumn("Occurences Required"); 
		tableModel.addColumn("Occurences Found"); 

		// Append  rows
		tableModel.addRow(new Object[]{"class", new Integer(0), new Integer(0)});
		tableModel.addRow(new Object[]{"if", new Integer(0), new Integer(0)});
		tableModel.addRow(new Object[]{"for", new Integer(0), new Integer(0)});
		tableModel.addRow(new Object[]{"while", new Integer(0), new Integer(0)});
		tableModel.addRow(new Object[]{"extends", new Integer(0), new Integer(0)});
		tableModel.addRow(new Object[]{"implements", new Integer(0), new Integer(0)});

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(281, 109, 418, 213);
		contentPane.add(scrollPane);
		
		table = new JTable(tableModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table );
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setRowHeight(25);
		table.setDefaultRenderer(Object.class, new MyRenderer());
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		JButton btnAddKeyword = new JButton("Add keyword");
		btnAddKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addRow();
			}
		});
		btnAddKeyword.setBounds(536, 77, 158, 25);
		contentPane.add(btnAddKeyword);
		
		btnGrade = new JButton("Grade");
		btnGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				findGrade();
			}
		});
		btnGrade.setBounds(292, 396, 97, 25);
		contentPane.add(btnGrade);
		
		txtGrade = new JTextField();
		txtGrade.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGrade.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtGrade.setEditable(false);
		txtGrade.setBounds(401, 397, 97, 22);
		contentPane.add(txtGrade);
		txtGrade.setColumns(10);
		
		btnExit = new JButton("Exit ");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(602, 396, 97, 25);
		contentPane.add(btnExit);
	
	}
}
