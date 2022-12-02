package ru.finex.ws.l2.network.model.dto;

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
public class ChangeMoveTypeDto implements NetworkDto {

	private int runtimeId;
	private boolean isRunning;

}
