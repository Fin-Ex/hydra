package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class StopMoveDto implements NetworkDto {

	private int runtimeId;
	private int x;
	private int y;
	private int z;
	private int heading;

}
