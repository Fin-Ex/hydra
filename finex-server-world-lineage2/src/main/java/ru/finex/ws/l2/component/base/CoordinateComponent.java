package ru.finex.ws.l2.component.base;

import lombok.Getter;
import ru.finex.core.object.GameObject;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.component.player.ColliderComponent;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.ActionFailedDto;
import ru.finex.ws.l2.network.model.dto.MoveBackwardToLocationDto;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;
import ru.finex.ws.l2.persistence.PositionComponentPersistence;
import ru.finex.ws.model.ClientSession;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * Базовый компонент, хранящий в себе координаты игрового объекта на карте мира.
 *
 * @author m0nster.mind
 */
public class CoordinateComponent extends AbstractComponent {

    @Getter
    @PersistenceField(PositionComponentPersistence.class)
    private PositionComponentEntity entity = new PositionComponentEntity();

    public void setXYZ(double x, double y, double z) {
        entity.setXYZ(x, y, z);
    }

    public void moveToLocation(ClientSession session,
                               MoveBackwardToLocationDto dto,
                               OutcomePacketBuilderService outcomePacketBuilderService,
                               ColliderComponent collisionComponent) {

        GameObject gameObject = getGameObject();

        if ((dto.getDestinationX() == dto.getStartX())
            && (dto.getDestinationY() == dto.getStartY())
            && (dto.getDestinationZ() == dto.getStartZ())) {
            session.sendPacket(outcomePacketBuilderService.stopMove(gameObject));
            session.sendPacket(ActionFailedDto.INSTANCE);
            return;
        }

        // Validate position packets sends head level.
        dto.setDestinationZ((int) (dto.getDestinationZ() + collisionComponent.getHeight()));

        session.sendPacket(dto);
        session.sendPacket(outcomePacketBuilderService.validateLocation(gameObject));
        session.sendPacket(outcomePacketBuilderService.moveToLocation(gameObject,
            dto.getDestinationX(), dto.getDestinationY(), dto.getDestinationZ()));
    }

    public void validatePosition(ClientSession session, ValidateLocationDto dto) {
        final int realX = entity.getX().intValue();
        final int realY = entity.getY().intValue();
        int realZ = entity.getZ().intValue();

        int _x = dto.getX();
        int _y = dto.getY();
        int _z = dto.getZ();

        if ((_x == 0) && (_y == 0)) {
            if (realX != 0) {
                return;
            }
        }

        int dx;
        int dy;
        int dz;
        double diffSq;

        dx = _x - realX;
        dy = _y - realY;
        dz = _z - realZ;
        diffSq = ((dx * dx) + (dy * dy));


        if (diffSq < 360000) // if too large, messes observation
        {
            entity.setXYZ(realX, realY, realZ);

            if ((diffSq > 250000) || (Math.abs(dz) > 200)) {
                if ((Math.abs(dz) > 200) && (Math.abs(dz) < 1500) && (Math.abs(_z - realZ) < 800)) {
                    entity.setXYZ(realX, realY, realZ);
                } else {
                    entity.setXYZ(_x, _y, _z);
                    entity.setH(dto.getHeading());
                    session.sendPacket(dto);
                    return;
                }
            }
        }

        entity.setXYZ(_x, _y, _z);
        entity.setH(dto.getHeading());
        session.sendPacket(dto);
    }
}
