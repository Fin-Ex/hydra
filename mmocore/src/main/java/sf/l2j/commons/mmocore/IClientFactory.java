package sf.l2j.commons.mmocore;

/**
 * @author KenM
 * @param <T>
 */
public interface IClientFactory<T extends MMOClient> {

	T create(final MMOConnection<T> con);

}
