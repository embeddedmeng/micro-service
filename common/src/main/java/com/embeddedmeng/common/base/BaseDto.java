package com.embeddedmeng.common.base;

import java.util.List;
import java.util.Map;

public class BaseDto {

    public String code;
    public String state;
    public String message;
    public Map<String, Object> data;
    public List<Object> lists;

    /**
     * 增删改操作返回规范
     * @param code 服务器状态码
     * @param state 操作状态码
     * @param message 提示信息
     */
    public BaseDto(String code, String state, String message) {
        this.code = code;
        this.state = state;
        this.message = message;
    }
    
    /**
     * 查询返回Map规范
     * @param code 服务器状态码
     * @param state 操作状态码
     * @param message 提示信息
     * @param data 返回的Map
     */
    public BaseDto(String code, String state, String message, Map<String, Object> data) {
    		this.code = code;
        this.state = state;
        this.message = message;
        this.data = data;
	}
    
    /**
     * 查询返回List规范
     * @param code 服务器状态码
     * @param state 操作状态码
     * @param message 提示信息
     * @param lists 返回的Lists
     */
    public BaseDto(String code, String state, String message, List<Object> lists) {
		this.code = code;
	    this.state = state;
	    this.message = message;
	    this.lists = lists;
    }

    @Override
	public String toString() {
		return "BaseDto [code=" + code + ", state=" + state + ", message=" + message + ", data=" + data + ", lists="
				+ lists + "]";
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public List<Object> getLists() {
		return lists;
	}

	public void setLists(List<Object> lists) {
		this.lists = lists;
	}
    
}
