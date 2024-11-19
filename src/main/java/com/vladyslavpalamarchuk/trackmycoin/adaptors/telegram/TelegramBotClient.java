package com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Getter
public class TelegramBotClient {

  private final TelegramClient telegramClient;
}
