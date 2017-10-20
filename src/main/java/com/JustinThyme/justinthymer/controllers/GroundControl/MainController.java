package com.JustinThyme.justinthymer.controllers.GroundControl;


import com.JustinThyme.justinthymer.controllers.TwilioReminder.TwillTask;
import com.JustinThyme.justinthymer.models.data.PacketDao;
import com.JustinThyme.justinthymer.models.data.SeedDao;
import com.JustinThyme.justinthymer.models.data.UserDao;
import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.Seed;
import com.JustinThyme.justinthymer.models.forms.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;


@Controller
@RequestMapping("JustinThyme")
public class MainController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SeedDao seedDao;

    @Autowired
    private PacketDao packetDao;


    @RequestMapping(value="")
    public String splash(Model model) {

        model.addAttribute("title", "Welcome to JustinThyme");
        return "splash";

    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Log on in!");
        //model.addAttribute(new User());
        return "/login";
    }
    
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpServletResponse response) {

        model.addAttribute("users", userDao.findAll());
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                model.addAttribute("user", user);
                //TODO add cookie = ${username}
                Cookie userCookie = new Cookie("username", user.getUsername());
                response.addCookie(userCookie);
                //TODO set loggedIn = 1;
                user.setLoggedIn(true);
                userDao.save(user);
                //TODO set sessionID
                return "/welcome-user";
               }
                else {
                model.addAttribute("title", "No user by that name or incorrect password!");
            }
        }
        return "/login";
    }



    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "New User!");
        model.addAttribute(new User());
        model.addAttribute("areas", Seed.Area.values());
        return "/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid User newUser, Errors errors, Model model,
                      String verifyPassword) {

        String username = newUser.username;
        String password = newUser.getPassword();
        //newUser.checkPassword();

        if (errors.hasErrors() || (!password.equals(verifyPassword))) {
            model.addAttribute("title", "Try again");
            model.addAttribute(newUser);
            model.addAttribute("areas", Seed.Area.values());
            if(password != "" && !password.equals(verifyPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }
            return "/signup";
        } else {
            userDao.save(newUser);
            model.addAttribute("user", newUser);
            Seed.Area area = newUser.getArea();
            //Packet seeds = new Packet(newUser.getId(), seedDao.findByArea(area));
            List<Seed> seeds = new ArrayList<>();
            seeds = seedDao.findByArea(area);

            model.addAttribute("seeds", seeds);
            //return "/welcome-user";
            //return "redirect:seed-edit";
            return "/seed-edit";
        }
    }

    @RequestMapping(value = "/seed-edit", method = RequestMethod.GET)
    public String showSeeds(Model model, User newUser) {
        Seed.Area area = newUser.getArea();
        System.out.println("**********************" + newUser.getId());
        model.addAttribute(new Packet());
        model.addAttribute("seeds", seedDao.findByArea(area));
        model.addAttribute("user", newUser);
        return "/seed-edit";
    }



    @RequestMapping(value = "/seed-edit", method = RequestMethod.POST)
    public String seedListing(Model model, User newUser, @ModelAttribute Packet aPacket, @RequestParam int[] seedIds,
                              Integer userId) {

        //goes through list of chosen seeds and adds them to user's packet
        for (int seedId : seedIds) {
            Seed seedToPlant = seedDao.findOne(seedId);
            aPacket.addSeed(seedToPlant);
            aPacket.setReminder(seedToPlant);//note turns reminder on for all seeds in this sprint
        }


        aPacket.setUser_id(userId);
        packetDao.save(aPacket);
        User currentUser = userDao.findOne(userId);


        String number = currentUser.getPhoneNumber(); ;//note will only work with my number for now
        Timer timer = new Timer(true);

        for (Seed seed : aPacket.getSeeds()) {
            String message = "It's time to plant " + seed.name;
            Date date = seed.getPlantDate();
            System.out.println("+++++++++++" + seed.plantDate);
            System.out.println(message);
            System.out.println("=====================" + number);
            timer.schedule(new TwillTask.TwillReminder(message, number), date);
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("packet", aPacket);
        model.addAttribute("seeds",aPacket.getSeeds());

        return "/welcome-user";


    }





}
