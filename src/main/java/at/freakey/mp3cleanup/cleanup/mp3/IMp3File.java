package at.freakey.mp3cleanup.cleanup.mp3;

/**
 * For multiversion mp3 files
 *
 */
public interface IMp3File {
	
	/**
	 * Get the mp3s track
	 * @return the track
	 */
	String getTrack();
	
	/**
	 * Get the mp3s album
	 * @return the album
	 */
	String getAlbum();
	
	/**
	 * Get the mp3s artist
	 * @return the artist
	 */
	String getArtist();
	
	/**
	 * Get the mp3s year
	 * @return the years
	 */
	String getYear();
	
	/**
	 * Get the mp3s title
	 * @return the title
	 */
	String getTitle();
	
	void setYear(String year);
	void setArtist(String artist);
	void setAlbum(String album);
	void setTrack(String track);
	void setTitle(String title);
}
