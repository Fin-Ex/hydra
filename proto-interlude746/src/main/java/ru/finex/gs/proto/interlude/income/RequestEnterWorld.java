package ru.finex.gs.proto.interlude.income;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.events.EventBus;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.event.PlayerEnterWorld;
import ru.finex.gs.proto.interlude.OutcomePacketBuilderService;
import ru.finex.gs.proto.network.IncomePacket;
import ru.finex.gs.proto.network.L2GameClient;
import ru.finex.gs.proto.network.L2GameClientPacket;
import ru.finex.gs.proto.network.Opcode;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author m0nster.mind
 */
@Slf4j
@IncomePacket(@Opcode(0x03))
public class RequestEnterWorld extends L2GameClientPacket {

    @Inject @Named("Network")
    private EventBus eventBus;
    
    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    protected void readImpl() {

    }

    @Override
    protected void runImpl() {
        L2GameClient client = getClient();
        GameObject gameObject = client.getGameObject();
        
        eventBus.notify(new PlayerEnterWorld(gameObject));
        sendPacket(packetBuilderService.userInfo(gameObject));
    }
}
