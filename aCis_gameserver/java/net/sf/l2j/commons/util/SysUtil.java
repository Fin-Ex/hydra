package net.sf.l2j.commons.util;

import org.slf4j.LoggerFactory;

/**
 * A class holding system oriented methods.
 */
public class SysUtil {

	private static final int MEBIOCTET = 1024 * 1024;

	/**
	 * @return the used amount of memory the JVM is using.
	 */
	public static long getUsedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MEBIOCTET;
	}

	/**
	 * @return the maximum amount of memory the JVM can use.
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory() / MEBIOCTET;
	}
}
