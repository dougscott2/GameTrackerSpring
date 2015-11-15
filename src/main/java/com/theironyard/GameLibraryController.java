package com.theironyard;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

/**
 * Created by DrScott on 11/13/15.
 */
@Controller
public class GameLibraryController {
    @Autowired
    GameRepository games;
    @Autowired
    UserRepository users;

    @PostConstruct
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException, FileNotFoundException {
        User user = users.findOneByName("Admin");
        if(user == null){
            user = new User();
            user.name="Admin";
            user.password = PasswordHash.createHash("password");
            users.save(user);
        }
    }



    @RequestMapping("/")
    public String home(HttpSession session,
                       Model model,
                      // String search,
                       String system,
                       String userGames){

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);


        if(userGames!=null){
            model.addAttribute("games", users.findOneByName(username).games);
        } else if(system != null){
            model.addAttribute("users", users.findOneByName(username));
            model.addAttribute("games", games.findBySystemOrderByNameAsc(system));
        } else
        model.addAttribute("users", users.findAll());
        model.addAttribute("games", games.findAll());
        return "home";
    }
    @RequestMapping("login")
    public String login(Model model, HttpSession session, String username, String password) throws Exception {
        session.setAttribute("username", username);

        User user = users.findOneByName(username);
        if(user==null){
            user = new User();
            user.name = username;
            user.password = PasswordHash.createHash(password);
            users.save(user);
        } else if (!PasswordHash.validatePassword(password, user.password)){
            throw new Exception("WRONG PASSWORD");
        }
        model.addAttribute("games", games.findAll());

        return "redirect:/";
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("add-game")
    public String addGame(HttpSession session,
                          String gameName,
                          String system
                          ) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null){
            throw new Exception("Not logged in!");
        }
        User user = users.findOneByName(username);
        Game game = new Game();
        game.name = gameName;
        game.system = system;
        game.user=user;
        games.save(game);
        return "redirect:/";
    }
    @RequestMapping("edit-game")
    public String editGame(Integer id,
                           String gameName,
                           String system,
                           HttpSession session
                           ) throws Exception{

        if (session.getAttribute("username")==null){
            throw new Exception("Not logged in!");
        }
        //User user = users.findOne(id);
        //int idNum = Integer.valueOf(id);


        Game game = games.findOne(id);
        User user = users.findOne(game.id);
        game.name = gameName;
        game.system = system;
        game.user = user;
        games.save(game);
        return "redirect:/edit-game";
    }
    @RequestMapping("delete-game")
    public String deleteGame(Integer id){
        games.delete(id);
        return "redirect:/";
    }







}
