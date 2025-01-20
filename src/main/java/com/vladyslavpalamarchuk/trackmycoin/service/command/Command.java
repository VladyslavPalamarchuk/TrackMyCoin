package com.vladyslavpalamarchuk.trackmycoin.service.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Command {
  START("/start"),
  INFO("/info"),
  HELP("/help"),
  GET_MONITOR("/get"),
  ADD_MONITOR("/add"),
  REMOVE_MONITOR("/remove"),
  NON_COMMAND("");

  private final String command;
}
