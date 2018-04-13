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
		user.setName("admin");
		user.setPassword("0925");
		userService.register(user);
	}

}
