package com.embeddedmeng.common.enums;

/**
 * @Author: MengXiangYu
 * @Description:
 * @Date: Create in 下午6:29 2018/6/15
 */
public enum TokenEnum {

    DEFAULT("0", "默认"),
    SUCCESS("1", "验证通过"),
    TIMEOUT("2", "验证超时"),
    UNPASS("3", "未通过验证"),
    FREQUENTLY("4", "请求过于频繁");

    private String state;
    private String stateInfo;

    TokenEnum(String state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static TokenEnum stateOf(int index) {
        for (TokenEnum state: values()) {
            if (state.getState()==String.valueOf(index)) {
                return state;
            }
        }
        return null;
    }

}
