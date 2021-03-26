package com.hjg.service.impl;

import com.hjg.bean.Message;
import com.hjg.bean.User;
import com.hjg.repository.MessageRepository;
import com.hjg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void saveMsg(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Message getLastMessageForUser(User user) {
        if(user != null){
            Date onlineTime = user.getOnlineTime();
            Pageable pageable = PageRequest.of(0, 1, Sort.by("sendTime").descending());
            Page<Message> messages = messageRepository.getBySendTimeGreaterThan(onlineTime, pageable);
            if(messages != null && messages.getContent().size() > 0){
                return messages.getContent().get(0);
            }
        }
        return null;
    }

    @Override
    public Page<Message> getMessagesInPageForUser(User user, int pageNum, int pageSize) {
        Page<Message> messages = null;
        if(user != null){
            Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("sendTime").descending());
            messages = messageRepository.getBySendTimeGreaterThan(user.getOnlineTime(), pageable);
        }
        return messages;
    }
}
