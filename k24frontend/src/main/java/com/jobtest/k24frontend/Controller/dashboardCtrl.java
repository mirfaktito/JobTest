package com.jobtest.k24frontend.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class dashboardCtrl {

    @GetMapping("/")
    public String awal(HttpSession session) {
        String tujuan ="";
        // Cek session 
        Object userlogin = session.getAttribute("userlogin");
        // System.out.println(userlogin);
        if (userlogin != null) {
             tujuan = "/dashboard/admin";
        }else{
             tujuan = "/dashboard/login";
        }
        return tujuan;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        String tujuan ="";
        // Cek session 
        Object userlogin = session.getAttribute("userlogin");
        // System.out.println(userlogin);
        if (userlogin != null) {
             tujuan = "/dashboard/admin";
        }else{
             tujuan = "/dashboard/login";
        }
        return tujuan;
    }

    @GetMapping("/setsessionadmin/{user}")
    public String setSessionadmin(HttpServletRequest request,
            @PathVariable String user, Model model) {
        request.getSession().setAttribute("userlogin", user);
        model.addAttribute("hUserName", "Halo," + user.toString() + "!");
        return "/dashboard/admin";
    }

    @GetMapping("/setsessionmember/{user}")
    public String setSessionmember(HttpServletRequest request,
            @PathVariable String user, Model model) {
        request.getSession().setAttribute("userlogin", user);
        model.addAttribute("hUserName", "Halo," + user.toString() + "!");
        return "/dashboard/member";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate(); // kill session
        return "/dashboard/login";
    }

    @GetMapping("/register")
    public String daftar() {
        return "dashboard/register";
    }
}
