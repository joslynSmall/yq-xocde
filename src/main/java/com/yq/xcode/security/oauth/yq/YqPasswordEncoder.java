package com.yq.xcode.security.oauth.yq;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class YqPasswordEncoder implements PasswordEncoder {

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword)  {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		boolean isMatch = bCryptPasswordEncoder.matches(encodedPassword,rawPassword.toString());
		return isMatch;
	}
    @Override
    public String encode(CharSequence rawPassword) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encrypted = bCryptPasswordEncoder.encode(rawPassword);
            return encrypted;
    }
	public static void main(String[] args) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encrypted = bCryptPasswordEncoder.encode("123456");
		System.out.println(encrypted);
		System.out.println(bCryptPasswordEncoder.matches("123456", encrypted));
	}
}
