package channels;

import java.util.Date;
import java.util.List;

public class Message {
	public int id;
	public String content;
	public Date sent;
	public List<Reaction> reactions;
	public Boolean isEdited;
	public Boolean isPinned;
	public Boolean isSigned;
}