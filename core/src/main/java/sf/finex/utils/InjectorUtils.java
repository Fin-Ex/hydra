package sf.finex.utils;

import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.experimental.UtilityClass;
import sf.finex.GlobalContext;
import sf.finex.inject.InjectedModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class InjectorUtils {

    public static List<Module> collectModules(Class<? extends Annotation> annType) {
        return GlobalContext.reflections.getTypesAnnotatedWith(InjectedModule.class)
            .stream()
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .map(Classes::createInstance)
            .map(e -> (Module) e)
            .collect(Collectors.toList());
    }

    public static Injector createChildInjector(Class<? extends Annotation> annType, Injector parentInjector) {
        return parentInjector.createChildInjector(collectModules(annType));
    }

}
