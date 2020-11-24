package com.example.wyapplication.widget

/**
 * @Author shenxiaoshun
 * @Date 2020/10/9
 */
class Particle(
    var x:Float,//X坐标
    var y:Float,//Y坐标
    var radius:Float,//半径
    var speed:Float,//速度
    var alpha: Int, //透明度
    var offset:Float,//当前移动距离
    var angle:Double,//粒子角度
    var maxOffset:Float=300f//最大移动距离
)