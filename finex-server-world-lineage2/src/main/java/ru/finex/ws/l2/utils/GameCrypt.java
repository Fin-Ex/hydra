package ru.finex.ws.l2.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author KenM
 */
public class GameCrypt {

	private final byte[] inKey = new byte[16];
	private final byte[] outKey = new byte[16];
	private boolean isEnabled;

	public void setKey(byte[] key) {
		System.arraycopy(key, 0, inKey, 0, 16);
		System.arraycopy(key, 0, outKey, 0, 16);
	}

	public void decrypt(ByteBuf buffer) {
		if (!isEnabled) {
			return;
		}

		int prevByte = 0x00;
		while (buffer.isReadable()) {
			int index = buffer.readerIndex();
			int origin = buffer.readByte() & 0xff;
			int encrypted = origin ^ inKey[index & 15] ^ prevByte;
			prevByte = encrypted;
			buffer.setByte(index, encrypted);
		}

		int old = inKey[8] & 0xff;
		old |= inKey[9] << 8 & 0xff00;
		old |= inKey[10] << 0x10 & 0xff0000;
		old |= inKey[11] << 0x18 & 0xff000000;

		old += buffer.writerIndex();

		inKey[8] = (byte) (old & 0xff);
		inKey[9] = (byte) (old >> 0x08 & 0xff);
		inKey[10] = (byte) (old >> 0x10 & 0xff);
		inKey[11] = (byte) (old >> 0x18 & 0xff);
	}

	public void encrypt(ByteBuf buffer) {
		if (!isEnabled) {
			isEnabled = true;
			return;
		}

		int prevByte = 0x00;
		while (buffer.isReadable()) {
			int index = buffer.readerIndex();
			int origin = buffer.readByte() & 0xff;
			int encrypted = origin ^ inKey[index & 15] ^ prevByte;
			prevByte = encrypted;
			buffer.setByte(index, encrypted);
		}

		int old = outKey[8] & 0xff;
		old |= outKey[9] << 8 & 0xff00;
		old |= outKey[10] << 0x10 & 0xff0000;
		old |= outKey[11] << 0x18 & 0xff000000;

		old += buffer.writerIndex();

		outKey[8] = (byte) (old & 0xff);
		outKey[9] = (byte) (old >> 0x08 & 0xff);
		outKey[10] = (byte) (old >> 0x10 & 0xff);
		outKey[11] = (byte) (old >> 0x18 & 0xff);
	}
}
