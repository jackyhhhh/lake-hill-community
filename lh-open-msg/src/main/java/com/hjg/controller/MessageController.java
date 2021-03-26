package com.hjg.controller;

import com.hjg.bean.Message;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import com.hjg.service.MessageService;
import com.hjg.util.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/msg")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/saveMsg")
    public Response saveMsgHandler(@RequestBody Message message){
        User user = ThreadContext.currentUser();
        message.setUsername(user.getUsername());
        message.setNickname(user.getNickname());
        messageService.saveMsg(message);
        Response response = Response.success(messageService.getLastMessageForUser(ThreadContext.currentUser()));
        response.setMsg("消息发送成功!");
        return response;
    }

    @GetMapping("/describeLastMsg")
    public Response describeLastMsgHandler(){
        User user = ThreadContext.currentUser();
        return Response.success(messageService.getLastMessageForUser(user));
    }

    @GetMapping("/describeMsgData")
    public Response describeMsgDataHandler(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize){
        User user = ThreadContext.currentUser();
        return Response.success(messageService.getMessagesInPageForUser(user, pageNum, pageSize));
    }
}
