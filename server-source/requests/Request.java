package requests;

import json.JSONObject;

public class Request {
	/*
	 * - Id = 1 
	 * - Version = 1
	 * - Name = Notification|GetChannel|CreateMessage|SendFile
	 * - Data { ... }
	 * 
	 * In case of a command,
	 * 
	 * - Id = 1 
	 * - Version = 1
	 * - Name = Command
	 * - Data {
	 *    name: RandomMeme|Gamble|Ban
	 *    channel: general
	 *  }
	 */
	public JSONObject content;

	public Request(int version, String id, String name, JSONObject data) {
		this.content = new JSONObject();
		this.content.put("id", id);
		this.content.put("name", name);
		this.content.put("data", data);
		this.content.put("version", version);
	}
}
