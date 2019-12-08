package net.sf.l2j.gameserver.handler.usercommandhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Support for /dismount command.
 * @author Micht
 */
public class DisMount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		62
	};
	
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (activeChar.isMounted())
			activeChar.dismount();
		
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}