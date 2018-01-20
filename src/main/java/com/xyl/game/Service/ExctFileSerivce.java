package com.xyl.game.Service;

import java.io.InputStream;

/**
 * 用解析exct表格
 * @author dazhi
 */
public interface ExctFileSerivce {
	public void savaDataForExct(InputStream exctFileStream) throws Exception;
}
