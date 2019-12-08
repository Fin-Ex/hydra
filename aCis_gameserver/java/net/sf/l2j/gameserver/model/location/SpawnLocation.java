package net.sf.l2j.gameserver.model.location;

import org.slf4j.LoggerFactory;

/**
 * A datatype extending {@link Location}, wildly used as character position, since it also stores heading of the character.
 */
public class SpawnLocation extends Location
{
	public static final SpawnLocation DUMMY_SPAWNLOC = new SpawnLocation(0, 0, 0, 0);
	
	protected volatile int heading;
	
	public SpawnLocation(int x, int y, int z, int heading)
	{
		super(x, y, z);
		
		this.heading = heading;
	}
	
	public SpawnLocation(SpawnLocation loc)
	{
		super(loc.getX(), loc.getY(), loc.getZ());
		
		heading = loc.getHeading();
	}
	
	@Override
	public String toString()
	{
		return x + ", " + y + ", " + z + ", " + heading;
	}
	
	@Override
	public int hashCode()
	{
		return x ^ y ^ z ^ heading;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof SpawnLocation)
		{
			SpawnLocation loc = (SpawnLocation) o;
			return (loc.getX() == x && loc.getY() == y && loc.getZ() == z && loc.getHeading() == heading);
		}
		
		return false;
	}
	
	public int getHeading()
	{
		return heading;
	}
	
	public void set(int x, int y, int z, int heading)
	{
		super.set(x, y, z);
		
		this.heading = heading;
	}
	
	public void set(SpawnLocation loc)
	{
		super.set(loc.getX(), loc.getY(), loc.getZ());
		
		heading = loc.getHeading();
	}
	
	@Override
	public void clean()
	{
		super.set(0, 0, 0);
		
		heading = 0;
	}
}