package com.zzwarn.options;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.iharder.Base64;


public class DecodeImage {
	public static void cvtStr2Img(String encodedImgStr, String str_save_path) {
		try {
			byte[] imageByteArray = Base64.decode(encodedImgStr);
			FileOutputStream imageOutFile = new FileOutputStream(str_save_path);
			imageOutFile.write(imageByteArray);
			imageOutFile.close();
			System.out.println("Image Successfully Stored");
		} catch (FileNotFoundException fnfe) {
			System.out.println("Image Path not found" + fnfe);
		} catch (IOException ioe) {
			System.out.println("Exception while converting the Image " + ioe);
		}
	}
}
