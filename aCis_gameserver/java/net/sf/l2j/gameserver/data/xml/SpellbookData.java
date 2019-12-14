package net.sf.l2j.gameserver.data.xml;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.data.xml.XMLDocument;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.Config;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class loads and stores spellbook / skillId relation.<br>
 * TODO Could be possibly moved back on skillTrees.
 */
public class SpellbookData extends XMLDocument {

	private final Map<Integer, Integer> _books = new HashMap<>();

	protected SpellbookData() {
		load();
	}

	@Override
	protected void load() {
		loadDocument("./data/xml/spellbooks.xml");
		LOG.info("Loaded " + _books.size() + " spellbooks.");
	}

	@Override
	protected void parseDocument(Document doc, File file) {
		// First element is never read.
		final Node n = doc.getFirstChild();

		for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling()) {
			if (!"book".equalsIgnoreCase(o.getNodeName())) {
				continue;
			}

			final NamedNodeMap attrs = o.getAttributes();
			_books.put(Integer.valueOf(attrs.getNamedItem("skillId").getNodeValue()), Integer.valueOf(attrs.getNamedItem("itemId").getNodeValue()));
		}
	}

	public int getBookForSkill(int skillId, int level) {
		if (skillId == L2Skill.SKILL_DIVINE_INSPIRATION) {
			if (!Config.DIVINE_SP_BOOK_NEEDED) {
				return 0;
			}

			switch (level) {
				case 1:
					return 8618; // Ancient Book - Divine Inspiration (Modern Language Version)
				case 2:
					return 8619; // Ancient Book - Divine Inspiration (Original Language Version)
				case 3:
					return 8620; // Ancient Book - Divine Inspiration (Manuscript)
				case 4:
					return 8621; // Ancient Book - Divine Inspiration (Original Version)
				default:
					return 0;
			}
		}

		if (level != 1) {
			return 0;
		}

		if (!Config.SP_BOOK_NEEDED) {
			return 0;
		}

		if (!_books.containsKey(skillId)) {
			return 0;
		}

		return _books.get(skillId);
	}

	public static SpellbookData getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		protected static final SpellbookData INSTANCE = new SpellbookData();
	}
}
