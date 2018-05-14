package com.alexzfx.earlywarninguser;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EarlywarningUserApplicationTests {

	@Autowired
	private UserService userService;


	@Test
	public void contextLoads() {
	}

	@Test
	public void testRegister(){
		User user = new User();
        user.setUsername("admin");
		user.setName("admin");
        user.setEmail("z1079911968@163.com");
        user.setPassword("123.abc");
		userService.register(user);
	}

}
