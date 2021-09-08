package ru.finex.ws.l2.network;

import sf.l2j.commons.mmocore.AbstractPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author m0nster.mind
 */
public class PacketRegistry {

    private final Map<Integer, Class<? extends AbstractPacket>> packets = new HashMap<>();
    private final Map<Integer, PacketRegistry> subRegistries = new HashMap<>();

    public void addPacket(int opcode, Class<? extends AbstractPacket> packet) {
        packets.put(opcode, packet);
    }

    public void addSubRegistry(int opcode, PacketRegistry registry) {
        subRegistries.put(opcode, registry);
    }

    public Class<? extends AbstractPacket> getPacket(int...opcodes) {
        return getPacket(opcodes, 0);
    }

    private Class<? extends AbstractPacket> getPacket(int[] opcodes, int opcodePosition) {
        int opcode = opcodes[opcodePosition];
        if (opcodes.length - 1 == opcodePosition) {
            return packets.get(opcode);
        }

        PacketRegistry subRegistry = subRegistries.get(opcode);
        if (subRegistry == null) {
            return null; // sub registry not found
        }

        return subRegistry.getPacket(opcodes, ++opcodePosition);
    }

    protected PacketRegistry getSubRegistry(int opcode) {
        return subRegistries.get(opcode);
    }

    public int size() {
        return packets.size() + subRegistries.values().stream()
            .mapToInt(PacketRegistry::size)
            .sum();
    }

}
