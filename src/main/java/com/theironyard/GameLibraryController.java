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

        if (games.count() == 0){
            Scanner scanner = new Scanner(new File("games.csv"));
            scanner.nextLine();
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Game g = new Game();
                g.name = columns[0];
                g.system=columns[1];
               // g.user = user;
                games.save(g);
            }
        }
    }



    @RequestMapping("/")

    public String home(HttpSession session,
                       Model model,
                       String name,
                       Integer system){

        String username = (String) session.getAttribute("username");

        if (username == null){
            return "login";
        }
        model.addAttribute("users", users.findAll());
        model.addAttribute("games", games.findAll());
        return "home";
    }

    @RequestMapping("add-game")
    public String addGame(HttpSession session,
                          String gameName,
                          String system
                          ) throws Exception {
        // HttpSession session = request.getSession();
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



    @RequestMapping("login")
    public String login(HttpSession session, String username, String password) throws Exception {
        session.setAttribute("username", username);

        User user = users.findOneByName(username);
        if(user==null){
            user = new User();
            user.name = username;
            user.password = PasswordHash.createHash(password);
            users.save(user);
        } else if (!PasswordHash.validatePassword(password, user.password)){
            throw new Exception("WRONG PASSWORD DUMMY!");
        }

        return "redirect:/";
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }



}
