package com.embeddedmeng.common.enums;

// 数据操作返回结果枚举

public enum  StateEnum {
    SUCCESS("0", "执行成功"),
    FAIL("1", "执行失败"),
    WITHOUT("2", "未查询到"),
    REPEAT("-1", "重复操作"),
    INNER_ERROR("-2","系统异常"),
    DATA_REWRITE("-3", "数据篡改");

    private String state;
    private String stateInfo;

    StateEnum(String state, String stateInfo) {
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

    public static StateEnum stateOf(int index) {
        for (StateEnum state: values()) {
            if (state.getState()==String.valueOf(index)) {
                return state;
            }
        }
        return null;
    }
}
