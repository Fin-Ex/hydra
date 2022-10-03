package ru.finex.ws.l2.network.model.dto;

import lombok.Data;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
public class LeaveWorldDto implements NetworkDto {

    public static final LeaveWorldDto INSTANCE = new LeaveWorldDto();

}
