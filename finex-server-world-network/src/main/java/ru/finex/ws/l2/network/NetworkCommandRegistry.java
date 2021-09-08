package ru.finex.ws.l2.network;

import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.GlobalContext;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.NetworkDto;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class NetworkCommandRegistry {

    private final Map<Class<?>, Class<? extends AbstractNetworkCommand>> registry;

    public NetworkCommandRegistry() {
        registry = GlobalContext.reflections.getTypesAnnotatedWith(IncomePacket.class)
            .stream()
            .map(e -> Pair.of(e, e.getDeclaredAnnotation(IncomePacket.class).command()))
            .filter(e -> e.getRight() != null)
            .collect(Collectors.toMap(
                Pair::getKey,
                Pair::getValue
            ));
    }

    public AbstractNetworkCommand createCommand(L2GameClientPacket packet, NetworkDto dto) {
        Class<? extends AbstractNetworkCommand> commandType = registry.get(packet.getClass());
        if (commandType == null || commandType == NoOpCommand.class) {
            return null;
        }

        AbstractNetworkCommand command;
        try {
            Constructor<? extends AbstractNetworkCommand> ctor = getCommandConstructor(commandType, dto);
            if (dto == null) {
                command = ctor.newInstance();
            } else {
                command = ctor.newInstance(dto);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Fail to create network command!", e);
        }

        return command;
    }

    private Constructor<? extends AbstractNetworkCommand> getCommandConstructor(Class<? extends AbstractNetworkCommand> type, NetworkDto dto)
        throws ReflectiveOperationException {
        Constructor<? extends AbstractNetworkCommand> ctor;
        if (dto == null) {
            ctor = type.getConstructor();
        } else {
            ctor = type.getConstructor(dto.getClass());
        }

        return ctor;
    }

}
