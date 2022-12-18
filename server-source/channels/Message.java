package channels;

import java.util.Date;
import java.util.List;

import cryptography.Hash;

public class Message {
	public int id;
	public String content;
	public Date sent;
	public List<Reaction> reactions;
	public Boolean isEdited;
	public Boolean isPinned;
	public Boolean isSigned;
	public String hash;

	public Message(String content) {

		//The database will set the id for us, for now we will set it to 0 and change it later
		this.id = 0;
		this.content = content;
		this.sent = new Date();
		this.hash = Hash.sha256sum(content);
	}
}