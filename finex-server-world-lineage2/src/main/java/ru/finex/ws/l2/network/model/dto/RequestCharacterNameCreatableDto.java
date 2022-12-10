package ru.finex.ws.l2.network.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class RequestCharacterNameCreatableDto implements NetworkDto {

    @NotNull
    @Length(min = 1, max = 16)
    @Pattern(regexp = "[\\w\\d]{1,16}")
    private String name;

}
