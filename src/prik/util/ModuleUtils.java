package prik.util;

/**
 *
 * @author Professional
 */
public final class ModuleUtils {
    public static String[][] letters = {
            {
                    "    _   ",
                    "  ___ ",
                    "   ___ ",
                    "  ___  ",
                    "  ___ ",
                    "  ___ ",
                    "   ___ ",
                    "  _  _ ",
                    "  ___ ",
                    "     _ ",
                    "  _  __",
                    "  _    ",
                    "  __  __ ",
                    "  _  _ ",
                    "   ___  ",
                    "  ___ ",
                    "   ___  ",
                    "  ___ ",
                    "  ___ ",
                    "  _____ ",
                    "  _   _ ",
                    " __   __",
                    " __      __",
                    " __  __",
                    " __   __",
                    "  ____",
                    "  _ ",
                    "  ___ ",
                    "  ____",
                    "  _ _  ",
                    "  ___ ",
                    "   __ ",
                    "  ____ ",
                    "  ___ ",
                    "  ___ ",
                    "   __  ",
                    "    ",
                    "    ",
                    "  _ ",
                    "    __",
                    "   __",
                    " __  ",
                    "  ___ ",
                    " __   ",
                    "  _ "
            },
            {
                    "   /_\\  ",
                    " | _ )",
                    "  / __|",
                    " |   \\ ",
                    " | __|",
                    " | __|",
                    "  / __|",
                    " | || |",
                    " |_ _|",
                    "  _ | |",
                    " | |/ /",
                    " | |   ",
                    " |  \\/  |",
                    " | \\| |",
                    "  / _ \\ ",
                    " | _ \\",
                    "  / _ \\ ",
                    " | _ \\",
                    " / __|",
                    " |_   _|",
                    " | | | |",
                    " \\ \\ / /",
                    " \\ \\    / /",
                    " \\ \\/ /",
                    " \\ \\ / /",
                    " |_  /",
                    " / |",
                    " |_  )",
                    " |__ /",
                    " | | | ",
                    " | __|",
                    "  / / ",
                    " |__  |",
                    " ( _ )",
                    " / _ \\",
                    "  /  \\ ",
                    "    ",
                    "  _ ",
                    " | |",
                    "   / /",
                    "  / /",
                    " \\ \\ ",
                    " |__ \\",
                    " \\ \\  ",
                    " (_)"
            },
            {
                    "  / _ \\ ",
                    " | _ \\",
                    " | (__ ",
                    " | |) |",
                    " | _| ",
                    " | _| ",
                    " | (_ |",
                    " | __ |",
                    "  | | ",
                    " | || |",
                    " | ' < ",
                    " | |__ ",
                    " | |\\/| |",
                    " | .` |",
                    " | (_) |",
                    " |  _/",
                    " | (_) |",
                    " |   /",
                    " \\__ \\",
                    "   | |  ",
                    " | |_| |",
                    "  \\ V / ",
                    "  \\ \\/\\/ / ",
                    "  >  < ",
                    "  \\ V / ",
                    "  / / ",
                    " | |",
                    "  / / ",
                    "  |_ \\",
                    " |_  _|",
                    " |__ \\",
                    " / _ \\",
                    "   / / ",
                    " / _ \\",
                    " \\_, /",
                    " | () |",
                    "  _ ",
                    " ( )",
                    " |_|",
                    "  / / ",
                    " < < ",
                    "  > >",
                    "   /_/",
                    "  \\ \\ ",
                    "  _ "
            },
            {
                    " /_/ \\_\\",
                    " |___/",
                    "  \\___|",
                    " |___/ ",
                    " |___|",
                    " |_|  ",
                    "  \\___|",
                    " |_||_|",
                    " |___|",
                    "  \\__/ ",
                    " |_|\\_\\",
                    " |____|",
                    " |_|  |_|",
                    " |_|\\_|",
                    "  \\___/ ",
                    " |_|  ",
                    "  \\__\\_\\",
                    " |_|_\\",
                    " |___/",
                    "   |_|  ",
                    "  \\___/ ",
                    "   \\_/  ",
                    "   \\_/\\_/  ",
                    " /_/\\_\\",
                    "   |_|  ",
                    " /___|",
                    " |_|",
                    " /___|",
                    " |___/",
                    "   |_| ",
                    " |___/",
                    " \\___/",
                    "  /_/  ",
                    " \\___/",
                    "  /_/ ",
                    "  \\__/ ",
                    " (_)",
                    " |/ ",
                    " (_)",
                    " /_/  ",
                    "  \\_\\",
                    " /_/ ",
                    "  (_) ",
                    "   \\_\\",
                    " (_)"
            }
    };
    
    public static String strip(String str, String marginPrefix) {
        final int nonBlankIndex = firstNonBlankIndex(str);
        if (str.startsWith(marginPrefix, nonBlankIndex)) {
            return str.substring(nonBlankIndex + marginPrefix.length());
        } else {
            return str;
        }
    }

    public static boolean isBlank(String str) {
        return firstNonBlankIndex(str) == str.length();
    }
    
    private static int firstNonBlankIndex(String str) {
        final int length = str.length();
        for (int index = 0; index < length; index++) {
            final char ch = str.charAt(index);
            if (ch != ' ' && ch != '\t' && !Character.isWhitespace(ch)) {
                return index;
            }
        }
        return length;
    }
}
