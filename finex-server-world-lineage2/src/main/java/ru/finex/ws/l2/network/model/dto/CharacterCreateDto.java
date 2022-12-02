package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String name;
    private int race;
    private byte sex;
    private int classId;
    private int INT;
    private int STR;
    private int VIT; // CON
    private int SPRT; // MEN
    private int DEX;
    private int AGI; // WIT
    private byte hairStyle;
    private byte hairColor;
    private byte face;

}
