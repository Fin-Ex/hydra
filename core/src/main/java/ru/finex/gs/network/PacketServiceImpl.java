package ru.finex.gs.network;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.GlobalContext;
import ru.finex.core.utils.Classes;
import ru.finex.gs.service.PacketService;
import sf.l2j.commons.lang.HexUtil;
import sf.l2j.commons.mmocore.AbstractPacket;
import sf.l2j.commons.mmocore.IPacketHandler;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.gameserver.network.L2GameClient;

import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class PacketServiceImpl implements PacketService, IPacketHandler<L2GameClient> {

    private final PacketRegistry incomePackets = new PacketRegistry();
    private final PacketRegistry outcomePackets = new PacketRegistry();

    public PacketServiceImpl() {
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

    @Override
    public <T extends AbstractPacket<?>> T createIncomePacket(int...opcodes) {
        return createPacket(incomePackets, opcodes);
    }

    @Override
    public <T extends AbstractPacket<?>> T createOutcomePacket(int...opcodes) {
        return createPacket(outcomePackets, opcodes);
    }

    private static <T extends AbstractPacket<?>> T createPacket(PacketRegistry registry, int[] opcodes) {
        Class<? extends AbstractPacket> packetType = registry.getPacket(opcodes);
        if (packetType == null) {
            return null;
        }

        return (T) Classes.createInstance(packetType);
    }

    @Override
    public ReceivablePacket<L2GameClient> handlePacket(ByteBuffer buffer, L2GameClient client) {
        Class<? extends AbstractPacket> packetType = getPacketType(buffer, incomePackets, 0);
        if (packetType == null) {
            byte[] data = new byte[buffer.limit()];
            buffer.position(0);
            buffer.get(data);
            log.warn(HexUtil.printData(data, data.length));
            return null;
        }

        return (ReceivablePacket<L2GameClient>) Classes.createInstance(packetType);
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
            return getPacketType(buffer, registry, ++opcodeCount);
        }

        return registry.getPacket(opcode);
    }
}
