package com.example.demo.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@SpringBootApplication
@ComponentScan(basePackageClasses = BotController.class)
public class NotificationSenderBotApplication {
	static {
		ApiContextInitializer.init();
	}
	public static void main(String[] args) {
		SpringApplication.run(NotificationSenderBotApplication.class, args);
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			NotificationSenderBot bot = new NotificationSenderBot(new BotService());
			telegramBotsApi.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

	}

}
