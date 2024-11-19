package com.vladyslavpalamarchuk.trackmycoin.service;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import java.util.Optional;

@Service
public class MessageHandlerService {

    @Autowired
    private TelegramBotClient telegramBotClient;

    private final List<Command> commands = List.of(Command.START , Command.INFO , Command.HELP);

    public void processMessage(Long chatId, String messageText) {
       Optional<Command> commandOptional = commands.stream()
                .filter(c ->  c.getCommand().equals(messageText))
                .findFirst();

       if (commandOptional.isPresent()) {
           sendWelcomeMessage(chatId, commandOptional.get().getDescription());
       }else {
           sendWelcomeMessage(chatId, Command.NON_COMMAND.getDescription());
       }
    }

    private void sendWelcomeMessage(Long chatId , String messageText) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            telegramBotClient.getTelegramClient().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
