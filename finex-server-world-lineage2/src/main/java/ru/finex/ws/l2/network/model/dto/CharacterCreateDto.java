package ru.finex.ws.l2.network.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.model.enums.Gender;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.validation.GenderHairType;
import ru.finex.ws.l2.validation.GenderSubset;
import ru.finex.ws.l2.validation.RaceSubset;
import ru.finex.ws.l2.validation.ValidGenderHairType;
import ru.finex.ws.l2.validation.ValidStarterClass;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
@ValidGenderHairType
public class CharacterCreateDto implements NetworkDto, GenderHairType {

    @NotNull
    @Pattern(regexp = "^[\\w\\d]{3,16}$")
    private String name;

    @NotNull
    @RaceSubset({ Race.HUMAN, Race.ELF, Race.DARK_ELF, Race.ORC, Race.DWARF, Race.KAMAEL, Race.ERTHEIA })
    private Race race;

    @NotNull
    @GenderSubset({ Gender.MALE, Gender.FEMALE })
    private Gender gender;

    @NotNull
    @ValidStarterClass
    private int classId;

    @Range(min = 0, max = 100)
    private int INT;

    @Range(min = 0, max = 100)
    private int STR;

    @Range(min = 0, max = 100)
    private int CON;

    @Range(min = 0, max = 100)
    private int MEN;

    @Range(min = 0, max = 100)
    private int DEX;

    @Range(min = 0, max = 100)
    private int WIT;

    @Range(min = 0, max = 6)
    private int hairType;

    @Range(min = 0, max = 3)
    private int hairColor;

    @Range(min = 0, max = 2)
    private int face;

}
