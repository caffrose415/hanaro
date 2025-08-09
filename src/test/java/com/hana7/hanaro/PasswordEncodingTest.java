package com.hana7.hanaro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncodingTest {
	@Autowired
	PasswordEncoder encoder;

	@Test
	void printBcrypt(){
		System.out.println(encoder.encode("12345678"));
	}
}
