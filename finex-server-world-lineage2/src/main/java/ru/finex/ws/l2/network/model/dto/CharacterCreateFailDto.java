package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.model.enums.CharacterCreateReason;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class CharacterCreateFailDto implements NetworkDto {

	private CharacterCreateReason error;

}
