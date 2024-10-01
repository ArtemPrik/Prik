package prik.lib.modules.prik.util;

import prik.lib.Arguments;
import prik.lib.MapValue;
import prik.lib.StringValue;
import prik.lib.Variables;
import prik.lib.modules.Module;
import prik.util.ModuleUtils;

/**
 *
 * @author Professional
 */
public final class Font implements Module {
    @Override
    public void init() {
        MapValue font = new MapValue(1);
        
        font.set("renderText", args -> {
            Arguments.check(1, args.length);
            String raw = args[0].asString().toLowerCase();
            StringBuilder[] lines = new StringBuilder[4];
            for (int i = 0; i < 4; i++) {
                lines[i] = new StringBuilder();
            }
            for (int i = 0; i < raw.length(); i++) {
                char c = raw.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    int index = c - 'a';
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][index]);
                    }
                } else if (c == '.') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][36]);
                    }
                } else if (c == ',') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][37]);
                    }
                } else if (c == ' ') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append("     ");
                    }
                }else if (c == '!') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][38]);
                    }
                }else if (c == '/') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][39]);
                    }
                }else if (c == '<') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][40]);
                    }
                }else if (c == '>') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][41]);
                    }
                }else if (c == '?') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][42]);
                    }
                } else if (c == '\\') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][43]);
                    }
                }else if (c == ':') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][44]);
                    }
                }else if (c == '1') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][26]);
                    }
                }else if (c == '2') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][27]);
                    }
                }else if (c == '3') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][28]);
                    }
                }else if (c == '4') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][29]);
                    }
                }else if (c == '5') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][30]);
                    }
                }else if (c == '6') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][31]);
                    }
                }else if (c == '7') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][32]);
                    }
                }else if (c == '8') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][33]);
                    }
                }else if (c == '9') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][34]);
                    }
                }else if (c == '0') {
                    for (int j = 0; j < 4; j++) {
                        lines[j].append(ModuleUtils.letters[j][35]);
                    }
                }
            }
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                result.append(lines[i]).append("\n");
            }
            return new StringValue(result.toString());
        });
        
        Variables.define("Font", font);
    }
}
