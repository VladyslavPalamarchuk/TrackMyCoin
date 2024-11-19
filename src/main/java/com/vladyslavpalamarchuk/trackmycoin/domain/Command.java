package com.vladyslavpalamarchuk.trackmycoin.domain;

import lombok.Getter;


@Getter
public enum Command {

    START("/start", "Welcome to TrackMyCoin 📈💰!\nThis bot helps you track cryptocurrency prices\nand will send you notifications when your target price \nis reached.\n\nFor more information about the bot, type /info."),
    INFO("/info", "TrackMyCoin — your crypto assistant! 📈💰\n\nEasily track cryptocurrency prices and get notified\nwhen your target price is reached.\nPerfect for traders and crypto enthusiasts!\n\nType /help for a list of all commands."),
    HELP("/help", "List of commands 👾\nInfo -> /info\nHelp -> /help"),
    NON_COMMAND("","non command /help ");

    private final String command;
    private final String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }
}
