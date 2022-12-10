package ru.finex.ws.l2.model;

import ru.finex.ws.l2.model.enums.ClassId;
import ru.finex.ws.l2.model.enums.Gender;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.model.exception.AppearanceClassNotFoundException;

/**
 * @author m0nster.mind
 */
public enum PlayerAppearanceClass {

    FIGHTER {
        @Override
        public ClassId getClassId(Race race, Gender gender) throws AppearanceClassNotFoundException {
            return switch (race) {
                case HUMAN -> ClassId.HUMAN_FIGHTER;
                case ELF -> ClassId.ElvenFighter;
                case DARK_ELF -> ClassId.DarkFighter;
                case ORC -> ClassId.OrcFighter;
                case DWARF -> ClassId.DwarvenFighter;
                case KAMAEL -> switch (gender) {
                    case MALE -> ClassId.MALE_SOLDIER;
                    case FEMALE -> ClassId.FEMALE_SOLDIER;
                    default -> throw new AppearanceClassNotFoundException(race, gender);
                };
                case ERTHEIA -> ClassId.ERTHEIA_FIGHTER;
                default -> throw new AppearanceClassNotFoundException(race);
            };
        }
    },
    WIZARD {
        @Override
        public ClassId getClassId(Race race, Gender gender) throws AppearanceClassNotFoundException {
            return switch (race) {
                case HUMAN -> ClassId.HUMAN_MYSTIC;
                case ELF -> ClassId.ElvenWizard;
                case DARK_ELF -> ClassId.DarkWizard;
                case ORC -> ClassId.OrcMystic;
                case DWARF -> ClassId.DwarvenFighter;
                case KAMAEL -> switch (gender) {
                    case MALE -> ClassId.MALE_SOLDIER;
                    case FEMALE -> ClassId.FEMALE_SOLDIER;
                    default -> throw new AppearanceClassNotFoundException(race, gender);
                };
                case ERTHEIA -> ClassId.ERTHEIA_WIZARD;
                default -> throw new AppearanceClassNotFoundException(race);
            };
        }
    };

    public abstract ClassId getClassId(Race race, Gender gender) throws AppearanceClassNotFoundException;

    public int getNetworkId(Race race, Gender gender) throws AppearanceClassNotFoundException {
        return getClassId(race, gender).getId();
    }

    public static PlayerAppearanceClass ofClassId(int classId, Race race, Gender gender)
        throws AppearanceClassNotFoundException {
        PlayerAppearanceClass appearanceClass;
        if (FIGHTER.getNetworkId(race, gender) == classId) {
            appearanceClass = FIGHTER;
        } else if (WIZARD.getNetworkId(race, gender) == classId) {
            appearanceClass = WIZARD;
        } else {
            throw new AppearanceClassNotFoundException("Not found PlayerAppearanceClass for classId: " + classId);
        }

        return appearanceClass;
    }

}
