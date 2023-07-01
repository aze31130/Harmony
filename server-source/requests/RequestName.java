package requests;

public enum RequestName {
	/*
	 * Get the list of users with their status (online, offline), activities (playing, listening)
	 * and their metadata (roles, profile picture url, time joined, etc...)
	 * 
	 */
	GET_USERS,
	/*
	 * Get the list of accessible channel. The server will filter what channel the user is allowed to see.
	 */
	GET_CHANNELS,
	/*
	 * Get the list of commands.
	 */
	GET_COMMANDS,
	/*
	 * Get the last 50 messages (by default) in a given channel id.
	 */
	GET_MESSAGES,
	/*
	 * Send a message inside a text channel.
	 */
	CREATE_MESSAGE,
	/*
	 * Edit a message that the user sent.
	 */
	EDIT_MESSAGE,
	/*
	 * Delete a message. The user must be the sender or has privilege access.
	 */
	DELETE_MESSAGE,
	/*
	 * When a priviledged user wants to shutdown the server.
	 */
	COMMAND_SHUTDOWN,
	/*
	 * When the server is sending notifications to clients. This include a short version of the
	 * message to not make the client download the content.
	 */
	NOTIFICATION,
	/*
	 * When the client wants to upload a file
	 */
	SEND_FILE
}
