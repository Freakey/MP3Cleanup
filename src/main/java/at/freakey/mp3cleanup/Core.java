package at.freakey.mp3cleanup;

import at.freakey.mp3cleanup.gui.MainWindow;

public class Core {
	
	private static Core instance;
	private MainWindow mainWindow;
	
	private Core() {}
	
	private void init() {
		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
	
	public static synchronized Core getInstance() {
		return (instance == null) ? (instance = new Core()) : instance;
	}
	
	public static void main(String[] args) {
		getInstance().init();
	}
	
	

}
