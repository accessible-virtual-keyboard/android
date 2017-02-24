package no.ntnu.stud.avikeyb.backend;

import android.view.KeyEvent;

/**
 * The various symbols that can be selected and typed in the keyboard
 */
public enum Symbol {

    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    G("g"),
    H("h"),
    I("i"),
    J("j"),
    K("k"),
    L("l"),
    M("m"),
    N("n"),
    O("o"),
    P("p"),
    Q("q"),
    R("r"),
    S("s"),
    T("t"),
    U("u"),
    V("v"),
    W("w"),
    X("x"),
    Y("y"),
    Z("z"),

    NUM_0("0"),
    NUM_1("1"),
    NUM_2("2"),
    NUM_3("3"),
    NUM_4("4"),
    NUM_5("5"),
    NUM_6("6"),
    NUM_7("7"),
    NUM_8("8"),
    NUM_9("9"),


    QUESTION_MARK("?"),
    EXCLAMATION_MARK("!"),
    PERIOD("."),
    COMMA(","),
    SPACE(" "),
    SPACE_UNICODE_SYMBOL("\u005F"),

    NULL(null),

    BACKSPACE("\u232B"), //http://www.fileformat.info/info/unicode/char/2190/index.htm
    SEND("\u2332"),

    SWITCH("Switch"),

    // Testing for menu options.
    PROFILE("Profile"),
    PROFILE_UNICODE_SYMBOL("\uD83D\uDE42"), //Draft

    SETTING("Settings"),
    SETTING_UNICODE_SYMBOL("\u2699"), //Draft

    DICTIONARY("Dictionary"),
    DICTIONARY_UNICODE_SYMBOL("\uD83D\uDCD6"), //Draft

    OPTION1("Option 1"),
    OPTION2("Option 2"),
    OPTION3("Option 3");

    private String content;


    Symbol(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
