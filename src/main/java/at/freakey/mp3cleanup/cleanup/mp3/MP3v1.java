package at.freakey.mp3cleanup.cleanup.mp3;

import com.mpatric.mp3agic.ID3v1;

public class MP3v1 implements IMp3File {
	
	private ID3v1 tag;
	
	public MP3v1(ID3v1 tag) {
		this.tag = tag;
	}
	
	public ID3v1 getTag() {
		return tag;
	}

	public String getTrack() {
		return tag.getTrack();
	}

	public String getAlbum() {
		return tag.getAlbum();
	}

	public String getArtist() {
		return tag.getArtist();
	}


	public String getYear() {
		return tag.getYear();
	}

	public String getVersion() {
		return tag.getVersion();
	}

	public void setYear(String year) {
		tag.setYear(year);
		
	}

	public void setArtist(String artist) {
		tag.setArtist(artist);
		
	}

	public void setAlbum(String album) {
		tag.setAlbum(album);
		
	}
	
	public void setTrack(String track) {
		tag.setTrack(track);
		
	}

	public String getTitle() {
		return tag.getTitle();
	}

	public void setTitle(String title) {
		tag.setTitle(title);
		
	}

}
