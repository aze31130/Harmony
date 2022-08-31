package requests;

public enum Name {
	GET_USERS,
	GET_CHANNELS,
	GET_COMMANDS,
	GET_MESSAGES, //50 last messages with given channel id
	CREATE_MESSAGE, //with given channel id and content of the message
	EDIT_MESSAGE,
	DELETE_MESSAGE
}
