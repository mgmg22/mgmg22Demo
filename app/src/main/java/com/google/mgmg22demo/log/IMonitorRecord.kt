package com.google.mgmg22demo.log

interface IMonitorRecord {
    /**
     * 向Monitor悬浮窗中添加性能记录
     *
     * @param tvName      名称
     * @param tvNum       数值
     * @param isGoodValue 该性能参数的好坏，在悬浮窗中通过颜色来区分
     */
    fun addOneRecord(
        tvName: String?,
        tvNum: String?,
        isGoodValue: Boolean
    )
}