package com.koubal.image2hex;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Main {
	private static String FILE_NAME = "image.xml";
	private static String FILE_HEADDER = "v2.0 raw\n";
	private static String DEFAULT_IMAGE_NAME = "image";
	private static String DEFAULT_IMAGE_EXTENSION = ".png";

	public static void main(String[] args) {
		String imageName;
		String imageExtension = null;

		if (args.length > 0) {
			imageName = args[0];
		} else {
			imageName = DEFAULT_IMAGE_NAME;
		}

		if (args.length > 1) {
			imageExtension = "." + args[1];
		} else {
			imageExtension = DEFAULT_IMAGE_EXTENSION;
		}

		String output = FILE_HEADDER;

		for (int i = 0; ; i++) {
			File imageFile = new File(imageName + i + imageExtension);

			if (!imageFile.exists()) {
				System.out.println("Image '" + imageFile.getName() + "' not found, exiting!");
				break;
			}

			BufferedImage image = null;

			try {
				image = ImageIO.read(imageFile);
			} catch (IOException e) {
				System.out.println("Error reading image: " + e);
			}

			int temp = 0;
			int count = 0;

			for (int y = image.getHeight() - 1; y >= 0; y--) {
				for (int x = 0; x < image.getWidth(); x++) {
					count++;

					int colour = image.getRGB(x, y);

					int red = (colour & 0x00ff0000) >> 16;
					int green = (colour & 0x0000ff00) >> 8;
					int blue = (colour & 0x000000ff);

					int brightness = red + green + blue;

					if (brightness != 0) {
						temp += Math.pow(2, 3 - ((count - 1) % 4));
					}

					if (count != 0) {
						if (count % 4 == 0) {
							output += Integer.toHexString(temp);
							temp = 0;
						}
						if (count % 128 == 0) {
							output += "\n";
						} else if (count % 16 == 0) {
							output += " ";
						}
					}
				}
			}
		}
		PrintWriter writer = null;

		System.out.println(output);

		try {
			writer = new PrintWriter(FILE_NAME);
		} catch (FileNotFoundException e) {
			System.out.println("Error creating file: " + e);
		}

		writer.print(output);

		writer.close();
	}
}
