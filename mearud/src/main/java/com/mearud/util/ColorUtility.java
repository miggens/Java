package com.mearud.util;

public class ColorUtility {

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_BLACK  = "\u001B[30m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    private static final String ANSI_BRIGHT_BLACK  = "\u001B[90m";
    private static final String ANSI_BRIGHT_RED    = "\u001B[91m";
    private static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    private static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    private static final String ANSI_BRIGHT_BLUE   = "\u001B[94m";
    private static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    private static final String ANSI_BRIGHT_CYAN   = "\u001B[96m";
    private static final String ANSI_BRIGHT_WHITE  = "\u001B[97m";

    private static final String ANSI_BG_BLACK  = "\u001B[40m";
    private static final String ANSI_BG_RED    = "\u001B[41m";
    private static final String ANSI_BG_GREEN  = "\u001B[42m";
    private static final String ANSI_BG_YELLOW = "\u001B[43m";
    private static final String ANSI_BG_BLUE   = "\u001B[44m";
    private static final String ANSI_BG_PURPLE = "\u001B[45m";
    private static final String ANSI_BG_CYAN   = "\u001B[46m";
    private static final String ANSI_BG_WHITE  = "\u001B[47m";

    private static final String ANSI_BRIGHT_BG_BLACK  = "\u001B[100m";
    private static final String ANSI_BRIGHT_BG_RED    = "\u001B[101m";
    private static final String ANSI_BRIGHT_BG_GREEN  = "\u001B[102m";
    private static final String ANSI_BRIGHT_BG_YELLOW = "\u001B[103m";
    private static final String ANSI_BRIGHT_BG_BLUE   = "\u001B[104m";
    private static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";
    private static final String ANSI_BRIGHT_BG_CYAN   = "\u001B[106m";
    private static final String ANSI_BRIGHT_BG_WHITE  = "\u001B[107m";

    public static String wrapError(String error) {
        return ANSI_RED+error+ANSI_RESET;
    }

    public static String wrapWarning(String warn) {
        return ANSI_YELLOW+warn+ANSI_RESET;
    }

    public static String wrapInfo1(String info) {
        return ANSI_BRIGHT_BLUE+info+ANSI_RESET;
    }

    public static String wrapInfo2(String info) {
        return ANSI_PURPLE+info+ANSI_RESET;
    }

    public static String wrapSuccess(String msg) {
        return ANSI_GREEN+msg+ANSI_RESET;
    }
}
