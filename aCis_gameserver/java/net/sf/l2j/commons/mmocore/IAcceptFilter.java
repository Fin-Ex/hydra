package net.sf.l2j.commons.mmocore;

import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;

/**
 * @author KenM
 */
public interface IAcceptFilter {

	public boolean accept(SocketChannel sc);
}
