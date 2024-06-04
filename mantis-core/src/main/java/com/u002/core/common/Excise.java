package com.u002.core.common;

/**
 * <pre>
 * vintage 的 excise 方式，static、dynamic、ratio；
 * static表示使用静态列表，不剔除unreachable的node；dynamic完全剔除；ratio按比例提出。
 * 配置方式，ratio直接使用数字，其他使用数字0-100.
 * </pre>
 *
 * @author fishermen
 */
public enum Excise {
    excise_static("static"), excise_dynamic("dynamic"), excise_ratio("ratio");

    private final String name;

    Excise(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}