package at.freakey.mp3cleanup.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import at.freakey.mp3cleanup.cleanup.Cleanup;

import javax.swing.JTextField;
import javax.swing.JLabel;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4918451202570857012L;
	private JPanel contentPane;
	private JList<String> list;
	private JScrollPane listScroller;
	private List<File> fileList;
	private JTextField folderFormat, fileNameFormat;
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fileList = new ArrayList<File>();
		
		setResizable(false);
		setTitle("MP3Cleanup by Freakey");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 425, 399);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel listMenuPanel = new JPanel();
		listMenuPanel.setBorder(BorderFactory.createTitledBorder("List Menu"));
		listMenuPanel.setBounds(302, 11, 108, 250);
		contentPane.add(listMenuPanel);
		listMenuPanel.setLayout(null);
		
		JButton addButton = new JButton("Add");
		addButton.setBounds(10, 24, 89, 23);
		
		addButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setDialogTitle("Select mp3 files!");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new FileNameExtensionFilter(".mp3-files", "mp3"));
				
				int result = chooser.showOpenDialog(null);
				
				if(result == JFileChooser.APPROVE_OPTION) {
					for(File file : chooser.getSelectedFiles()) {
						addFile(file);
					}
				}
			}
		});
		
		listMenuPanel.add(addButton);
		
		JButton removeButton = new JButton("Remove");
		removeButton.setBounds(10, 58, 89, 23);
		
		removeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndices().length != 0) {
					for(int i : list.getSelectedIndices()) {
						removeFile(fileList.get(i));
					}
					updateList();
				}
			}
		});
		
		listMenuPanel.add(removeButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(10, 92, 89, 23);
		
		clearButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				fileList.clear();
				updateList();
			}
		});
		
		listMenuPanel.add(clearButton);
		
		list = new JList<String>();
		listScroller = new JScrollPane(list);
		listScroller.setBounds(10, 11, 282, 250);
		contentPane.add(listScroller);
		list.setOpaque(false);
		
		listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		folderFormat = new JTextField();
		folderFormat.setBounds(10, 294, 282, 20);
		contentPane.add(folderFormat);
		folderFormat.setColumns(10);
		
		fileNameFormat = new JTextField();
		fileNameFormat.setColumns(10);
		fileNameFormat.setBounds(10, 339, 282, 20);
		contentPane.add(fileNameFormat);
		
		JLabel folderFormatLabel = new JLabel("Folder format");
		folderFormatLabel.setBounds(10, 280, 282, 14);
		contentPane.add(folderFormatLabel);
		
		folderFormat.setText("{ARTIST}/{ALBUM}/{FILES}");
		fileNameFormat.setText("{ARTIST} - {TITLE}, {YEAR}.mp3");
		
		JLabel fileNameFormatLabel = new JLabel("Filename format");
		fileNameFormatLabel.setBounds(10, 325, 282, 14);
		contentPane.add(fileNameFormatLabel);
		
		JPanel programPanel = new JPanel();
		programPanel.setBounds(302, 272, 107, 87);
		contentPane.add(programPanel);
		programPanel.setLayout(null);
		programPanel.setBorder(BorderFactory.createTitledBorder("Cleanup"));
		
		JButton startButton = new JButton("Start");
		startButton.setBounds(10, 22, 89, 23);
		
		startButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(fileList.size() == 0) {
					JOptionPane.showMessageDialog(null, "There are no mp3 files to clean up!");
					return;
				}
				
				Cleanup cl = new Cleanup(fileList, folderFormat.getText(), fileNameFormat.getText());
				cl.start();
			}
		});
		
		programPanel.add(startButton);
		
	}
	
	/**
	 * Add a file
	 * @param file the file
	 */
	private void addFile(File file) {
		if(!fileList.contains(file)) fileList.add(file);
		updateList();
	}
	
	
	/**
	 * Remove a file
	 */
	private void removeFile(File file) {
		fileList.remove(file);
		updateList();
	}
	
	/**
	 * Remove non-existing files in the list
	 */
	private void purgeFileList() {
		for(int i = 0; i < fileList.size(); i++) {
			if(fileList.get(i) == null || !fileList.get(i).exists()) {
				fileList.remove(i);
			}
		}
	}
	
	/**
	 * Update the list, add all the new mp3 names
	 */
	private void updateList() {
		purgeFileList();
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(File file : fileList) {
			model.addElement(file.getName());
		}
		
		list.setModel(model);
	}
}
