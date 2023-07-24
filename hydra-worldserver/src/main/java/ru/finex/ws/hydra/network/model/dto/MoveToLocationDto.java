package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author finfan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class MoveToLocationDto implements NetworkDto {

	private int runtimeId;
	private int positionX;
	private int positionY;
	private int positionZ;
	private int destinationX;
	private int destinationY;
	private int destinationZ;

}
