package net.sf.l2j.commons.mmocore;

import org.slf4j.LoggerFactory;

/**
 * @author KenM
 * @param <T>
 */
public interface IMMOExecutor<T extends MMOClient<?>> {

	public void execute(ReceivablePacket<T> packet);
}
