package com.mrra.entity

enum class OperationMode(val msg: String) {

    // 初始态
    INITIAL("正初始化"),

    // 未连接
    DISCONNECTED("未连接"),

    // 主动
    INITIATIVE("主动模式"),

    // 被动
    PASSIVE("被动模式"),

    // 记忆
    MEMORY("记忆模式"),

    // 自由
    FREE("自由模式"),

    // 未在运行
    WAITING("未在运行"),

    // 错误
    ERROR("错误")

}