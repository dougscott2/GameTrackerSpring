package com.theironyard;

import com.theironyard.entities.Game;
import com.theironyard.entities.User;
import com.theironyard.services.GameRepository;
import com.theironyard.services.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
				.sessionAttr("username", "Test Name")
		);
		assertTrue(games.count()==1);
	}
	@Test
	public void testDeleteGame() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", "Test Name")
						.param("password", "testPassword")
		);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/add-game")
						.param("title", "Halo")
						.param("system", "Xbox360")
						.sessionAttr("username", "Test Name")
		);
		//System.out.println(games.f);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/delete-game")
						.param("id", games.findAll().iterator().next().id + "")
						.sessionAttr("username", "Test Name")
		);
		assertTrue(games.count()==0);
	}

	@Test
	public void testEditGame() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", "Test Name")
						.param("password", "testPassword")
		);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/add-game")
						.param("title", "Halo")
						.param("system", "Xbox360")
						.sessionAttr("username", "Test Name")
		);
		int x = games.findAll().iterator().next().id;
		mockMvc.perform(
				MockMvcRequestBuilders.post("/edit-game")
						.param("id", x +"")
						.param("edName", "Halo2")
						.param("edSystem", "Xbox360")
						.sessionAttr("username", "Test Name")
		);
		assertTrue(games.findOne(x).title.equals("Halo2"));
	}


}
