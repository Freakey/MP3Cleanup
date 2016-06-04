package at.freakey.mp3cleanup;

import at.freakey.mp3cleanup.gui.MainWindow;

public class Core {
	
	private static Core instance;
	private MainWindow mainWindow;
	
	private Core() {}
	
	private void init() {
		System.out.println("Inititalizing MP3Cleanup.");
		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		
		System.out.println("");
		System.out.println("   MP3Cleanup, (C) 2016, Adrian Kaiser");
		System.out.println("");
		System.out.println("");
	}
	
	public static synchronized Core getInstance() {
		return (instance == null) ? (instance = new Core()) : instance;
	}
	
	public static void main(String[] args) {
		getInstance().init();
	}
	
	

}
