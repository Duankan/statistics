package com.dankin.base;

/**
 * @author dankin
 * @date 2019-02-26
 * @descr 响应枚举
 */
public enum ResponseEnum {
    FAIL("500", "分析失败", ""),
    NOLEGAL("501","参数格式有误",""),
    SAVEPOLYFAIL("502","保存坐标文件失败",""),
    SAVEPNGFAIL("503","保存图片失败",""),
    SUCCESS("200", "分析成功", "");
    private String code;
    private String displayName;
    private String desc;

    private ResponseEnum(String code, String displayName, String desc) {
        this.code = code;
        this.displayName = displayName;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
