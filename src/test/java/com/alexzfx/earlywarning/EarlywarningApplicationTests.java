package com.alexzfx.earlywarning;

import com.alexzfx.earlywarning.entity.User;
import com.alexzfx.earlywarning.repository.UserRepository;
import com.alexzfx.earlywarning.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EarlywarningApplicationTests {

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
