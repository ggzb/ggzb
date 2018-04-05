package com.ilikezhibo.ggzb.listener;

import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonWriter;
import java.io.IOException;

/**
 * @ClassName: IJSON
 * @Description: TODO
 * @author jack.long
 * @date 2014-3-27 下午3:38:16
 * 
 */
public interface IJSON {
	void writeToJson(JsonWriter writer);

	boolean readFromJson(JsonReader reader, String tag) throws IOException;
}
