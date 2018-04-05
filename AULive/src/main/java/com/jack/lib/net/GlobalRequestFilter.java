/**
 * @Title: GlobalRequestFilter.java
 * @Package com.stay.lib.net
 * @Description: TODO
 * @author jack.long
 * @date 2013-10-20 下午10:38:59
 * @version
 */
package com.jack.lib.net;

import java.util.HashMap;

/**
 * @ClassName: GlobalRequestFilter
 * @Description: 过滤信息
 * @author jack.long
 * @date 2013-10-20 下午10:38:59
 *
 */
public interface GlobalRequestFilter {

	HashMap<String, String> filterHeader();

}

