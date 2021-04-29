package com.hjg.controller;

import com.hjg.bean.Message;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import com.hjg.service.MessageService;
import com.hjg.util.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping()
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/saveMsg")
    public Response saveMsgHandler(@RequestBody Map<String, String> params){
        User user = ThreadContext.currentUser();
        Message msg = new Message();
        msg.setContent((params.get("content")));
        msg.setUsername(user.getUsername());
        msg.setNickname(user.getNickname());
        messageService.saveMsg(msg);
        Response response = Response.success(messageService.getLastMessageForUser(user));
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
