package com.yq.xcode.security.crypto.coder;

import java.util.Arrays;
import java.util.LinkedList;

public class Base64StyleDataCoder implements DataCoder<byte[],String> {

	private final static char[] DEFAULT_DIGITS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ+/".toCharArray();
	private final static char SPECIAL_CHAR = '*';
	private static int BAD = -1;
	private static int[] DEFAULT_DIGIT_VALUES ;
	static {
		DEFAULT_DIGIT_VALUES = parseDigitValues(DEFAULT_DIGITS);
	}
	
	private char[] digits = DEFAULT_DIGITS;
	private int[] digitValues = DEFAULT_DIGIT_VALUES;
	
	
	public static Base64StyleDataCoder randomCoder() {
		
		return new Base64StyleDataCoder(randomDigits());
	}
	public Base64StyleDataCoder() {
		
	}
	
	public static char[] randomDigits() {
		char[] digits = new char[64];
		LinkedList<Character> digitList = new LinkedList<Character>();
		for(int i = 0;i < DEFAULT_DIGITS.length;i++) {
			digitList.add(DEFAULT_DIGITS[i]);
		}
		for(int i = 0;i < digits.length;i++) {
			int random = (int)(Math.random() * (64-i));
			digits[i] = digitList.remove(random);
		}
//		System.out.println(digits);
		return digits;
	}
	
	public Base64StyleDataCoder(char[] digits) {
		if(digits.length != 64) {
			throw new java.lang.IllegalArgumentException("Length of Digits should equal to 64.");
		}
		this.digits = digits;
		this.digitValues = parseDigitValues(digits);
	}
	
	protected static int[] parseDigitValues(char[] digits) {
		int[] values = new int[128];
		Arrays.fill(values, BAD);
		for(int i = 0;i < digits.length;i++) {
			values[digits[i]] = i;
		}
		return values;
	}

	@Override
	public String encode(byte[] in) {
		int inIndex = 0;
		int outIndex = 0;
		int inlen = in.length;
		char[] out = new char[inlen /3 * 4 +(inlen%3==0?0:4)];
		for (; inlen >= 3; inlen -= 3) {
			out[outIndex++] = digits[toPositiveInt(in[inIndex]) >> 2];
			out[outIndex++] = digits[((toPositiveInt(in[inIndex]) << 4) & 0x30) | (toPositiveInt(in[inIndex + 1]) >> 4)];
			out[outIndex++] = digits[((toPositiveInt(in[inIndex + 1]) << 2) & 0x3c) | (toPositiveInt(in[inIndex + 2]) >> 6)];
			out[outIndex++] = digits[toPositiveInt(in[inIndex + 2]) & 0x3f];
			inIndex += 3;
		}
		if (inlen > 0) {
			int fragment;
			out[outIndex++] = digits[toPositiveInt(in[inIndex]) >> 2];
			fragment = (toPositiveInt(in[inIndex]) << 4) & 0x30;
			if (inlen > 1)
				fragment |= toPositiveInt(in[inIndex + 1]) >> 4;
			out[outIndex++] = digits[fragment];
			out[outIndex++] = (inlen < 2) ? SPECIAL_CHAR : digits[(in[inIndex + 1] << 2) & 0x3c];
			out[outIndex++] = SPECIAL_CHAR;
		}
		return new String(out);
	}
	
	protected static int toPositiveInt(byte b) {
		return b>=0?b:(256+b);
	}

	@Override
	public byte[] decode(String src) {
		if(src == null) {
			return null;
		}
		int mod = src.length()%4;
		if(mod > 0) {
			StringBuffer buf = new StringBuffer(src);
			for(int i = 4 - mod;i> 0;i--) {
				buf.append(SPECIAL_CHAR);
			}
			src = buf.toString();
		}
		char[] in = src.toCharArray();
		int len = 0;
		int inIndex = 0;
		int outIndex = 0;
		byte[] out = new byte[in.length/4 * 3];
		char digit1, digit2, digit3, digit4;
		if (in[inIndex] == '+' && in[inIndex + 1] == ' ')
			inIndex += 2;
		if (in[inIndex] == '\r')
			return null;
		do {
			digit1 = in[inIndex];
			if (digitValues[digit1] == BAD)
				return null;
			digit2 = in[inIndex+1];
			if (digitValues[digit2] == BAD)
				return null;
			digit3 = in[inIndex+2];
			if (digit3 != SPECIAL_CHAR && digitValues[digit3] == BAD)
				return null;
			digit4 = in[inIndex+3];
			if (digit4 != SPECIAL_CHAR && digitValues[digit4] == BAD)
				return null;
			inIndex += 4;
			out[outIndex++] = (byte) ((digitValues[digit1] << 2) | (digitValues[digit2] >> 4));
			++len;
			if (digit3 != SPECIAL_CHAR) {
				out[outIndex++] = (byte) (((digitValues[digit2] << 4) & 0xf0) | (digitValues[digit3] >> 2));
				++len;
				if (digit4 != SPECIAL_CHAR) {
					out[outIndex++] = (byte) (((digitValues[digit3] << 6) & 0xc0) | digitValues[digit4]);
					++len;
				}
			}

		} while (inIndex < in.length && in[inIndex] != '\r' && digit4 != SPECIAL_CHAR);
		byte[] result = new byte[len];
		System.arraycopy(out, 0, result, 0, len);
		return result;
	}
	
	
	
	public static void main(String[] args) {
		Base64StyleDataCoder coder = Base64StyleDataCoder.randomCoder();
//		Base64StyleCoder coder = new Base64StyleCoder();
		byte[] data = new byte[]{-4,5,6};
		String encoded = coder.encode(data);
		System.out.print("Original:\t");
		for(int i = 0;i < data.length;i++) {
			System.out.print(data[i]);
			System.out.print(" ");
		}
		System.out.println();
		for(int i = 0;i < data.length;i++) {
			System.out.print(Integer.toBinaryString(data[i]));
			System.out.print(" ");
		}
		System.out.println("\nEncoded:\t"+encoded);
		data =  coder.decode(encoded);

		for(int i = 0;i < encoded.length();i++) {
			System.out.print(Integer.toBinaryString(encoded.charAt(i)));
			System.out.print(" ");
		}
		System.out.println();
		System.out.print("Decoded:\t");
		for(int i = 0;i < data.length;i++) {
			System.out.print(data[i]);
			System.out.print(" ");
		}
		System.out.println();
		for(int i = 0;i < data.length;i++) {
			System.out.print(Integer.toBinaryString(data[i]));
			System.out.print(" ");
		}
	}
		
}
