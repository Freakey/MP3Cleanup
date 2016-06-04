package at.freakey.mp3cleanup.cleanup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import at.freakey.mp3cleanup.cleanup.mp3.IMp3File;
import at.freakey.mp3cleanup.cleanup.mp3.MP3v1;
import at.freakey.mp3cleanup.cleanup.mp3.MP3v2;

public class Cleanup {
	
	private List<File> mp3s;
	private String folderFormat, fileNameFormat;
	private File folder;
	private Map<File, IMp3File> newFiles = new HashMap<File, IMp3File>();
	
	public Cleanup(List<File> mp3s, String folderFormat, String fileNameFormat) {
		this.mp3s = mp3s;
		this.folderFormat = folderFormat;
		this.fileNameFormat = fileNameFormat;
	}
	
	/**
	 * Start the cleanup
	 */
	public void start() {
		System.out.println("Starting cleanup ...");
		purgeFileList();
		folder = getFolder();
		generateFolderSystem();
		moveFiles();
	}
	
	/**
	 * Generate a folder system for the given files
	 */
	private void generateFolderSystem() {
		for(File file : mp3s) {
			generate(file);
		}
	}
	
	/**
	 * Move all files in their folders
	 */
	private void moveFiles() {
		purgeFileList();
		for(File file : mp3s) {
			IMp3File mp3 = null;
			if(newFiles.containsKey(file)) {
				mp3 = newFiles.get(file);
			} else mp3 = fromFile(file);
			
			finalizeMp3(mp3, file);
			File newFile = new File(folder.getPath() + "/" + format(folderFormat.replace("{FILES}", ""), mp3), format(fileNameFormat, mp3));
			System.out.println("Moving file: " + file.getName());
			try {
				Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
				System.out.println("[ERROR] Couldn't move the file " + file.getName());
			}
		}
		
		JOptionPane.showMessageDialog(null, "Finished sorting your .mp3-files!");
	}
	
	private void finalizeMp3(IMp3File mp3, File file) {
		while(mp3.getTitle() == null) {
			mp3.setTitle(JOptionPane.showInputDialog(null, "Please enter a title for: ("+file.getName()+")"));
		}
		
		while(mp3.getTrack() == null) {
			mp3.setTrack(JOptionPane.showInputDialog(null, "Please enter a track for (number): ("+file.getName()+")"));
		}
		
		while(mp3.getArtist() == null) {
			mp3.setArtist(JOptionPane.showInputDialog(null, "Please enter an artist for: ("+file.getName()+")"));
		}
		
		while(mp3.getYear() == null) {
			mp3.setYear(JOptionPane.showInputDialog(null, "Please enter a year for: ("+file.getName()+")"));
		}
		
		while(mp3.getAlbum() == null) {
			mp3.setAlbum(JOptionPane.showInputDialog(null, "Please enter an album for: ("+file.getName()+")"));
		}
	}
	
	 /**
	  * Generate new folders from the file
	  * @param file the file
	  */
	private void generate(File file) {
		final IMp3File mp3 = fromFile(file);
		if(canFormat(folderFormat, mp3)) {
			File f = new File(folder.getPath() + "/" + format(folderFormat.replace("{FILES}", ""), mp3));;
			f.mkdirs();
			System.out.println("Creating folder: " + f.getName());
		} else {
			finalizeMp3(mp3, file);
			try {
				Mp3File mp3file = new Mp3File(file);
				if(mp3file.hasId3v1Tag()) {
					mp3file.setId3v1Tag(((MP3v1)mp3).getTag());
				} else if(mp3file.hasId3v2Tag()) {
					mp3file.setId3v2Tag(((MP3v2)mp3).getTag());
				}
				mp3file.save(file.getName());
			} catch (UnsupportedTagException e) {
				System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") has an unsupported tag");
			} catch (InvalidDataException e) {
				System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") has got invalid data");
			} catch (IOException e) {
				System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") couldn't be loaded");
			} catch (NotSupportedException e) {
				System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") is not supported");
			}
			
			newFiles.put(file, mp3);
			File f = new File(folder.getPath() + "/" + format(folderFormat.replace("{FILES}", ""), mp3));
			System.out.println("Creating folder " + f.getName());
			f.mkdirs();
		}
	}
	
	/**
	 * Get a folder from a JFileChooser
	 * @return the selected folder
	 */
	private File getFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a folder where the mp3s should get moved to and formatted");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int result = chooser.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		
		return null;
	}
	
	/**
	 * Check if the String can be formatted
	 * @param s the string
	 * @param mp3 the imp3file
	 * @return if the String can be formatted
	 */
	private boolean canFormat(String s, IMp3File mp3) {
		if(mp3.getTitle() == null || mp3.getTrack() == null || mp3.getAlbum() == null || mp3.getArtist() == null || mp3.getYear() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Format the given String, replace all placeholders
	 * @param s the String to be formatted
	 * @param mp3 the mp3 file for the format
	 * @return the formatted String
	 */
	private String format(String s, IMp3File mp3) {
		String str = new String(s);
		if(str.contains("{TITLE}")) str = str.replace("{TITLE}", mp3.getTitle());
		if(str.contains("{TRACK}")) str = str.replace("{TRACK}", mp3.getTrack());
		if(str.contains("{ALBUM}")) str = str.replace("{ALBUM}", mp3.getAlbum());
		if(str.contains("{ARTIST}")) str = str.replace("{ARTIST}", mp3.getArtist());
		if(str.contains("{YEAR}")) str = str.replace("{YEAR}", mp3.getYear());
		
		return str;
	}
	
	/**
	 * Get an IMp3File from the file
	 * @param file The file
	 * @return the IMP3File instance, if there is an invalid tag it returns null
	 */
	public IMp3File fromFile(File file) {
		try {
			Mp3File f = new Mp3File(file);
			
			if(f.hasId3v1Tag()) return new MP3v1(f.getId3v1Tag());
			if(f.hasId3v2Tag()) return new MP3v2(f.getId3v2Tag());
			return null;
			
		} catch (UnsupportedTagException e) {
			System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") has an unsupported tag");
		} catch (InvalidDataException e) {
			System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") has got invalid data");
		} catch (IOException e) {
			System.out.println("[ERROR] The selected .mp3-file ("+file.getName()+") couldn't be loaded");
		}
		
		return null;
	
	}
	
	/**
	 * Remove non-existing files in the list
	 */
	private void purgeFileList() {
		for(int i = 0; i < mp3s.size(); i++) {
			if(mp3s.get(i) == null || !mp3s.get(i).exists()) {
				mp3s.remove(i);
			}
		}
	}

	/**
	 * Generate a new folder from the given mp3
	 * @param mp3 the mp3
	 */
	public void generate(IMp3File mp3) {
		new File(folder.getPath() + "/" + format(folderFormat, mp3)).mkdirs();
	}

}
