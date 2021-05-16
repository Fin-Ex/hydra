package sf.l2j.loginserver.network.gameserverpackets;

import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import org.slf4j.Logger;

import javax.crypto.Cipher;

import sf.l2j.loginserver.network.clientpackets.ClientBasePacket;

public class BlowFishKey extends ClientBasePacket {

	byte[] _key;
	protected static final Logger _log = LoggerFactory.getLogger(BlowFishKey.class.getName());

	public BlowFishKey(byte[] decrypt, RSAPrivateKey privateKey) {
		super(decrypt);
		int size = readD();
		byte[] tempKey = readB(size);
		try {
			byte[] tempDecryptKey;
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
			tempDecryptKey = rsaCipher.doFinal(tempKey);
			// there are nulls before the key we must remove them
			int i = 0;
			int len = tempDecryptKey.length;
			for (; i < len; i++) {
				if (tempDecryptKey[i] != 0) {
					break;
				}
			}
			_key = new byte[len - i];
			System.arraycopy(tempDecryptKey, i, _key, 0, len - i);
		} catch (GeneralSecurityException e) {
			_log.error("Error While decrypting blowfish key (RSA)");
			e.printStackTrace();
		}
	}

	public byte[] getKey() {
		return _key;
	}
}
