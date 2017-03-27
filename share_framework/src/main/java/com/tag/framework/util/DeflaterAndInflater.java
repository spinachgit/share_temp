package com.tag.framework.util;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeflaterAndInflater {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeflaterAndInflater.class);

	public static byte[] compressBytes(byte[] input) {
		Deflater compresser = new Deflater();
		compresser.setInput(input);
		compresser.finish();
		byte[] temp = new byte[input.length + 128];
		int compressedDataLength = compresser.deflate(temp);
		byte[] output = Arrays.copyOf(temp, compressedDataLength);

		return output;
	}

	public static byte[] decompressBytes(byte[] input) {
		Inflater decompresser = new Inflater();
		decompresser.setInput(input);
		byte[] temp = new byte[1024];
		byte[] output = new byte[0];
		while (true){
			if (!decompresser.needsInput()) {
				try {
					int decompressedDateLength = decompresser.inflate(temp);
					int currentLen = output.length;
					output = Arrays.copyOf(output, currentLen + decompressedDateLength);
					System.arraycopy(temp, 0, output, currentLen, decompressedDateLength);
				} catch (DataFormatException e) {
					e.printStackTrace();
					LOGGER.error("解压字节数组出错.");
				}
			}
		}
		//decompresser.end();
		//return output;
	}
}