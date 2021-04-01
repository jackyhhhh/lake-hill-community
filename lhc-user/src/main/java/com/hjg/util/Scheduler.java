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
//        log.debug("定时任务(刷新用户状态)执行第 " + count + " 次!");
        List<User> all = userService.findAll();
//        log.debug("all.size():"+all.size());
        if (all.size() > 0) {
            int i = 0;
            for (User u : all) {
                i++;
//                log.debug("username-"+i+": "+u.getUsername());
//                log.debug("statusBefore-"+i+": "+u.getStatus());
                String s = redisTemplate.boundValueOps("uid_" + u.getUid()).get();
                if (s == null && u.getStatus() == User.STATUS_ON) {
                    u.setStatus(User.STATUS_OFF);
                    u.setOnlineTime(null);
                }else if ( s != null && u.getStatus() == User.STATUS_OFF){
                    u.setOnlineTime(new Date());
                    u.setStatus(User.STATUS_ON);
                }
//                log.debug("statusAfter-"+i+": "+u.getStatus());
                userService.save(u);
            }
//            log.debug("refreshing user status successfully !");
        }
        count++;
    }
}

