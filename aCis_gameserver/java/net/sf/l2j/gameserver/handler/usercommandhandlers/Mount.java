package net.sf.l2j.gameserver.handler.usercommandhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Support for /mount command.
 * @author Tempy
 */
public class Mount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		61
	};
	
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		return activeChar.mountPlayer(activeChar.getActiveSummon());
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}