package ru.finex.ws.hydra.network.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.ws.hydra.network.serializers.userinfo.UIAnimationComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIAtkElementalComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIBaseStatsComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIBasicInfoComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIClanComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIColliderComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIColorComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIComponentSerializer;
import ru.finex.ws.hydra.network.serializers.userinfo.UIElementalComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIEnchantComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIHeroComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIInventoryLimitComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIMaxStatusComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIMoveTypeComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIPositionComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIRelationComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UISlotComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UISocialComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UISpeedComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIStatComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIStatusComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIStatusFlagComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIVisualComponent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIVitaFameComponent;

import java.util.Arrays;
import java.util.List;

/**
 * @author finfan
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public enum UserInfoComponent implements NetworkComponent {
	// three octets
	RELATION(UIRelationComponent.class),
	BASIC_INFO(UIBasicInfoComponent.class),
	BASE_STATS(UIBaseStatsComponent.class),
	MAX_STATUS(UIMaxStatusComponent.class),
	STATUS(UIStatusComponent.class),
	ENCHANT(UIEnchantComponent.class),
	VISUAL(UIVisualComponent.class),
	STATUS_FLAG(UIStatusFlagComponent.class),
	
	STAT(UIStatComponent.class),
	ELEMENTAL(UIElementalComponent.class),
	POSITION(UIPositionComponent.class),
	SPEED(UISpeedComponent.class),
	ANIMATION(UIAnimationComponent.class),
	COLLIDER(UIColliderComponent.class),
	ATK_ELEMENTAL(UIAtkElementalComponent.class),
	CLAN(UIClanComponent.class),
	
	SOCIAL(UISocialComponent.class),
	VITA_FAME(UIVitaFameComponent.class),
	SLOT(UISlotComponent.class),
	MOVE_TYPE(UIMoveTypeComponent.class),
	COLOR(UIColorComponent.class),
	INVENTORY_LIMIT(UIInventoryLimitComponent.class),
	HERO(UIHeroComponent.class);

	@Getter private final Class<? extends UIComponentSerializer> serializer;

	@Override
	public int getFlag() {
		return 1 << ordinal();
	}

	public static List<UserInfoComponent> all() {
		return Arrays.asList(values());
	}

	public static int count() {
		return values().length;
	}

}