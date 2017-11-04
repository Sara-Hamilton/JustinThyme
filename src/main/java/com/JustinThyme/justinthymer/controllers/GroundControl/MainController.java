package com.JustinThyme.justinthymer.controllers.GroundControl;


import com.JustinThyme.justinthymer.controllers.TwilioReminder.TwillTask;
import com.JustinThyme.justinthymer.models.converters.HashPass;

import com.JustinThyme.justinthymer.models.data.PacketDao;
import com.JustinThyme.justinthymer.models.data.SeedDao;
import com.JustinThyme.justinthymer.models.data.SeedInPacketDao;
import com.JustinThyme.justinthymer.models.data.UserDao;
import com.JustinThyme.justinthymer.models.converters.SeedToPacketSeed;
import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.Seed;
import com.JustinThyme.justinthymer.models.forms.SeedInPacket;
import com.JustinThyme.justinthymer.models.forms.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;


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
//     private HttpSession httpSession;

    private SeedInPacketDao seedInPacketDao;


    @RequestMapping(value = "")
    public String splash(Model model) {

        model.addAttribute("title", "Welcome to JustinThyme");
        return "splash";

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Log on in!");
        return "/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpServletResponse response, HttpServletRequest request) {

        //model.addAttribute("users", userDao.findAll());
        Iterable<User> users = userDao.findAll();

        for (User user : users) {

            String salt = user.getSalt();
            String enteredPassword = HashPass.generateHash(salt + password);

            if (user.getUsername().equals(username) && user.getPassword().equals(enteredPassword)) {

                model.addAttribute("user", user);
                // add user to session
                request.getSession().setAttribute("user", user);

                // create and set cookie for username
                Cookie usernameCookie = new Cookie("username", username);
                usernameCookie.setMaxAge(60 * 60);
                response.addCookie(usernameCookie);

                // create and set cookie for sessionId
                long sessionID = (long) (Math.random() * 1000000);
                String sessionId = String.valueOf(sessionID);
                Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
                sessionIdCookie.setMaxAge(60 * 60);
                response.addCookie(sessionIdCookie);

                // save data to database
                user.setSessionId(sessionId);
                userDao.save(user);

//                 //TODO set sessionID and cookie to something other than username for security
//                 httpSession.setAttribute("user_id", user.getId());

                // gets the packet associated with that user for display
                Packet userPacket = packetDao.findByUserId(user.getId());

                // redirects user to seed-edit page if they have no seeds in their packet
                if (userPacket == null) {
                    model.addAttribute("seeds", seedDao.findByArea(user.getArea()));
                    return "/seed-edit";
                }

                //makes a list of seeds not picked for display from list of seedsInPacket
                List<Seed> seedsToRemove = new ArrayList<>();
                for (SeedInPacket seedInPacket : userPacket.getSeeds()) {
                    String name = seedInPacket.getName();
                    List<Seed> aSeed = seedDao.findByName(name);

                    //returns a list of all the seeds with that name, most efficient?

                    seedsToRemove.addAll(aSeed);
                }


                List<Seed> seedsLeft = seedDao.findByArea(user.getArea());
                seedsLeft.removeAll(seedsToRemove);

                //seedsLeft will be Seed objects and seeds will be SeedInPacket

                model.addAttribute("user", user);
                model.addAttribute("title", "Testing seed removal");
                model.addAttribute("seeds", userPacket.getSeeds());
                model.addAttribute("seedsLeft", seedsLeft);

                return "/welcome-user";
            } else {
                model.addAttribute("title", "No user by that name or incorrect password!");
            }
        }
        return "/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {
        model.addAttribute("title", "Click here to Logout.");
        return "/logout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpServletResponse response, HttpServletRequest request) {
        model.addAttribute("title", "See ya next Thyme!");

        // Remove sessionId from database
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                String sessionId = cookie.getValue();

                Iterable<User> users = userDao.findAll();
                for (User user : users) {
                    if (user.getSessionId().equals(sessionId)) {
                        user.setSessionId("");
                        userDao.save(user);
                    }
                }
            }
        }

        //Remove cookies
        Cookie userCookie = new Cookie("username", "");
        Cookie sessionIdCookie = new Cookie("sessionId", "");
        userCookie.setMaxAge(0);
        sessionIdCookie.setMaxAge(0);
        response.addCookie(userCookie);
        response.addCookie(sessionIdCookie);
        request.getSession().removeAttribute("user");

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
    public String add(@ModelAttribute @Valid User newUser, Errors errors, Model model, String password,
                      String verifyPassword, HttpServletResponse response, HttpServletRequest request) {

        String username = newUser.username;

        String salt = HashPass.saltShaker();
        newUser.setSalt(salt);

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
            System.out.println("++" + password);
            System.out.println("--" + verifyPassword);
            if (password != "" && !password.equals(verifyPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }
            if (errors.hasErrors())
                System.out.println(errors);
            return "/signup";
        } else {
            // create and set cookie for username
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(60 * 60);
            response.addCookie(usernameCookie);

            // create and set cookie for sessionId
            long sessionID = (long) (Math.random() * 1000000);
            String sessionId = String.valueOf(sessionID);
            Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
            sessionIdCookie.setMaxAge(60 * 60);
            response.addCookie(sessionIdCookie);

            //hashes password before saving to User

            newUser.setPassword(HashPass.generateHash(salt + password));
            //byte[] salt = HashPass.saltShaker();
            //String saltyHash = HashPass.saltHash(password);
            //newUser.setSalt(salt);
            //newUser.setPassword(saltyHash);
            userDao.save(newUser);


            newUser.setSessionId(sessionId);
            userDao.save(newUser);

            model.addAttribute("user", newUser);
            Seed.Area area = newUser.getArea();
            List<Seed> seeds = new ArrayList<>();
            seeds = seedDao.findByArea(area);


//             //use http session to create session object with name user_id
//             httpSession.setAttribute("user_id", newUser.getId());


            request.getSession().setAttribute("user", newUser);

            model.addAttribute("seeds", seeds);
            return "/seed-edit";
        }
    }

    @RequestMapping(value = "/seed-edit", method = RequestMethod.GET)
    public String showSeeds(Model model, User newUser, HttpServletRequest request) {

        Seed.Area area = newUser.getArea();
        model.addAttribute(new Packet());
        model.addAttribute("seeds", seedDao.findByArea(area));
        model.addAttribute("user", newUser);
        return "/seed-edit";
    }


    @RequestMapping(value = "/seed-edit", method = RequestMethod.POST)

    public String seedListing(HttpSession session, Model model, @RequestParam (required = false)int[] seedIds,

                              Integer userId) {

        if(seedIds == null){
            User newUser = (User) session.getAttribute("user");
            Seed.Area area = newUser.getArea();
            model.addAttribute(new Packet());
            model.addAttribute("seeds", seedDao.findByArea(area));
            model.addAttribute("user", newUser);
            return "/seed-edit";
        }
        Packet newPacket = new Packet();
        User currentUser = userDao.findOne(userId);
        if (currentUser.getPacket() != null){
            newPacket = currentUser.getPacket();
        }
        newPacket.setUser(currentUser);
        packetDao.save(newPacket);
        //2 lists, 1 needed for packet and another to remove Seeds from display list
        //because you cannot remove seedsInPacket from seedDao
        List<SeedInPacket> seedsToPlant = new ArrayList<>();
        List<Seed> pickedSeedsAsSeeds = new ArrayList<>();

        //goes through list of chosen seeds and adds them to user's packet and sets reminder
        if (seedIds == null){
            model.addAttribute(new Packet());
            model.addAttribute("seeds", seedDao.findByArea(currentUser.getArea()));
            model.addAttribute("user", currentUser);
            model.addAttribute("title", "pick at least ONE seed!");
            return "/seed-edit";
        } else {
            for (int seedId : seedIds) {
                Seed seedPicked = seedDao.findOne(seedId);
                pickedSeedsAsSeeds.add(seedPicked);

            //note conversion using factory
                SeedInPacket seedToPlant = SeedToPacketSeed.fromSeedToPacket(seedPicked, newPacket);
                seedToPlant.setReminder(seedToPlant);
                seedsToPlant.add(seedToPlant);
                seedInPacketDao.save(seedToPlant);
            }
        }


        //after all seeds have been converted and reminder turned on, set to packet
        newPacket.setSeeds(seedsToPlant);


        //below needed for TwilioTask
        String number = currentUser.getPhoneNumber();
        Timer timer = new Timer(true); //daemon set to true

        //needed for display note see above @108
        Seed.Area area = currentUser.getArea();
        List<Seed> notChosenSeeds = seedDao.findByArea(area);
        notChosenSeeds.removeAll(pickedSeedsAsSeeds);


        //loops through the user's seeds and starts the update for each
        for (SeedInPacket seed : newPacket.getSeeds()) {

            if (seed.getReminder() == true) {
                String message = "It's time to plant " + seed.name;
                Date date = seed.getPlantDate();
                timer.schedule(new TwillTask.TwillReminder(message, number), date);
            }
        }


        model.addAttribute("user", currentUser);
        model.addAttribute("seeds", newPacket.getSeeds());
        model.addAttribute("seedsLeft", notChosenSeeds);

        return "/welcome-user";


    }


    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public String editProfilePreferences(Model model, HttpServletRequest request) {
        // display form with relevant options
        // change your area?
        // change your cell phone
        // change your password

       User aUser = (User) request.getSession().getAttribute("user");

        if (aUser != null) {
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
    public String saveChangesToProfilePreferences(@ModelAttribute @Valid User user, Errors errors,
                                                  Model model, HttpServletRequest request) {

        //process form, capture all user input into fields
        //make operative changes upon user's information in the database
        //update changes to user in database, save AND commit to it
        //return the same page with the update information displayed

        User aUser = (User) request.getSession().getAttribute("user");
        Boolean AreaChanged = false;
        if ((aUser.getArea() != user.getArea())){
            AreaChanged = true;
        }

        if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("areas", Seed.Area.values());
            model.addAttribute("title", "Editing Preferences for " + aUser.username);
            return "/edit-profile";
        } else {
            //SAVE CHANGED INFO

            //take user form session, and use validated fields to take new values
            if (!(aUser.getPhoneNumber().equals(user.getPhoneNumber()))) {
                model.addAttribute("phoneNumberChangedMessage", "Phone number has been changed.");
            }
            aUser.setPhoneNumber(user.getPhoneNumber());
            // empty seed packet if user changes area
            if (AreaChanged) {
                Packet aPacket = packetDao.findByUserId(aUser.getId());
                if (aPacket != null) {
                    List<SeedInPacket> seedsToRemove = aPacket.getSeeds();
                    for (SeedInPacket seed : seedsToRemove) {
                        seedInPacketDao.delete((SeedInPacket) seed);
                    }
                }



                //must delete packet to avoid multiples with same user_id => crash table
//                packetDao.delete(aPacket);
//                model.addAttribute("title", "New area!");
//                model.addAttribute("user", aUser);
//                model.addAttribute("seeds", seedDao.findByArea(aUser.getArea()));
//
//                return "/seed-edit";

                //model.addAttribute("areaChangedMessage", "Area has been changed.");


            }

            //must delete packet to avoid multiples with same user_id => crash table
            //packetDao.delete(aPacket);
            //model.addAttribute("title", "New area!");
            //model.addAttribute("user", user);
            //model.addAttribute("seeds", seedDao.findByArea(user.getArea()));

            //return "/seed-edit";

            //model.addAttribute("areaChangedMessage", "Area has been changed.");

        }

        aUser.setArea(user.getArea());
        //aUser.setPassword(user.getPassword());

        userDao.save(aUser);
        request.getSession().setAttribute("user", aUser);

        if (AreaChanged) {
            model.addAttribute("title", "New area!");
            model.addAttribute("user", aUser);
            model.addAttribute("seeds", seedDao.findByArea(aUser.getArea()));

            return "/seed-edit";
        } else {

            model.addAttribute("user", aUser);
            model.addAttribute("areas", Seed.Area.values());
            model.addAttribute("title", "Editing Preferences for " + aUser.username);
            return "/edit-profile";

        }
    }



    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public String changePassword(Model model, HttpServletRequest request){
        User aUser = (User) request.getSession().getAttribute("user");

        if (aUser != null) {
            model.addAttribute("user", aUser);
            model.addAttribute("title", "Change Password for " + aUser.username);
            return "/change-password";
        } else {
            //if no current user, redirect to splash page
            //nothing to edit if user is not logged in
            model.addAttribute("title", "Welcome");
            return "splash";
        }
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String changePassword(@ModelAttribute @Valid User user, Errors errors, Model model,String password, String newPassword,
                                       String verifyNewPassword, HttpServletRequest request, HttpServletResponse response){

        User aUser = (User) request.getSession().getAttribute("user");


        String salt = aUser.getSalt();
        //hashes input password and checks against stored password

        String checkPass = HashPass.generateHash(salt + password);
        String realPass = aUser.getPassword();

        if (!checkPass.equals(realPass)) {
            model.addAttribute("title", "Try again");
            model.addAttribute("passwordErrorMessage", "Incorrect password");
            //model.addAttribute(user);
            return "/change-password";
        }
        else if (!newPassword.equals(verifyNewPassword) || newPassword.length() < 6 || newPassword.equals(aUser.getPassword())) {
            model.addAttribute("title", "Try again");
            //model.addAttribute(user);
            if (newPassword.length() < 6) {
                model.addAttribute("newPasswordErrorMessage", "Passwords must be at least 6 characters long.");
            }
            if (newPassword.equals(aUser.getPassword())) {
                model.addAttribute("newPasswordErrorMessage2", "Use a different password.");
            }
            if (newPassword != "" && !newPassword.equals(verifyNewPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }

            return "/change-password";
        }


        //note keeps original salt
        String newHash = HashPass.generateHash(salt + newPassword);
        aUser.setPassword(newHash);
        userDao.save(aUser);

        //Remove cookies
        Cookie userCookie = new Cookie("username", "");
        Cookie sessionIdCookie = new Cookie("sessionId", "");
        userCookie.setMaxAge(0);
        sessionIdCookie.setMaxAge(0);
        response.addCookie(userCookie);
        response.addCookie(sessionIdCookie);
        request.getSession().removeAttribute("user");


        model.addAttribute("title", "Login with New Password");
        return "/splash";
    }


    @RequestMapping(value ="/welcome-user", method = RequestMethod.GET)
    public String dashboard (Model model, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");

//        if user is not logged in, sent back to splash page
        if(user == null){
            model.addAttribute("title", "Welcome to JustinThyme");
            return "/splash";
        } else {
            Packet aPacket = packetDao.findByUserId(user.getId());
            // redirects user to seed-edit page if they have no seeds in their packet
            if (aPacket == null) {
                model.addAttribute("user", user);
                model.addAttribute("seeds", seedDao.findByArea(user.getArea()));
                return "/seed-edit";
            }
            List<Seed> seedsToRemove = new ArrayList<>();
            for (SeedInPacket seedInPacket : aPacket.getSeeds()) {
                String name = seedInPacket.getName();
                List<Seed> aSeed = seedDao.findByName(name);
                seedsToRemove.addAll(aSeed);
            }

            List<Seed> notChosenSeeds = seedDao.findByArea(user.getArea());
            notChosenSeeds.removeAll(seedsToRemove);

            model.addAttribute("user", user);
            model.addAttribute("seeds", aPacket.getSeeds());
            model.addAttribute("seedsLeft", notChosenSeeds);
        }
        return "/welcome-user";
    }

    @RequestMapping (value ="/welcome-user", method = RequestMethod.POST)
    public String dashboardAdd (Model model , @RequestParam(required = false)int[] seedToRemoveIds,
                                @RequestParam(required = false)int[] ON,
                                @RequestParam(required = false)int[] OFF,
                                @RequestParam(required = false)int[] seedIds,
                                @RequestParam Integer userId){



        Packet aPacket = packetDao.findByUserId(userId);

        List<SeedInPacket> seedsToPlant = new ArrayList<>();
        List<Seed> pickedSeedsAsSeeds = new ArrayList<>();



        //changes reminder ON Off if either button pushed
        if (ON != null) {
            for (int id : ON) {
                SeedInPacket seedToTurnOff = seedInPacketDao.findById(id);
                seedToTurnOff.removeReminder(seedToTurnOff);
            }
        }
        if (OFF != null) {
            for (int id : OFF) {
                SeedInPacket seedToTurnOn = seedInPacketDao.findById(id);
                seedToTurnOn.setReminder(seedToTurnOn);
            }
        }

        //remove seeds from packet if they are selected

            if (seedToRemoveIds != null) {
                for (int id : seedToRemoveIds) {
                    SeedInPacket seedToGo = seedInPacketDao.findById(id);
                    seedInPacketDao.delete(seedToGo);
                }
            }


        //add selected seed to seedInPacketDao
        if (seedIds != null) {
            for (int seedId : seedIds) {
                Seed seedPicked = seedDao.findOne(seedId);
                pickedSeedsAsSeeds.add(seedPicked);

                SeedInPacket seedToPlant = SeedToPacketSeed.fromSeedToPacket(seedPicked, aPacket);
                seedToPlant.setReminder(seedToPlant);
                seedsToPlant.add(seedToPlant);
                seedInPacketDao.save(seedToPlant);
            }
        }

        User user = userDao.findOne(userId);

        //get seedname from user's seedInPacket
        List<Seed> seedsToRemove = new ArrayList<>();
        for (SeedInPacket seedInPacket : aPacket.getSeeds()) {
            String name = seedInPacket.getName();
            List<Seed> aSeed = seedDao.findByName(name);
            seedsToRemove.addAll(aSeed);
        }

        //TODO put if conditional to removeReminder form seedInPacket from radio button in form

        List<Seed> seedsLeft = seedDao.findByArea(user.getArea());
        seedsLeft.removeAll(seedsToRemove);

        model.addAttribute("user", user);
        model.addAttribute("seeds", aPacket.getSeeds());
        model.addAttribute("seedsLeft",seedsLeft );

        return "/welcome-user";
    }




    @RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
    public String displayUserToRemove(Model model) {

        model.addAttribute("title", "Sayonara!");
        return "/unsubscribe";
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public String processUserRemoval(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:login";
        }

        else {

            //retrieve and iterate over users packet to remove seeds before deleting user
            List<SeedInPacket> seeds = seedInPacketDao.findAll();
            Packet moldyPacket = packetDao.findByUserId(user.getId());
                for (SeedInPacket seed : seeds) {
                    if (seed.getPacket() == moldyPacket) {
                        seedInPacketDao.delete(seed);
                    }
                }
                //this qualifier avoids the error if unsubscribe twice in a row
                if (moldyPacket != null) {
                packetDao.delete(moldyPacket);}
                // "logs out" user before deletion
                request.getSession().removeAttribute("user");
                userDao.delete(user);
                model.addAttribute("title", "Deleted!");
                return "/well-wishes";

            }
        }


    }





