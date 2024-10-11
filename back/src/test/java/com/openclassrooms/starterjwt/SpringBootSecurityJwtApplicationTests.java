package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(AuthConfig.class)
public class SpringBootSecurityJwtApplicationTests {

	@Test
	public void contextLoads() {
	}

}
