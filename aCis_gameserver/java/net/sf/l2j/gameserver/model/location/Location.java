package net.sf.l2j.gameserver.model.location;

/**
 * A datatype used to retain a 3D (x/y/z) point. It got the capability to be set
 * and cleaned.
 */
public class Location {

	public static final Location DUMMY_LOC = new Location(0, 0, 0);

	protected volatile int x;
	protected volatile int y;
	protected volatile int z;

	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
	}

	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}

	@Override
	public int hashCode() {
		return x ^ y ^ z;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Location) {
			Location loc = (Location) o;
			return (loc.getX() == x && loc.getY() == y && loc.getZ() == z);
		}

		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public void set(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
	}

	public void clean() {
		x = 0;
		y = 0;
		z = 0;
	}
}
