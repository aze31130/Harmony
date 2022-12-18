package channels;

public class VoiceChannel extends Channel {
	public int maxMembers;
	public int bitRate;

	public VoiceChannel(String name, String description, Boolean ageRestricted) {
		super(name, description, ageRestricted);
		
	}
}