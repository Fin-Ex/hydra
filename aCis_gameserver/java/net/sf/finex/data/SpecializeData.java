/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import org.slf4j.LoggerFactory;

import net.sf.finex.enums.ECraftSpec;
import lombok.Data;

/**
 *
 * @author FinFan
 */
@Data
public class SpecializeData {

	private final ECraftSpec specialize;
	private int exp;
	private int lvl = 1;
	private int fails, succesess;
}
