package ru.finex.gs.proto.interlude;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventBus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author finfan
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class IngameStateProcessor {
	
	private final OutcomePacketBuilderService packetBuilderService;
	
	@Inject
	public void registerListeners(@Named("Network") EventBus eventBus) {
	
	}
	
}
