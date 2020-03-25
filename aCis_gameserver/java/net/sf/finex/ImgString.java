/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

import lombok.Data;

/**
 *
 * @author finfan
 */
@Data
public class ImgString {

	protected final int width, height;
	protected final String path;

	public String buildHtmlTEG() {
		if(path == null) {
			return "error.empty_path";
		}
		
		return "<img src=\"" + path + "\" width=" + width + " height" + height + "/>";
	}
}
