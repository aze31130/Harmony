package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
	public static String readRawFile(String filename) {
		try {
			return new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String();
	}

	public static void writeRawFile(String filename, String content) {
		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static Boolean isFileExists(String fileName) {
		return new File(fileName).exists();
	}

	public static Boolean isFolderExists(String folderName) {
		File folder = new File(folderName);
		return folder.exists() && folder.isDirectory();
	}

	public static void createFolder(String folderName) {
		if (!isFolderExists(folderName))
			new File(folderName).mkdir();
	}
}
