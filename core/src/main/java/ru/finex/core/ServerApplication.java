package ru.finex.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.finex.core.inject.InjectedModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.utils.InjectorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author m0nster.mind
 */
public class ServerApplication {

    public static void start(String modulePackage, String[] args) {
        saveArguments(args);

        GlobalContext.rootPackage = modulePackage;
        GlobalContext.reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));

        Injector globalInjector = Guice.createInjector(Stage.PRODUCTION, InjectorUtils.collectModules(modulePackage, LoaderModule.class));
        GlobalContext.injector = globalInjector;

        ServerContext serverContext = globalInjector.getInstance(ServerContext.class);
        Injector serverInjector = InjectorUtils.createChildInjector(modulePackage, InjectedModule.class, globalInjector);
        serverContext.setInjector(serverInjector);

        ApplicationBuilt callback = serverInjector.getInstance(ApplicationBuilt.class);
        callback.onApplicationBuilt();
    }

    private static void saveArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] pair = arg.split("=", 1);
                arguments.put(pair[0], pair[1]);
            } else {
                arguments.put(arg, arg);
            }
        }
        GlobalContext.arguments = arguments;
    }

}
