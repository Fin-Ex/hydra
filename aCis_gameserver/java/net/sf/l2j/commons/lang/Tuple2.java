package net.sf.l2j.commons.lang;

import lombok.Data;

/**
 * Tuple2
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Data
public class Tuple2<K, V> {

	private K first;
	private V second;

	public Tuple2(K k, V v) {
		first = k;
		second = v;
	}

	public Tuple2() {
	}
}
