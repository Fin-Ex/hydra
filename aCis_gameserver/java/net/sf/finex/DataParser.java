/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package net.sf.finex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author finfan
 */
public class DataParser {

	@Getter private static final DataParser instance = new DataParser();

	private DataParser() {
	}

	/**
	 * Argument contract:<br>
	 * args[0] = Class T[] type<br>
	 *
	 * @param <T> parameter type like Type[].class and etc
	 * @param path
	 * @param args
	 * @return List with given T[] parsed from json (from path)
	 */
	public <T> List<T> parseAndGet(String path, Object... args) {
		final Class<T[]> type = (Class<T[]>) args[0];
		final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		final File file = new File(path);
		if (file.isDirectory()) {
			final List<T> list = new ArrayList<>();
			for (File f : file.listFiles()) {
				try (Reader reader = new FileReader(f)) {
					list.addAll(Arrays.asList(gson.fromJson(reader, type)));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return list;
		} else {
			try (Reader reader = new FileReader(file)) {
				return Arrays.asList(gson.fromJson(reader, type));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
