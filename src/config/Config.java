package config;

import java.util.logging.Logger;


public final class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private Config() {
    }

    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(toEnvVarName(key));
        }
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key, null);
        if (value == null) return defaultValue;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number for " + key + ": '" + value + "'. Using default " + defaultValue + ".");
            return defaultValue;
        }
    }

    private static String toEnvVarName(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}
