package ru.finex.gs.concurrent.game;

import ru.finex.gs.model.Client;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.Component;
import ru.finex.gs.model.component.player.ClientComponent;
import ru.finex.gs.service.WorldService;
import ru.finex.gs.service.concurrent.GameExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author finfan
 */
@Singleton
public class GameTickServiceImpl {
	
	private final WorldService worldService;
	private final ScheduledFuture<?> tickTask;
	
	@Inject
	public GameTickServiceImpl(WorldService worldService, GameExecutorService executorService) {
		this.worldService = worldService;
		tickTask = executorService.execute(new RunnableGameTask(this::tick), 50, 50, TimeUnit.MILLISECONDS);
	}
	
	public void tick() {
		List<Component> components = worldService.getGameObjects().stream()
			.flatMap(e -> e.getComponents().stream())
			.sorted(Component.COMPARATOR)
			.collect(Collectors.toList());
		
		GameThread thread = (GameThread) Thread.currentThread();
		doUpdate(thread, components, Component::onPreUpdate);
		doUpdate(thread, components, Component::onUpdate);
		doUpdate(thread, components, Component::onPostUpdate);
	}
	
	private void doUpdate(GameThread thread, List<Component> components, Consumer<Component> consumer) {
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			
			GameObject gameObject = component.getGameObject();
			thread.setGameObject(gameObject);
			
			ClientComponent clientComponent = gameObject.getComponent(ClientComponent.class);
			if (clientComponent != null) {
				Client client = clientComponent.getClient();
				thread.setClient(client);
			}
			
			consumer.accept(component);
		}
	}
	
}
