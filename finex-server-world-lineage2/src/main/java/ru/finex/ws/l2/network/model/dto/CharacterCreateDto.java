package ru.finex.ws.l2.network.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
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
public class CharacterCreateDto implements NetworkDto {

    @NotNull
    @Length(min = 1, max = 16)
    @Pattern(regexp = "[\\w\\d]{1,16}")
    private String name;

    @Range(min = 0, max = 6)
    private int race;

    @Range(min = 0, max = 1)
    private byte sex;

    private int classId;

    @Range(min = 0, max = 100)
    private int INT;

    @Range(min = 0, max = 100)
    private int STR;

    @Range(min = 0, max = 100)
    private int VIT; // CON

    @Range(min = 0, max = 100)
    private int SPRT; // MEN

    @Range(min = 0, max = 100)
    private int DEX;

    @Range(min = 0, max = 100)
    private int AGI; // WIT

    @Range(min = 0, max = 6)
    private byte hairStyle;

    @Range(min = 0, max = 3)
    private byte hairColor;

    @Range(min = 0, max = 2)
    private byte face;

}
