package config;

/**
 * A class with the following functionality:
 * A) Parse the type of language for the bot (messages, buttons, etc.)
 * B) Provide a common interface for the bot's usage with the single class
 * Used pattern: Facade
 */
public class Text {
    // TODO: Make a connection between user default telegram language and initial language

    // Chooser for the language
    private static Language language = Language.ENG;

    // Text for the particular language
    private static IText text = language.parseText();

    /**
     * Change a language according to the user's decision.
     * @param language type of language that the user chooses
     */
    public static void changeLanguage(Language language) {
        Text.language = language;
        Text.text = language.parseText();
    }

    // All text's methods

    // Bot Buttons' messages
    public static String BookRoomBtn() {
        return text.BookRoomBtn();
    }

    public static String CheckBookingsBtn() {
        return text.CheckBookingsBtn();
    }

    // Bot answers
    public static String BookRoomMsg_Answer() {
        return text.BookRoomMsg_Answer();
    }

    public static String CheckBookingsMsg_Answer() {
        return text.CheckBookingsMsg_Answer();
    }

    // Bot messages
    public static String NewbiesMsg() {
        return text.NewbiesMsg();
    }
}

/**
 * Enumerate the possible languages for the bot and the functionality
 * to parse text in the given language
 */
enum Language {
    RUS {
        @Override
        public IText parseText() {
            return new RusText();
        }
    },
    ENG {
        @Override
        public IText parseText() {
            return new EngText();
        }
    };

    /**
     * Receive the instance of text with the chosen user language
     * @return instance of the right text
     */
    public IText parseText() {
        return null;
    }
}