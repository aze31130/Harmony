package logger;

public class Logger {
    public Logger() {
        
    }

    public static void log(LogLevel level, String message) {
        System.out.println(level.toString() + " " + message);
    }
}