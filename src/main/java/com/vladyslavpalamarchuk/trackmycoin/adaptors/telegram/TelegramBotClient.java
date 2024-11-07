package com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramBotClient {

  private final TelegramClient telegramClient;
}
