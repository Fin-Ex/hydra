package ru.finex.ws.l2.network.model.dto;

import lombok.Data;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author finfan
 */
@Data
public class CharacterCreateOk implements NetworkDto {

	public static final CharacterCreateOk INSTANCE = new CharacterCreateOk();

}
