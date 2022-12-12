package ru.finex.ws.l2.network.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaveWorldDto implements NetworkDto {

    public static final LeaveWorldDto INSTANCE = new LeaveWorldDto();

}
