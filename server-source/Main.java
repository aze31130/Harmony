public class Main {
	/*
	Entry point of the project
	*/
	public static void main(String[] args) {
		System.out.println("Starting Harmony server version ");
		Server server = Server.getInstance();
		server.start();
	}
}