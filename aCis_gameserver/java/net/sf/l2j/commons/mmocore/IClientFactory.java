package net.sf.l2j.commons.mmocore;

import org.slf4j.LoggerFactory;

/**
 * @author KenM
 * @param <T>
 */
public interface IClientFactory<T extends MMOClient<?>> {

	public T create(final MMOConnection<T> con);
}
