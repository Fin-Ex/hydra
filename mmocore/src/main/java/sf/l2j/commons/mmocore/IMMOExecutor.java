package sf.l2j.commons.mmocore;

/**
 * @author KenM
 * @param <T>
 */
public interface IMMOExecutor<T extends MMOClient> {

	public void execute(ReceivablePacket<T> packet);
}
