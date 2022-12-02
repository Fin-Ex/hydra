package ru.finex.ws.l2.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.core.uid.impl.ClusteredRuntimeIdService;
import ru.finex.transport.l2.model.dto.WorldSession;
import ru.finex.transport.l2.service.world.WorldSessionService;
import ru.finex.ws.l2.component.base.StatusComponent;
import ru.finex.ws.l2.model.enums.CharCreateFailReason;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.CharCreateOk;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.model.ClientSession;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;
import java.util.regex.Pattern;

@Slf4j
@Data
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CharacterCreateService {

	private final GameClient session;
	private final OutcomePacketBuilderService outcomePacketBuilderService;
	private final ClusteredRuntimeIdService clusteredRuntimeIdService;
	private final GameObjectService gameObjectService;
	private final ComponentService componentService;

	public void execute(CharacterCreateDto dto) {
		if ((dto.getName().length() < 1) || (dto.getName().length() > 16)) {
			log.debug("Character creation failure. Wrong name symbol count (must be > 1 & <= 16): {}", dto.getName());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_16_ENG_CHARS));
			return;
		}

		/*if (FakePlayerData.getInstance().getProperName(_name) != null)
		{
			log.debug("Character creation failure. Wrong name: {}", dto.getName());
			session.sendPacket(new CharCreateFail(CharCreateFail.REASON_INCORRECT_NAME));
			return;
		}*/

		boolean isNameAllNumerical = Pattern.matches("[0-9]", dto.getName());
		boolean isNameContainsIllegalCharacter = Pattern.matches("[aA-zZ0-9]", dto.getName());
		if (isNameAllNumerical || isNameContainsIllegalCharacter) {
			log.debug("Character creation failure. Wrong name (name from numbers or name contains illegal symbols): {}", dto.getName());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_INCORRECT_NAME));
			return;
		}

		if ((dto.getFace() > 2) || (dto.getFace() < 0)) {
			log.debug("Character creation failure. Wrong face type: {}", dto.getFace());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_CREATION_FAILED));
			return;
		}

		if ((dto.getHairStyle() < 0)
			|| ((dto.getSex() == 0) && (dto.getHairStyle() > 4))
			|| ((dto.getSex() != 0) && (dto.getHairStyle() > 6))) {
			log.debug("Character creation failure. Wrong hairStyle/sex: style={}[sex={}]", dto.getHairStyle(), dto.getSex());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_CREATION_FAILED));
			return;
		}

		if ((dto.getHairColor() > 3) || (dto.getHairColor() < 0)) {
			log.debug("Character creation failure. Wrong hairColor: {}", dto.getHairColor());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_CREATION_FAILED));
			return;
		}

		if (dto.getRace() == Race.ERTHEIA.getId()) {
			log.debug("Character creation failure. Wrong race: {}", dto.getRace());
			session.sendPacket(outcomePacketBuilderService.charCreateFail(CharCreateFailReason.REASON_CREATION_FAILED));
			return;
		}

		GameObject gameObject = gameObjectService.createGameObject("test", clusteredRuntimeIdService.generateId());

		StatusComponent status = componentService.getComponent(gameObject, StatusComponent.class);
		status.getStatusEntity().fillAllByMax();

		session.sendPacket(CharCreateOk.INSTANCE);

		session.sendPacket(outcomePacketBuilderService.charSelectInfo(session.getLogin(), session.getData().getSessionId()));

		//initNewChar(session, newChar);
		log.info("New character {} created!", gameObject);
	}

	/*private void initNewChar(GameClient client, PlayerInstance newChar)
	{
		World.getInstance().addObject(newChar);

		if (Config.STARTING_ADENA > 0)
		{
			newChar.addAdena("Init", Config.STARTING_ADENA, null, false);
		}

		final PlayerTemplate template = newChar.getTemplate();

		if (Config.CUSTOM_STARTING_LOC)
		{
			final Location createLoc = new Location(Config.CUSTOM_STARTING_LOC_X, Config.CUSTOM_STARTING_LOC_Y, Config.CUSTOM_STARTING_LOC_Z);
			newChar.setXYZInvisible(createLoc.getX(), createLoc.getY(), createLoc.getZ());
		}
		else if (Config.FACTION_SYSTEM_ENABLED)
		{
			newChar.setXYZInvisible(Config.FACTION_STARTING_LOCATION.getX(), Config.FACTION_STARTING_LOCATION.getY(), Config.FACTION_STARTING_LOCATION.getZ());
		}
		else
		{
			final Location createLoc = template.getCreationPoint();
			newChar.setXYZInvisible(createLoc.getX(), createLoc.getY(), createLoc.getZ());
		}
		newChar.setTitle("");

		if (Config.ENABLE_VITALITY)
		{
			newChar.setVitalityPoints(Math.min(Config.STARTING_VITALITY_POINTS, PlayerStat.MAX_VITALITY_POINTS), true);
		}
		if (Config.STARTING_LEVEL > 1)
		{
			newChar.getStat().addLevel((byte) (Config.STARTING_LEVEL - 1));
		}
		if (Config.STARTING_SP > 0)
		{
			newChar.getStat().addSp(Config.STARTING_SP);
		}

		final List<PlayerItemTemplate> initialItems = InitialEquipmentData.getInstance().getEquipmentList(newChar.getClassId());
		if (initialItems != null)
		{
			for (PlayerItemTemplate ie : initialItems)
			{
				final ItemInstance item = newChar.getInventory().addItem("Init", ie.getId(), ie.getCount(), newChar, null);
				if (item == null)
				{
					LOGGER.warning("Could not create item during char creation: itemId " + ie.getId() + ", amount " + ie.getCount() + ".");
					continue;
				}

				if (item.isEquipable() && ie.isEquipped())
				{
					newChar.getInventory().equipItem(item);
				}
			}
		}

		for (SkillLearn skill : SkillTreesData.getInstance().getAvailableSkills(newChar, newChar.getClassId(), false, true))
		{
			newChar.addSkill(SkillData.getInstance().getSkill(skill.getSkillId(), skill.getSkillLevel()), true);
		}

		// Register all shortcuts for actions, skills and items for this new character.
		InitialShortcutData.getInstance().registerAllShortcuts(newChar);

		EventDispatcher.getInstance().notifyEvent(new OnPlayerCreate(newChar, newChar.getObjectId(), newChar.getName(), client), Containers.Players());

		newChar.setOnlineStatus(true, false);
		if (Config.SHOW_GOD_VIDEO_INTRO)
		{
			newChar.getVariables().set("intro_god_video", true);
		}
		Disconnection.of(client, newChar).storeMe().deleteMe();

		final CharSelectionInfo cl = new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1);
		client.setCharSelection(cl.getCharInfo());
	}*/
}
