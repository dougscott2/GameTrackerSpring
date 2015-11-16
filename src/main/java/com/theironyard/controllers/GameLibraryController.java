package com.theironyard.controllers;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import com.theironyard.entities.Game;
import com.theironyard.entities.User;
import com.theironyard.services.GameRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.util.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        User doug = users.findOneByName("doug");
        if(user == null){
            user = new User();
            user.name="Admin";
            user.password = PasswordHash.createHash("password");
            users.save(user);
        }
        if(doug == null){
            doug = new User();
            doug.name="doug";
            doug.password = PasswordHash.createHash("1234");
            users.save(doug);
        }

        if (games.count() == 0) {
            Scanner scanner = new Scanner(new File("games.csv"));
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Game g = new Game();
                int id = Integer.valueOf(columns[2]);
                g.user=users.findOne(id);
                g.title = columns[0];
                g.system = columns[1];
                games.save(g);
            }
        }
    }


    @RequestMapping("/")
    public String home(HttpSession session,
                       Model model,
                       String system,
                       String userGames,
                       @RequestParam(defaultValue = "0")   int page){
        PageRequest pr = new PageRequest(page, 5);
        Page p;

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("system", system);
        model.addAttribute("userGames", userGames);
        if(userGames!=null){
            //model.addAttribute("games", users.findOneByName(username).userGames);
            p = games.findAllByUser(pr, users.findOneByName(username));
            //users.findOneByName(username).userGames;
        } else if(system != null){
            //model.addAttribute("users", users.findOneByName(username));
           p = games.findAllBySystem(pr, system);
            // model.addAttribute("games", games.findAllBySystem(system));
        } else {
            // model.addAttribute("users", users.findOneByName(username));
            //model.addAttribute("games", games.findAll());
            p = games.findAll(pr);
        }


            model.addAttribute("nextPage", page + 1);
            model.addAttribute("system", system);
            model.addAttribute("games", p);
            model.addAttribute("showNext", p.hasNext());
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
        game.title = gameName;
        game.system = system;
        game.user=user;
        games.save(game);
       // Scanner scanner = new Scanner(new File("games.csv"));  tried writing to csv file


        return "redirect:/";
    }
    @RequestMapping("edit")
    public String edit(HttpSession session,
                       Model model,
                       int id) throws Exception {
        String username = (String) session.getAttribute("username");
        Game game = games.findOne(id);
        User user = users.findOneByName(username);
        model.addAttribute("games", game);
        if (game.user!=user){
            return "error";
           // throw new Exception("you can't edit someone else's entries!!");
        }
        return "edit-game";
    }

    @RequestMapping("edit-game")
    public String editGame(int id,
                           String edName,
                           String edSystem,
                           HttpSession session
                           ) throws Exception{

        String username = (String) session.getAttribute("username");

        if (username==null){
            throw new Exception("Not logged in!");
        }
        User user = users.findOneByName(username);

        Game game = games.findOne(id);

        game.title = edName;
        game.system = edSystem;
        game.user= user;
        games.save(game);
        return "redirect:/";
    }

    @RequestMapping("delete-game")
    public String deleteGame(HttpSession session, int id) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByName(username);
        Game game = games.findOne(id);
        if(game.user!=user){
            //throw new Exception ("You can't delete what you didn't make!");
            return "error";
        }
        games.delete(id);
        return "redirect:/";
    }







}
