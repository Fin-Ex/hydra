package ru.finex.gs.service;

import sf.l2j.commons.mmocore.AbstractPacket;

/**
 * @author m0nster.mind
 */
public interface PacketService {

    <T extends AbstractPacket<?>> T createIncomePacket(int...opcodes);
    <T extends AbstractPacket<?>> T createOutcomePacket(int...opcodes);

}
