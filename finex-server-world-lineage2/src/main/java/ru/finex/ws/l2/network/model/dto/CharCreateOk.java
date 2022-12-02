package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

@Data
@Builder
@NetworkCommandScoped
public class CharCreateOk implements NetworkDto {

	public static final CharCreateOk INSTANCE = new CharCreateOk();

}
