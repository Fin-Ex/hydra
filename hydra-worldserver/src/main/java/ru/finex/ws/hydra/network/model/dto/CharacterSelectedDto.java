package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.hydra.model.enums.ClassId;
import ru.finex.ws.hydra.model.enums.Gender;
import ru.finex.ws.hydra.model.PlayerAppearanceClass;
import ru.finex.ws.hydra.model.enums.Race;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSelectedDto implements NetworkDto {

    private int runtimeId;
    private int sessionId;

    private String name;
    private String title;
    private Race race;
    private Gender gender;
    private PlayerAppearanceClass appearanceClass;
    private ClassId classId;

    private int clanId;

    private double x;
    private double y;
    private double z;

    private double hp;
    private double mp;

    private long sp;
    private long exp;
    private int level;

}
