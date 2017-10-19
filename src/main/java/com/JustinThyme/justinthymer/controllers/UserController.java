//package com.JustinThyme.justinthymer.controllers;
//
//import com.JustinThyme.justinthymer.models.forms.User;
//import com.JustinThyme.justinthymer.models.data.SeedDao;
//import com.JustinThyme.justinthymer.models.data.UserDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//
//import javax.validation.Valid;
//
//
//@Controller
//@RequestMapping("user")
//public class UserController {
//
//    @Autowired
//    private UserDao userDao;
//
//    @Autowired
//    private SeedDao seedDao;
//
////    adding new user : sign-up
//    @RequestMapping(value="add" , method = RequestMethod.GET)
//    public String add(Model model, @ModelAttribute @Valid User user, String verifyPassword, Errors errors) {
//
//        model.addAttribute(new User());
//
//        if (errors.hasErrors()) {
//            return "user/add";
//        }
//        if (!user.getPassword().equals(verifyPassword)) {
//            return "user/add";
//        } else {
//            userDao.save(user);
//            return "redirect:";
//        }
//
//    }
//
////    setting up process : select Zone
//    @RequestMapping(value="zone" , method = RequestMethod.GET)
//    public String zone (Model model){
//            return "zone";
//        }
//
//
////  log-in
//    @RequestMapping(value="login" , method = RequestMethod.GET)
//    public String login(Model model, @ModelAttribute @Valid User user, String password){
//
//        if (user.getPassword()==password){
//            return "user/dashboard";
//        } else {
//            return "redirect:";
//        }
//    }
//}
//
