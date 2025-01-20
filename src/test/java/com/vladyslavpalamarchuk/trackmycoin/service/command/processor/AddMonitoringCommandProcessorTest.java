package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.BinanceApiClient;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;

import lombok.NonNull;
import org.hibernate.sql.ast.spi.ParameterMarkerStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
class AddMonitoringCommandProcessorTest {

  private static final Long CHAT_ID = 1L;
  private static final String INVALID_INPUT_MESSAGE =
      "Please enter two values: the coin ticker and its current price ⚠️";
  @InjectMocks private AddMonitoringCommandProcessor processor;

  @Mock private BinanceApiClient binanceApiClient;
  @Mock private TelegramBotClient telegramBotClient;
  @Mock private MonitoringRepository monitoringRepository;
  @Mock private UserRepository userRepository;

  @Test
  void process_whenInvalidInput_thenSendMessageWithInvalidTicker() {
    // given
    Update update = new Update();
    Message message = new Message();
    message.setText("Invalid");
    Chat chat = Chat.builder().id(CHAT_ID).type("type").build();
    message.setChat(chat);
    update.setMessage(message);

    // when
    processor.process(update);

    // then
    verify(telegramBotClient).sendMessage(CHAT_ID, INVALID_INPUT_MESSAGE);
    verifyNoMoreInteractions(telegramBotClient, binanceApiClient, monitoringRepository, userRepository);
  }
}
