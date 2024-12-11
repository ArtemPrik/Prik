package prik.preprocessor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Professional
 */
public final class Preprocessor {
    public static String preprocess(String code) {
        Map<String, String> macros = new HashMap<>();

        StringBuilder processedCode = new StringBuilder();

        String[] lines = code.split("\n");
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("#define")) {
                String[] parts = trimmedLine.split("\\s+", 3);
                if (parts.length == 3) {
                    macros.put(parts[1], parts[2]);
                }
            } else if (trimmedLine.startsWith("#include")) {
                String[] parts = trimmedLine.split("\\s+", 2);
                if (parts.length == 2) {
                    processedCode.append("import ").append(parts[1]).append("\n");
                }
            } else if (trimmedLine.startsWith("#jInclude")) {
                String[] parts = line.trim().split("\\s+", 2);
                if (parts.length == 2) {
                    String[] partsOfPkg = parts[1].split("\\.");
                    final String template = "const %s: any = JavaClass(%s)";
                    processedCode.append("import \"prik.lang.Reflection\"; ");
                    processedCode.append(String.format(template, partsOfPkg[2].replaceAll("\"", ""), parts[1]));
                    processedCode.append("\n");
                }
            } else {
                String lastLine = line;
                for (Map.Entry<String, String> entry : macros.entrySet()) {
                    lastLine = lastLine.replace(entry.getKey(), entry.getValue());
                }
                processedCode.append(lastLine).append("\n");
            }
        }

        return processedCode.toString();
    }
}
