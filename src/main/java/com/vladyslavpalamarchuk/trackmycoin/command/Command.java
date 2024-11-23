package com.vladyslavpalamarchuk.trackmycoin.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Command {
  START("/start"),
  INFO("/info"),
  HELP("/help"),
  NON_COMMAND("");

  private final String command;
}
