package de.bgrci.verzeichnis;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Date;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class VerzeichnisSort {
	public static void main(String[] args) {
		durchsucheVerzeichns(new File("d:\\tmp\\joerg\\"));
	}
	
	/*
	 * durchsucht Verzeichnis und Unterverzeichnisse
	 */
	public static void durchsucheVerzeichns(File pfad) {
		
		for (File f : pfad.listFiles()) {
			if (f.isDirectory()) durchsucheVerzeichns(f);
		 else {
				if (f.getName().toLowerCase().endsWith(".jpg")) speicherBild(f);
			}
		}
	}
	
	private static void speicherBild(File bild) {
		try {
			System.out.println("\nBild " + bild.getName());
			Metadata metadata = JpegMetadataReader.readMetadata(bild);

			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

			Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			
			System.out.println("Datum : " + date);
			
			System.out.println("Pfad " + getPfad(date));
			
			try {
				Files.createDirectories(Path.of(new File("d:\\tmp\\" + getPfad(date)).toURI()));
				Files.copy(Path.of(bild.getAbsoluteFile().toURI()), Path.of(   (new File("d:\\tmp\\" + getPfad(date) + bild.getName())).toURI()   ) , java.nio.file.StandardCopyOption.REPLACE_EXISTING );
			} catch (Exception a) {
				System.out.println("Kopieren fehlgeschlagen! " + bild.getName());
				a.printStackTrace();
			}
		} catch (ImageProcessingException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Keine EXIF Daten.");
		}

	}

	private static String getPfad(Date date) {
		String pfad = "";
		
		pfad = (date.getYear() + 1900) + "\\" + (date.getMonth() + 1) + "\\";
		
		return pfad;
	}
}
