package ru.finex.core.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.Objects;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ConfigProvider implements Provider<Config> {

    private final Config config;

    public ConfigProvider() {
        config = loadConfig();
        Objects.requireNonNull(config, "Not found configuration!");
    }

    @Override
    public Config get() {
        return config;
    }

    private Config loadConfig() {
        Config config = tryLoadFromFilesystem();
        if (config == null) {
            config = tryLoadFromClasspath();
        }

        return config;
    }

    private Config tryLoadFromFilesystem() {
        try {
            return ConfigFactory.parseFile(new File("resources/application.conf"));
        } catch (Exception e) {
            return null;
        }
    }

    private Config tryLoadFromClasspath() {
        try {
            return ConfigFactory.load("application.conf");
        } catch (Exception e) {
            return null;
        }
    }

}
