package prik.lib;

import prik.exceptions.PrikException;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class ModuleLoader {
    private static final String PACKAGE = "prik.lib.modules.%s";

    private ModuleLoader() { }

    public static Module load(String name) {
        try {
            return (Module) Class.forName(String.format(PACKAGE, name))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load module \"" + name + "\"\n\n", ex);
        }
    }

    public static void loadAndUse(String name) {
        final var module = load(name);
        module.init();
    }
}
