package ru.finex.ws.l2.network;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.GlobalContext;
import ru.finex.core.utils.ClassUtils;
import ru.finex.ws.l2.network.serial.PacketSerializer;
import sf.l2j.commons.lang.HexUtil;
import sf.l2j.commons.mmocore.AbstractPacket;
import sf.l2j.commons.mmocore.IPacketHandler;
import sf.l2j.commons.mmocore.ReceivablePacket;

import javax.inject.Singleton;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class PacketService {

    private final PacketRegistry incomePackets = new PacketRegistry();
    private final PacketRegistry outcomePackets = new PacketRegistry();

    public PacketService() {
        GlobalContext.reflections.getTypesAnnotatedWith(IncomePacket.class)
            .stream()
            .filter(e -> e.getCanonicalName().startsWith(GlobalContext.rootPackage))
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .forEach(e -> processPacket(incomePackets, e, e.getAnnotation(IncomePacket.class).value()));

        GlobalContext.reflections.getTypesAnnotatedWith(OutcomePacket.class)
            .stream()
            .filter(e -> e.getCanonicalName().startsWith(GlobalContext.rootPackage))
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .forEach(e -> processPacket(outcomePackets, e, e.getAnnotation(OutcomePacket.class).value()));

        log.info("Registered income packets: {}, outcome packets: {}", incomePackets.size(), outcomePackets.size());
    }

    private void processPacket(PacketRegistry registry, Class<?> packetType, Opcode[] opcodes) {
        for (int i = 0; i < opcodes.length; i++) {
            Opcode opcode = opcodes[i];
            if (opcodes.length - 1 == i) {
                registry.addPacket(opcode.value(), (Class<? extends AbstractPacket>) packetType);
            } else {
                PacketRegistry subRegistry = registry.getSubRegistry(opcode.value());
                if (subRegistry == null) {
                    subRegistry = new PacketRegistry();
                    registry.addSubRegistry(opcode.value(), subRegistry);
                }

                registry = subRegistry;
            }
        }
    }

    public <T extends AbstractPacket<?>> T createIncomePacket(int...opcodes) {
        return createPacket(incomePackets, opcodes);
    }

    public <T extends AbstractPacket<?>> T createOutcomePacket(int...opcodes) {
        return createPacket(outcomePackets, opcodes);
    }

    private static <T extends AbstractPacket<?>> T createPacket(PacketRegistry registry, int[] opcodes) {
        Class<? extends AbstractPacket> packetType = registry.getPacket(opcodes);
        if (packetType == null) {
            return null;
        }
        
        log.debug("Create outcome packet: {}", packetType.getSimpleName());

        return (T) ClassUtils.createInstance(packetType);
    }

    @Override
    public PacketSerializer<?> handlePacket(ByteBuffer buffer) {
        PacketSerializer packetSerializer = getPacketType(buffer, incomePackets, 0);
        if (packetSerializer == null) {
            byte[] data = new byte[buffer.limit() - 2];
            buffer.position(2);
            buffer.get(data);
            log.warn(HexUtil.printData(data, data.length));
            return null;
        }

        log.debug("Create income packet: {}", packetType.getSimpleName());
        
        return packetSerializer.serialize(buffer);
    }

    private Class<? extends AbstractPacket> getPacketType(ByteBuffer buffer, PacketRegistry registry, int opcodeCount) {
        int opcode;
        if (opcodeCount == 0) {
            opcode = buffer.get() & 0xff;
        } else {
            opcode = buffer.getShort() & 0xffff;
        }

        PacketRegistry subRegistry = registry.getSubRegistry(opcode);
        if (subRegistry != null) {
            return getPacketType(buffer, subRegistry, ++opcodeCount);
        }

        return registry.getPacket(opcode);
    }
}
