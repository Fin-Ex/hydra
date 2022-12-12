package ru.finex.ws.l2.network.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author finfan
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CharacterCreateOk implements NetworkDto {

	public static final CharacterCreateOk INSTANCE = new CharacterCreateOk();

}
