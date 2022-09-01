package events;

import channels.Message;
import channels.TextChannel;
import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import users.User;

public class MessageReceiveEvent extends Event {
	public User sender;
	public Message message;

	public MessageReceiveEvent(TextChannel channel, User sender, Message message) {
		super(RequestType.REQUEST, RequestName.CREATE_MESSAGE);
		this.sender = sender;
		this.message = message;
	}

	@Override
	public void fire(JSONObject arguments) {
		// TODO Auto-generated method stub
		
	}
}