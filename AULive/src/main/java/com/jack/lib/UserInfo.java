package com.jack.lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String USER_INFO = "user_info";
	public static final String MODULE_TARGET_CLASS = "module_target_class";
	public static final String MODULE_TARGET_TITILE = "module_target_title";
	public static final String MODULE_TARGET_DETAIL = "module_target_detail";
	public static final String MODULE_TARGET_DTO_CLASS = "module_target_dto_class";
	public static final String GO_MODULE_TYPE_STRING = "module_type";
	public static final int GO_MODULE_TYPE_PICK = 0;
	public static final int GO_MODULE_TYPE_SHOW = 1;
	public static final int GO_MODULE_TYPE_CREATE = 2;
	public static final int GO_MODULE_TYPE_EDIT = 3;
	public static final String USER_INFO_FRAGMENTS_ARGS = "user_info_fragments_args";
	public static final String MODULE_FRAGMENT_TYPE_CLASS = "module_fragment_type_class";
	public static final String MODULE_LIST_SORT_EXPRESS = "module_list_sort";
	public static final String MODULE_LIST_FILTER_EXPRESS = "module_list_filter";
	public static final String MODULE_LIST_TITLE = "module_list_indicator_name";
	public static final String MODULE_PICK_BIND_ARGS = "module_pick_bind_args";
	public static final String SYNC_DTO_ERRORS = "sync_dto_errors";
	public static final String CLOUD_RESPONSE_BODY = "cloud_response_body";
	public static final String CLOUD_RESPONSE_STATUS_CODE = "cloud_response_status_code";
	public static final String EDITOR_TITLE = "text_editor_title";
	public static final String EDITOR_LIMIT_NUM = "editor_limit_num";
	public static final String EDITOR_VALUE_TYPE = "editor_value_type";
	public static final String TEXTEDITOR_EDITORTYPE = "text_editor_type";
	
	public static final String NUMEDITOR_EDITORTYPE = "num_editor_type";
	public static final String NUMEDITOR_NUMS_AFTER_SEPARATOR = "num_editor_nums_after_separator";
	public static final String NUMEDITOR_NUMS_ALLOWED_CHARACTERS = "num_editor_nums_allowed_characters";
	public static final String NUMEDITOR_MAX_VALUE = "num_editor_max_value";
	public static final String NUMEDITOR_MINI_VALUE = "num_editor_mini_value";
	
	public static final String DATEEDITOR_MAX_VALUE= "date_editor_max_value";
	public static final String DATEEDITOR_MINI_VALUE = "date_editor_mini_value";
	
	public static final String TEXTARRAY_PICKER_ARGS = "text_array_picker_args";
	
	public static final String DATEEDITOR_VALUE_TYPE = "date_editor_type";
	
	
	

	private HashMap<String, Object> mInfoMap;

	public UserInfo() {
		mInfoMap = new HashMap<String, Object>();
	}

	public UserInfo(HashMap<String, Object> map) {
		mInfoMap = map;
	}

	public void putInfo(String key, Object value) {
		mInfoMap.put(key, value);
	}

	public void removeInfo(String key) {
		mInfoMap.remove(key);
	}

	public ArrayList getUserInfoArrayList(String key) {
		return containsKey(key) ? (ArrayList) mInfoMap.get(key) : null;
	}

	public void clear() {
		mInfoMap.clear();
	}

	public boolean containsKey(String key) {
		return mInfoMap.containsKey(key);
	}

	public Object getInfoValue(String key) {
		return mInfoMap.get(key);
	}

	public String getString(String key) {
		return mInfoMap.containsKey(key) ? (String) mInfoMap.get(key) : null;
	}

	public int getInt(String key) {
		return mInfoMap.containsKey(key) ? (Integer) mInfoMap.get(key) : 0;
	}

	public Class getObjectClass(String key) {
		return mInfoMap.containsKey(key) ? (Class) mInfoMap.get(key) : null;
	}

	public String[] getStringArray(String key) {
		return mInfoMap.containsKey(key) ? (String[]) mInfoMap.get(key) : null;
	}

	public boolean getBoolean(String key) {
		return mInfoMap.containsKey(key) ? (Boolean) mInfoMap.get(key) : false;
	}

	public HashMap<String, Object> getUserInfo() {
		return mInfoMap;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : mInfoMap.entrySet()) {
			sb.append(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		return sb.toString();
	}

}
