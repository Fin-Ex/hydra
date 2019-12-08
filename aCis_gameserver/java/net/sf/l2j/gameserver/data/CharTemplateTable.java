package net.sf.l2j.gameserver.data;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;

import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Unknown, Forsaiken
 */
public class CharTemplateTable
{
	private static final Logger _log = LoggerFactory.getLogger(CharTemplateTable.class.getName());
	
	private final Map<Integer, PlayerTemplate> _templates = new HashMap<>();
	
	public static CharTemplateTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected CharTemplateTable()
	{
		final File mainDir = new File("./data/xml/classes");
		if (!mainDir.isDirectory())
		{
			_log.error( "CharTemplateTable: Main dir " + mainDir.getAbsolutePath() + " hasn't been found.");
			return;
		}
		
		for (final File file : mainDir.listFiles())
		{
			if (file.isFile() && file.getName().endsWith(".xml"))
				loadFileClass(file);
		}
		
		_log.info( "CharTemplateTable: Loaded " + _templates.size() + " character templates.");
		_log.info( "CharTemplateTable: Loaded " + SkillTreeTable.getInstance().getSkillTreesSize() + " classes skills trees.");
	}
	
	private void loadFileClass(final File f)
	{
		try
		{
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if ("class".equalsIgnoreCase(d.getNodeName()))
				{
					NamedNodeMap attrs = d.getAttributes();
					StatsSet set = new StatsSet();
					
					final ClassId classId = ClassId.VALUES[Integer.parseInt(attrs.getNamedItem("id").getNodeValue())];
					String items = null;
					
					for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
					{
						if ("set".equalsIgnoreCase(cd.getNodeName()))
						{
							attrs = cd.getAttributes();
							String name = attrs.getNamedItem("name").getNodeValue().trim();
							String value = attrs.getNamedItem("val").getNodeValue().trim();
							set.set(name, value);
						}
						else if ("skillTrees".equalsIgnoreCase(cd.getNodeName()))
						{
							List<L2SkillLearn> skills = new ArrayList<>();
							for (Node cb = cd.getFirstChild(); cb != null; cb = cb.getNextSibling())
							{
								L2SkillLearn skillLearn = null;
								if ("skill".equalsIgnoreCase(cb.getNodeName()))
								{
									attrs = cb.getAttributes();
									final int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
									final int lvl = Integer.parseInt(attrs.getNamedItem("lvl").getNodeValue());
									final int minLvl = Integer.parseInt(attrs.getNamedItem("minLvl").getNodeValue());
									final int cost = Integer.parseInt(attrs.getNamedItem("sp").getNodeValue());
									skillLearn = new L2SkillLearn(id, lvl, minLvl, cost, 0, 0);
									skills.add(skillLearn);
								}
							}
							SkillTreeTable.getInstance().addSkillsToSkillTrees(classId, skills);
						}
						else if ("items".equalsIgnoreCase(cd.getNodeName()))
						{
							attrs = cd.getAttributes();
							items = attrs.getNamedItem("val").getNodeValue().trim();
						}
					}
					PlayerTemplate pcT = new PlayerTemplate(classId, set);
					
					// Add items listed in "items" if class possess a filled "items" string.
					if (items != null)
					{
						String[] itemsSplit = items.split(";");
						for (String element : itemsSplit)
							pcT.addItem(Integer.parseInt(element));
					}
					
					_templates.put(pcT.getClassId().getId(), pcT);
				}
			}
		}
		catch (Exception e)
		{
			_log.warn( "CharTemplateTable: Error loading from file: " + f.getName(), e);
		}
	}
	
	public PlayerTemplate getTemplate(ClassId classId)
	{
		return _templates.get(classId.getId());
	}
	
	public PlayerTemplate getTemplate(int classId)
	{
		return _templates.get(classId);
	}
	
	public final String getClassNameById(int classId)
	{
		PlayerTemplate pcTemplate = _templates.get(classId);
		if (pcTemplate == null)
			throw new IllegalArgumentException("No template for classId: " + classId);
		
		return pcTemplate.getClassName();
	}
	
	private static class SingletonHolder
	{
		protected static final CharTemplateTable _instance = new CharTemplateTable();
	}
}