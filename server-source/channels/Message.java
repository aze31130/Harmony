package channels;

import java.util.Date;
import java.util.List;
import cryptography.Hash;
import json.IJSONAble;
import json.JSONObject;

public class Message implements IJSONAble {
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

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("id", this.id);
		result.put("content", this.content);
		result.put("sent", this.sent.toString());

		return result;
	}
}