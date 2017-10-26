package com.JustinThyme.justinthymer.controllers.GroundControl;


import com.JustinThyme.justinthymer.controllers.TwilioReminder.TwillTask;
import com.JustinThyme.justinthymer.models.data.PacketDao;
import com.JustinThyme.justinthymer.models.data.SeedDao;
import com.JustinThyme.justinthymer.models.data.UserDao;
import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.Seed;
import com.JustinThyme.justinthymer.models.forms.User;
import com.JustinThyme.justinthymer.models.forms.UserData;
import com.oracle.jrockit.jfr.ValueDefinition;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Controller
@RequestMapping("JustinThyme")
@SessionAttributes("username")
public class MainController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SeedDao seedDao;

    @Autowired
    private PacketDao packetDao;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value="")
    public String splash(Model model) {

        model.addAttribute("title", "Welcome to JustinThyme");
        return "splash";

    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Log on in!");
        return "/login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpServletResponse response) {

        model.addAttribute("users", userDao.findAll());
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                model.addAttribute("user", user);
                //add cookie called username
                Cookie userCookie = new Cookie("username", user.getUsername());
                // set cookie to expire in 1 hour
                userCookie.setMaxAge(1 * 60 * 60);
                response.addCookie(userCookie);
                //set loggedIn == 1(true) in database
                user.setLoggedIn(true);
                userDao.save(user);
                model.addAttribute("seeds", seedDao.findByArea(user.getArea()));
                //TODO set sessionID and cookie to something other than username for security
                httpSession.setAttribute("user_id", user.getId());
                return "/welcome-user";
               }
                else {
                model.addAttribute("title", "No user by that name or incorrect password!");
            }
        }
        return "/login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(Model model) {
        model.addAttribute("title", "Click here to Logout.");
        return "/logout";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpServletResponse response) {
        model.addAttribute("title", "See ya next Thyme!");
        //Set loggedIn to false
        Iterable<User> users = userDao.findAll();
        for (User user: users) {
            if (!(user.isLoggedIn() == true)) {
                continue;
            }
            user.setLoggedIn(false);
            userDao.save(user);
        }
        //Remove cookie
        Cookie userCookie = new Cookie("username","");
        userCookie.setMaxAge(0);
        response.addCookie(userCookie);
        return "/see-ya";
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
                      String verifyPassword, HttpServletResponse response) {

        String username = newUser.username;
        String password = newUser.getPassword();


        // username must be unique
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                model.addAttribute("title", "Try again");
                model.addAttribute(newUser);
                model.addAttribute("areas", Seed.Area.values());
                model.addAttribute("userErrorMessage", "That username is taken.");
                return "/signup";
            }
        }

        if (errors.hasErrors() || (!password.equals(verifyPassword))) {
            model.addAttribute("title", "Try again");
            model.addAttribute(newUser);
            model.addAttribute("areas", Seed.Area.values());
            if(password != "" && !password.equals(verifyPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }
            return "/signup";
        } else {
            //add cookie called username
            Cookie userCookie = new Cookie("username", username);
            // set cookie to expire in 1 hour
            userCookie.setMaxAge(1 * 60 * 60);
            response.addCookie(userCookie);
            //save new user to database
            userDao.save(newUser);
            //set loggedIn == 1(true) in database
            newUser.setLoggedIn(true);
            model.addAttribute("user", newUser);
            Seed.Area area = newUser.getArea();
            List<Seed> seeds = new ArrayList<>();
            seeds = seedDao.findByArea(area);

            //use http session to create session object with name user_id
            httpSession.setAttribute("user_id", newUser.getId());

            model.addAttribute("seeds", seeds);
            return "/seed-edit";
        }
    }

    @RequestMapping(value = "/seed-edit", method = RequestMethod.GET)
    public String showSeeds(Model model, User newUser) {

        Seed.Area area = newUser.getArea();
        model.addAttribute(new Packet());
        model.addAttribute("seeds", seedDao.findByArea(area));
        model.addAttribute("user", newUser);
        return "/seed-edit";
    }

    @RequestMapping(value = "/seed-edit", method = RequestMethod.POST)
    public String seedListing(HttpSession session, Model model, User newUser, @ModelAttribute Packet aPacket, @RequestParam int[] seedIds,
                              Integer userId) {

        //goes through list of chosen seeds and adds them to user's packet
        for (int seedId : seedIds) {
            Seed seedToPlant = seedDao.findOne(seedId);
            aPacket.addSeed(seedToPlant);
            aPacket.setReminder(seedToPlant);//note turns reminder on for all seeds in this sprint
        }

        User currentUser = userDao.findOne(userId);
        aPacket.setUser(currentUser);
        packetDao.save(aPacket);



        String number = currentUser.getPhoneNumber();
        Timer timer = new Timer(true);
        Seed.Area area = currentUser.getArea();

        //loops through the user's seeds and set the update reminder for each
        for (Seed seed : aPacket.getSeeds()) {

            if (seed.getReminder() == true) {
                String message = "It's time to plant " + seed.name;
                Date date = seed.getPlantDate();
                timer.schedule(new TwillTask.TwillReminder(message, number), date);
            }
        }


        List<Seed> notChosenSeeds = seedDao.findByArea(area);
        notChosenSeeds.removeAll(aPacket.getSeeds());


        model.addAttribute("user", currentUser);
        model.addAttribute("packet", aPacket);
        model.addAttribute("seeds",aPacket.getSeeds());
        model.addAttribute("seedsLeft", notChosenSeeds);

        return "/welcome-user";


    }


    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public String editProfilePreferences(Model model) {
        // display form with relevant options
        // change your area?
        // change your cell phone
        // change your password

       Integer userId = (Integer) httpSession.getAttribute("user_id");
       User aUser = userDao.findById(userId);

        if (userId != 0) {
            model.addAttribute("user", aUser);
            model.addAttribute("areas", Seed.Area.values());
            model.addAttribute("title", "Editing Preferences for " + aUser.username);
            return "/edit-profile";
        } else {
            //if no current user, redirect to splash page
            //nothing to edit if user is not logged in
            model.addAttribute("title", "Welcome to JustinThyme");
            return "splash";
        }
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String saveChangesToProfilePreferences(@ModelAttribute @Valid User updatedUser, Errors errors, Model model) {

        //process form, capture all user input into fields
        //make operative changes upon user's information in the database
        //update changes to user in database, save AND commit to it
        //return the same page with the update information displayed

        Integer userId = (Integer) httpSession.getAttribute("user_id");
        User aUser = userDao.findById(userId);

        if (errors.hasErrors()) {
            model.addAttribute("user", aUser);
            model.addAttribute("areas", Seed.Area.values());
            model.addAttribute("title", "Editing Preferences for " + aUser.username);
            return "/edit-profile";
        } else {
            //SAVE CHANGED INFO

            //take user form session, and use validated fields to take new values
            aUser.setPhoneNumber(updatedUser.getPhoneNumber());
            aUser.setArea(updatedUser.getArea());
            aUser.setPassword(updatedUser.getPassword());

            userDao.save(aUser);

            model.addAttribute("user", aUser);
            model.addAttribute("areas", Seed.Area.values());
            model.addAttribute("title", "Editing Preferences for " + aUser.username);
            return "/edit-profile";
        }

    }

    @RequestMapping(value="/welcome-user-temp")
    public String tempHolder() {
        return "/welcome-user-temp";
    }

    @RequestMapping(value = "/unsubscribe")
    public String displayConstruction() {
        return "/well-wishes";
    }

//    @RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
//    public String displayUserToRemove(HttpSession session, Model model, @RequestParam int userId) {
//        //System.out.println(session.getAttributeNames());
//        //model.addAttribute("user", userDao.getAll());
//        model.addAttribute("title", "Sayonara!");
//        return "unsubscribe" + userId;
//    }
//
//    @RequestMapping(value = "/unsubscribe/<userId>", method = RequestMethod.POST)
//    public String processUserRemoval(@RequestParam int userId) {
//
//        User exUser = userDao.findById(userId);
//        //Packet moldyPacket = packetDao.findByUserId(userId);
//        //packetDao.delete(moldyPacket);
//        userDao.delete(exUser);
//
//        return "/well-wishes";
//    }



}
