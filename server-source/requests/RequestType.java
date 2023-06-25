package requests;

public enum RequestType {
	REQUEST, //When a client wants to perform an action (get message, send message, etc...)
	CONNECTION, //When a client wants to instanciate a connection with the server
	COMMAND, //When the client is sending a command in a channel
	RESPONSE, //When the server answers to a request
	NOTIFICATION, //When the server is sending notifications to clients
	ERROR //When an error occured
}
