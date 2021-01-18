package com.yq.xcode.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;

/**
 * AES加密解密类*/

public class AESCryption {
	/*
	 *  JAVA默认支持AES  128 Bit 的key, 如果使用 192 Bit 或者 256 Bit key
	 *  直接使用 192或者 256的key 会出现  Illegal key size 错误
	 *  需要安装Java  Extension (JCE) Unlimited Strength Jurisdiction Policy Files 
	 *  在官方网站下载JCE无限制权限策略文件，下载后解压，
		可以看到local_policy.jar和US_export_policy.jar以及readme.txt，
		如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件；
		如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件
	 */
	
	public static final int KEY_LENTH_128 = 128;
	
	/**
	 * 对数据用AES算法加密
	 * @param plainText  要加密的字节数组
	 * @param key  密钥
	 * @param ivStr 加密向量
	 * @return 加密后的字节数组
	 */
	public static String encrypt(String content, String keyStr, String ivStr) {
		try {
			//加密的字符串转换字节型			byte[] plainText = content.getBytes("UTF-8");
			Base64 objBase64 = new Base64();
			//key和ivStr转换Base64字节
			byte[] key = objBase64.decode(keyStr.getBytes("UTF-8"));
			byte[] ivByte = objBase64.decode(ivStr.getBytes("UTF-8"));
			//实例化加密类，加密方法为：AES
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", getProvider());
			//实例化加密向量			IvParameterSpec iv = new IvParameterSpec(ivByte);
			//初始化加密类
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			//加密
			byte[] decryptText = cipher.doFinal(plainText);
			//加密码后的字节数组转化为Base64位类型			return new BASE64Encoder().encode(decryptText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 对AES算法加密的数据解密
	 * @param cipherText  要解密的字节数组
	 * @param key  密钥
	 * @return 解密后的字节数组
	 */
	public static String decrypt(String cipherText, String keyStr, String ivStr) {
		try {
			//cipherText、key和ivStr转换Base64字节
			byte[] cipherContent = ToBase64.decode(cipherText);
			Base64 objBase64 = new Base64();
			byte[] key = objBase64.decode(keyStr.getBytes("UTF-8"));
			byte[] ivByte = objBase64.decode(ivStr.getBytes("UTF-8"));
			//实例化加密类，加密方法为：AES
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", getProvider());
			//实例化加密向量			IvParameterSpec iv = new IvParameterSpec(ivByte);
			//初始化加密类
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			//解密
			byte[] plainText = cipher.doFinal(cipherContent);
			//解密后的字节数组转化为字符串
			return new String(plainText);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	private static Provider getProvider() {
		Provider[] providers = Security.getProviders();
		Provider p = null;
		for (int i = 0; i < providers.length; i++) {
			if (providers[i].getName().equals("SunJCE")) {
				p = providers[i];
			}
		}
		return p;
	}
	
	public static String convert2SpPwd(String pwd, String[] keyArr){
    	String enPwd = AESCryption.encrypt(pwd, keyArr[0], keyArr[1]);
    	try {
			enPwd = java.net.URLEncoder.encode(enPwd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return enPwd;
    }	
	
	
	/** 
	 * 随机生成秘钥 
	 */  
	public static void genKey(){    
	    try {    
	        KeyGenerator kg = KeyGenerator.getInstance("AES");    
	        kg.init(KEY_LENTH_128);//要生成多少位，只需要修改这里即可128, 192或256    
	        SecretKey sk = kg.generateKey();    
	        byte[] b = sk.getEncoded();    
	        String s = byteToHexString(b);    
	        System.out.println(s);    
	        System.out.println("base64字符串 "+ToBase64.encode(b));
	        System.out.println("十六进制密钥长度为"+s.length());    
	        System.out.println("二进制密钥的长度为"+s.length()*4);    
	    } catch (NoSuchAlgorithmException e) {    
	        e.printStackTrace();    
	        System.out.println("没有此算法。");    
	    }    
	}    
	  
	/** 
	 * 使用指定的字符串生成秘钥 
	 */  
	public static void getKeyByPass(String password) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			// kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
			// SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。、
			kg.init(KEY_LENTH_128, new SecureRandom(password.getBytes()));
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			String s = byteToHexString(b);
			System.out.println(s);
			System.out.println("base64字符串 " + ToBase64.encode(b));
			System.out.println("十六进制密钥长度为" + s.length());
			System.out.println("二进制密钥的长度为" + s.length() * 4);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("没有此算法。");
		}
	}
	
	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String strHex = Integer.toHexString(bytes[i]);
			if (strHex.length() > 3) {
				sb.append(strHex.substring(6));
			} else {
				if (strHex.length() < 2) {
					sb.append("0" + strHex);
				} else {
					sb.append(strHex);
				}
			}
		}
		return sb.toString();
	}
	
	
	public static void main(String[] a) throws UnsupportedEncodingException{
		getKeyByPass("yunqi@83837106");
		getKeyByPass("l9o243");
		System.out.println();
		String k1 = "K.%^63:35.030.-.";
		System.out.println("**** 明文: "+k1);
		String k2 = AESCryption.encrypt(k1, "o3OWNi9iYXx8Yxi4+K6GXg==", "igEw5Y/ufL0K0C3totZ5bQ==");
		k2 = java.net.URLEncoder.encode(k2,"UTF-8");
		System.out.println("**** 加密 ");
		System.out.println("******密文:"+k2);
		k2 = java.net.URLDecoder.decode(k2,"UTF-8");
		System.out.print("**** 解密后 ");
		System.out.println(AESCryption.decrypt(k2,  "o3OWNi9iYXx8Yxi4+K6GXg==", "igEw5Y/ufL0K0C3totZ5bQ=="));
		
		System.out.println();
		
		String k = "Ey98kQx/XXR2sRlGl9qCIPy1W1xXBdPLzBA3SLHsen/1OhNnY0KFYdPky2OfiPEYRYjTEiY7gjOVM8z5NivgU9oH7Q+0Y8h6LbYVRng6OTg=";
		k = java.net.URLEncoder.encode(k,"UTF-8");
		System.out.println("******k:"+k);
		String key = "CGWKB9nlx7a0nPJkOOu20lRSHFct2dyJlHRKZV0JnhzUeidWjqeViiipRbFbFbSKQvrztdXK2RyxCsFn3psNtwsWNezdnOV%2bGoFhqMgJvtI%3d";
		key = java.net.URLDecoder.decode(key,"UTF-8");
		System.out.println(key);
		System.out.println(AESCryption.decrypt(key, "JH5WAWptF3sqJARHFHkNTA==", "SFZPZW9qGyVPYxo4VFNcZw=="));
	}
}
