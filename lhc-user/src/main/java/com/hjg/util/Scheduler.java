package com.hjg.util;


import com.hjg.bean.User;
import com.hjg.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Scheduler {

    private int count = 1;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedDelay = 60 * 1000)
    public void userStatusHeartBeat(){
        List<User> all = userService.findAll();
        if (all.size() > 0) {
            int i = 0;
            for (User u : all) {
                i++;
                String s = redisTemplate.boundValueOps("uid_" + u.getUid()).get();
                if (s == null && u.getStatus() == User.STATUS_ON) {
                    log.debug("定时任务(刷新用户状态)执行第 " + count + " 次! all.size():"+all.size());
                    log.debug("(" + i + ")" + u.getUsername() + " statusBefore: "+u.getStatus());
                    u.setStatus(User.STATUS_OFF);
                    u.setOnlineTime(null);
                    log.debug("statusAfter: "+u.getStatus() + ", " + ThreadContext.requestId());
                }else if ( s != null && u.getStatus() == User.STATUS_OFF){
                    log.debug("定时任务(刷新用户状态)执行第 " + count + " 次! all.size():"+all.size());
                    log.debug("(" + i + ")" + u.getUsername() + " statusBefore: "+u.getStatus());
                    u.setOnlineTime(new Date());
                    u.setStatus(User.STATUS_ON);
                    log.debug("statusAfter: "+u.getStatus());
                }
                userService.save(u);
            }
        }
        count++;
    }
}

