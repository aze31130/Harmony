package events;

import channels.Message;
import channels.TextChannel;
import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import users.User;

public class MessageReceiveEvent extends Event {

	public TextChannel channel;
	public Message message;
	public User author;

	public MessageReceiveEvent() {
		super(RequestType.REQUEST, RequestName.CREATE_MESSAGE);
	}

	@Override
	public void fire(JSONObject data) {
		/*
		 * Arguments:
		 * @String: author (the name of the author)
		 * @String: channel (the id of the channel)
		 * @String: message (the content of the message)
		 */
		// What do we do when we receive a message ?
		// Broadcast to other users

		//Check for achievement triggers ?
	}
}