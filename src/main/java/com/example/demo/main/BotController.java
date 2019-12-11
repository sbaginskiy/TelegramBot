package com.example.demo.main;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BotController {
    @Autowired

    private NotificationSenderBot notificationSenderBot;

    @PostMapping("/bot-send-notification")
    @SneakyThrows
    public void sendNotification(@RequestBody Map notificationInfo) {
        notificationSenderBot.sendNotification(Long.valueOf(((Integer)notificationInfo.get("chatId"))),
                 "*" + notificationInfo.get("notificationTextPartOne") + "* \n"
                         + notificationInfo.get("notificationTextPartTwo") + "\n"
                         + "Notification URL: https://cimdemo.stage.xm-online.com/application/RESOURCE"
        );
    }
}

