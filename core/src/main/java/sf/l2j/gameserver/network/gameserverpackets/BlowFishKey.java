package sf.l2j.gameserver.network.gameserverpackets;

import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import org.slf4j.Logger;

import javax.crypto.Cipher;

public class BlowFishKey extends GameServerBasePacket {

	private static Logger _log = LoggerFactory.getLogger(BlowFishKey.class.getName());

	public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey) {
		writeC(0x00);
		byte[] encrypted = null;
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encrypted = rsaCipher.doFinal(blowfishKey);

			writeD(encrypted.length);
			writeB(encrypted);
		} catch (GeneralSecurityException e) {
			_log.error("Error While encrypting blowfish key for transmision (Crypt error)");
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
