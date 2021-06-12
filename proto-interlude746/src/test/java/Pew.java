import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author finfan
 */
public class Pew {
	
	@Test
	public void getPacketOpcode() {
		ByteBuffer buffer = ByteBuffer.allocate(0x0f);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte)0x08);
		buffer.put((byte)0x00);
		buffer.position(0);
		System.out.println(buffer.getShort() & 0xffff);
	}
	
}
