package com.hjg.service;

import com.hjg.bean.Message;
import com.hjg.bean.User;
import org.springframework.data.domain.Page;

public interface MessageService {
    void saveMsg(Message message);
    Message getLastMessageForUser(User user);
    Page<Message> getMessagesInPageForUser(User user, int pageNum, int pageSize);
}
