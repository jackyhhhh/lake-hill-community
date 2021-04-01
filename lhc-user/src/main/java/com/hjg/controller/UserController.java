package com.hjg.controller;

import com.hjg.annotation.LoginFree;
import com.hjg.bean.User;
import com.hjg.bean.form.PasswordForm;
import com.hjg.bean.form.Response;
import com.hjg.service.UserService;
import com.hjg.util.ThreadContext;
import com.hjg.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @LoginFree
    @GetMapping("/checkUsername")
    public Response checkUsernameHandler(@RequestParam String username){
        User user = userService.findByUsername(username);
        if (user == null) {
            return Response.success();
        }
        return Response.fail("username has been occupied !");
    }

    @LoginFree
    @PostMapping("/reg")
    public Response regHandler(@RequestBody User form){
        String username = form.getUsername();
        String password = form.getPassword();
        if(username == null || "".equals(username.trim()) || password == null || "".equals(password.trim())){
            return Response.fail("username or password can not be empty !");
        }
        User user = userService.findByUsername(username);
        if (user != null) {
            return Response.fail("username has been occupied !");
        }
        userService.save(form);
        return Response.success();
    }

    @LoginFree
    @PostMapping("/login")
    public Response loginHandler(@RequestBody User form, HttpServletResponse response){
        String username = form.getUsername();
        String password = form.getPassword();
        if(username == null || "".equals(username.trim()) || password == null || "".equals(password.trim())){
            return Response.fail("username or password can not be empty !");
        }
        User user = userService.findByUsername(username);
        if (user == null || ! password.equals(user.getPassword())) {
            return Response.fail("invalid username or password !");
        }
        tokenService.signToken(user, response);
        String token = tokenService.createToken(user);
        tokenService.setTokenInResponse(token, response);
        user.setOnlineTime(new Date());
        user.setStatus(User.STATUS_ON);
        userService.save(user);
        return Response.success(user);
    }

    @GetMapping("/logout")
    public Response logoutHandler(HttpServletRequest request, HttpServletResponse response){
        User user = ThreadContext.currentUser();
        user.setStatus(User.STATUS_OFF);
        user.setOnlineTime(null);
        userService.save(user);
        redisTemplate.delete("uid_"+user.getUid());
        tokenService.invalidToken(tokenService.getTokenFromRequest(request), response);
        return Response.success();
    }

    @PostMapping("/modify")
    public Response modifyHandler(@RequestBody User form){
        String username = form.getUsername();
        String nickname = form.getNickname();
        User user = ThreadContext.currentUser();
        if (username != null && ! ("".equals(username.trim()))) {
            user.setUsername(username);
        }
        if (nickname != null && !("".equals(nickname.trim()))) {
            user.setNickname(nickname);
        }
        userService.save(user);
        return Response.success();
    }

    @PostMapping("/modifyPassword")
    public Response modifyPasswordHandler(@RequestBody PasswordForm form){
        String oldPassword = form.getOldPwd();
        String newPassword = form.getNewPwd();
        User user = ThreadContext.currentUser();
        if(! (oldPassword.equals(user.getPassword()))){
            return Response.fail("invalid old password !");
        }
        if( newPassword == null || "".equals(newPassword.trim())){
            return Response.fail("new password should not be null !");
        }
        user.setPassword(newPassword);
        userService.save(user);
        return Response.success();
    }

    @PostMapping("/removeUser")
    public Response deleteHandler(@RequestBody User form){
        User user = ThreadContext.currentUser();
        if (user == null) {
            return Response.fail("user not found !");
        }
        if(! user.getPassword().equals(form.getPassword())){
            return Response.fail("invalid password !");
        }
        userService.deleteById(user.getUid());
        return Response.success();
    }

    @GetMapping("/describe")
    public Response describeHandler(){
        User user = ThreadContext.currentUser();
        return Response.success(user);
    }

    @GetMapping("/online")
    public Response onlineHandler(){ return Response.success(userService.findByStatus(User.STATUS_ON));}

    @GetMapping("/offline")
    public Response offlineHandler(){
        return Response.success(userService.findByStatus(User.STATUS_OFF));
    }

    @GetMapping("/all")
    public Response listAllHandler(){
        return Response.success(userService.findAll());
    }

}
