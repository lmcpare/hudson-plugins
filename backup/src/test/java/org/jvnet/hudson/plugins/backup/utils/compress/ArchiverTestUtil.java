package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ArchiverTestUtil {

	public static void addTestFiles(Archiver archiver) throws ArchiverException {
		archiver.addFile("file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/file1").getFile()));
		archiver.addFile("dir1/file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file1")
				.getFile()));
		archiver.addFile("dir1/file2", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file2")
				.getFile()));
        archiver.addFile("dir$dollar/file$dollar.txt", new File(Thread.currentThread()
                .getContextClassLoader().getResource("data/dir$dollar/file$dollar.txt")
                .getFile()));

		archiver.close();
	}
	
	/**
	 * Compare the content of 2 dufferents directory
	 * @param directory1
	 * @param directory2
	 * @return
	 * @throws IOException
	 */
	public static boolean compareDirectoryContent(File directory1, File directory2)
			throws IOException {
		if (!directory1.isDirectory() || !directory2.isDirectory()) {
			return false;
		}

		File list1[] = directory1.listFiles();
		File list2[] = directory2.listFiles();

		if (list1.length != list2.length) {
			return false;
		}

		for (int i = 0; i < list1.length; i++) {
			File file1 = list1[i];
			File file2 = list2[i];

			if (!file1.getName().equals(file2.getName())) {
				return false;
			}

			if (file1.isDirectory()) {
				return compareDirectoryContent(file1, file2);
			} else {
				return FileUtils.contentEquals(file1, file2);
			}
		}
		return true;
	}

}
