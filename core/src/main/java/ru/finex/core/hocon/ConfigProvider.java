package ru.finex.core.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.util.Objects;

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
            File file = new File("resources/application.conf");
            if(!file.exists()) {
                return null;
            }
            return ConfigFactory.parseFile(file);
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
