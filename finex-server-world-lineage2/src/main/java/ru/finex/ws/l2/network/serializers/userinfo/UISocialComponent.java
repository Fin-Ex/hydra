package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UISocialComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        RecommendationComponent recommendationComponent = dto.getRecommendationComponent();
        buffer.writeByte(0x00); // FIXME finfan: pvp flag
        buffer.writeIntLE(0x00); // FIXME finfan: Reputation
        buffer.writeByte(0x00); // FIXME finfan: noble level
        buffer.writeByte(0); //FIXME finfan: _player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 1 : 0
        buffer.writeByte(0x00); // FIXME finfan: pledge class
        buffer.writeIntLE(0); // FIXME finfan: pkkills
        buffer.writeIntLE(0); // FIXME finfan: pvpkills
        buffer.writeShortLE(recommendationComponent.getLeft());
        buffer.writeShortLE(recommendationComponent.getCollect());
    }

}
