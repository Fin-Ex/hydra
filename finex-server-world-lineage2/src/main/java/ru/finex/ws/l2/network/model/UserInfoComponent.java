package ru.finex.ws.l2.network.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.network.serializers.userinfo.UIAnimationComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIAtkElementalComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIBaseStatsComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIBasicInfoComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIClanComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIColliderComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIColorComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIComponentSerializer;
import ru.finex.ws.l2.network.serializers.userinfo.UIElementalComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIEnchantComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIHeroComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIInventoryLimitComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIMaxStatusComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIMoveTypeComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIPositionComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIRelationComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UISlotComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UISocialComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UISpeedComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIStatComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIStatusComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIStatusFlagComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIVisualComponent;
import ru.finex.ws.l2.network.serializers.userinfo.UIVitaFameComponent;

import java.util.Arrays;
import java.util.List;

/**
 * @author finfan
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public enum UserInfoComponent implements NetworkComponent {
	// three octets
	RELATION(0x00, 1 << 7, UIRelationComponent.class),
	BASIC_INFO(0x00, 1 << 6, UIBasicInfoComponent.class),
	BASE_STATS(0x00, 1 << 5, UIBaseStatsComponent.class),
	MAX_STATUS(0x00, 1 << 4, UIMaxStatusComponent.class),
	STATUS(0x00, 1 << 3, UIStatusComponent.class),
	ENCHANT(0x00, 1 << 2, UIEnchantComponent.class),
	VISUAL(0x00, 1 << 1, UIVisualComponent.class),
	STATUS_FLAG(0x00, 1 << 0, UIStatusFlagComponent.class),
	
	STAT(0x01, 1 << 7, UIStatComponent.class),
	ELEMENTAL(0x01, 1 << 6, UIElementalComponent.class),
	POSITION(0x01, 1 << 5, UIPositionComponent.class),
	SPEED(0x01, 1 << 4, UISpeedComponent.class),
	ANIMATION(0x01, 1 << 3, UIAnimationComponent.class),
	COLLIDER(0x01, 1 << 2, UIColliderComponent.class),
	ATK_ELEMENTAL(0x01, 1 << 1, UIAtkElementalComponent.class),
	CLAN(0x01, 1 << 0, UIClanComponent.class),
	
	SOCIAL(0x02, 1 << 7, UISocialComponent.class),
	VITA_FAME(0x02, 1 << 6, UIVitaFameComponent.class),
	SLOT(0x02, 1 << 5, UISlotComponent.class),
	MOVE_TYPE(0x02, 1 << 4, UIMoveTypeComponent.class),
	COLOR(0x02, 1 << 3, UIColorComponent.class),
	INVENTORY_LIMIT(0x02, 1 << 2, UIInventoryLimitComponent.class),
	HERO(0x02, 1 << 1, UIHeroComponent.class);

	/** The byte position. */
	@Getter private final int position;
	/** The mask flag. */
	@Getter private final int flag;
	@Getter private final Class<? extends UIComponentSerializer> serializer;

	public static List<UserInfoComponent> all() {
		return Arrays.asList(values());
	}

	public static int octets() {
		return 3;
	}

	public static int count() {
		return values().length;
	}

}