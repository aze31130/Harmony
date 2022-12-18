package channels;

public abstract class Channel {
	public int id;
	public String name;
	public String description;
	public Boolean ageRestricted;

	public Channel(String name, String description, Boolean ageRestricted) {
		this.name = name;
		this.description = description;
		this.ageRestricted = ageRestricted;
	}
}