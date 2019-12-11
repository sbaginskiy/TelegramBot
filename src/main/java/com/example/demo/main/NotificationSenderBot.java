package com.example.demo.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class NotificationSenderBot extends TelegramLongPollingBot {


    private final BotService botService;
    private final String DEFOLT_MESSAGE = "Hello, I'm the XM^online Notification Bot! From now on, " +
            "I will send you notifications right from the XM^online System. ";

    public NotificationSenderBot(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        initChanal(message);
    }

    @Override
    public String getBotUsername() {
        return "name";
    }

    @Override
    public String getBotToken() {
        return "token";
    }

    public void initChanal(Message message) {
        SendMessage s = new SendMessage();
        s.setChatId(message.getChatId());
        boolean newUser = botService.isNewUser(message.getChatId());
        if (newUser) {
            botService.createXmEntityChatId(message);
        }
        s.setText(DEFOLT_MESSAGE);
        try {
            sendApiMethod(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(Long chatId, String notification) throws TelegramApiException {
        SendMessage s = new SendMessage();
        s.setChatId(chatId);
        s.setText(notification);
        s.setParseMode("markdown");
        sendApiMethod(s);
    }
}
