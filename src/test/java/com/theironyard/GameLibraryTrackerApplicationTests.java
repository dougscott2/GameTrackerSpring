package com.theironyard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GameLibraryTrackerApplication.class)
@WebAppConfiguration
public class GameLibraryTrackerApplicationTests {

	@Autowired
	GameRepository games;

	@Autowired
	UserRepository users;

	@Autowired
	WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
	public void Before(){
		games.deleteAll();
		users.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	}
	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", "Test Name")
						.param("password", "testPassword")
		);
		assertTrue(users.count() == 1);
	}
	@Test
	public void testAddGame() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.post("/add-game")
				.param("title", "Halo")
				.param("system", "Xbox360")
				.sessionAttr("username", "doug")
		);
		assertTrue(games.count()==1);
	}
	@Test
	public void testDeleteGame() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.post("/add-game")
						.param("title", "Halo")
						.param("system", "Xbox360")
						.sessionAttr("username", "doug")
		);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/add-game")
						.param("title", "Halo2")
						.param("system", "Xbox360")
						.sessionAttr("username", "doug")
		);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/delete-game")
				.param("id", "1")
		);
		assertTrue(games.count()==1);
	}


}
