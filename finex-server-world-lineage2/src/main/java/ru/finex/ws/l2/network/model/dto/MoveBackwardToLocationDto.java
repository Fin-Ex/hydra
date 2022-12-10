package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.math.vector.Vector3f;
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
public class MoveBackwardToLocationDto implements NetworkDto {

	private int positionX;
	private int positionY;
	private int positionZ;
	private int destinationX;
	private int destinationY;
	private int destinationZ;
	private int mode; // FIXME m0nster.mind: to enum

	public Vector3f getPosition() {
		return new Vector3f(positionX, positionY, positionZ);
	}

	public Vector3f getDestination() {
		return new Vector3f(destinationX, destinationY, destinationZ);
	}

}
