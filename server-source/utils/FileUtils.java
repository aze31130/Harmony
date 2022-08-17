package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
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

	public static void createFile(String fileName, String content) {
		if (!isFileExists(fileName)) {
			try {
				FileWriter f = new FileWriter(fileName);
				f.write(content);
				f.flush();
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
