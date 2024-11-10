package prik.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public class Character implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(23);
        
        map.set("isValidCodePoint", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isValidCodePoint(args[0].asInt()));
        });
        map.set("isBmpCodePoint", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isBmpCodePoint(args[0].asInt()));
        });
        map.set("isSupplementaryCodePoint", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isSupplementaryCodePoint(args[0].asInt()));
        });
        map.set("isHighSurrogate", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isHighSurrogate((char) args[0].asInt()));
        });
        map.set("isLowSurrogate", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isLowSurrogate((char) args[0].asInt()));
        });
        map.set("isSurrogate", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Character.isSurrogate((char) args[0].asInt()));
        });
        map.set("isSurrogate", (Value... args) -> {
            Arguments.check(2, args.length);
            return new BooleanValue(java.lang.Character.isSurrogatePair(
                    (char) args[0].asInt(), (char) args[1].asInt()));
        });
        map.set("isLowerCase", (Value... args) -> {
            Arguments.check(2, args.length);
            return new BooleanValue(java.lang.Character.isLowerCase(args[0].asInt()));
        });
        map.set("isUpperCase", (Value... args) -> {
            Arguments.check(2, args.length);
            return new BooleanValue(java.lang.Character.isUpperCase(args[0].asInt()));
        });
        map.set("isTitleCase", (Value... args) -> {
            Arguments.check(2, args.length);
            return new BooleanValue(java.lang.Character.isTitleCase(args[0].asInt()));
        });
        map.set("isDigit", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isDigit(args[0].asInt()));
        });
        map.set("isDefined", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isDefined(args[0].asInt()));
        });
        map.set("isLetter", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isLetter(args[0].asInt()));
        });
        map.set("isLetterOrDigit", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isLetterOrDigit(args[0].asInt()));
        });
        map.set("isAlphabetic", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isAlphabetic(args[0].asInt()));
        });
        map.set("isIdeographic", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isIdeographic(args[0].asInt()));
        });
        map.set("isIdentifierIgnorable", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isIdentifierIgnorable(args[0].asInt()));
        });
        map.set("isEmoji", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isEmoji(args[0].asInt()));
        });
        map.set("isEmojiPresentation", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isEmojiPresentation(args[0].asInt()));
        });
        map.set("isEmojiModifier", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isEmojiModifier(args[0].asInt()));
        });
        map.set("isEmojiModifierBase", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isEmojiModifierBase(args[0].asInt()));
        });
        map.set("isEmojiComponent", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isEmojiComponent(args[0].asInt()));
        });
        map.set("isExtendedPictographic", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(java.lang.Character.isExtendedPictographic(args[0].asInt()));
        });
        
        
        Variables.define("Character", map);
    }
}
