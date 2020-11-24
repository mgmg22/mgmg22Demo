package com.google.mgmg22demo.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.lang.Math.*
import java.util.*

/**
 * @Author shenxiaoshun
 * @Date 2020/10/9
 */
class DimpleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //定义一个粒子的集合
    private var particleList = mutableListOf<Particle>()

    //定义画笔
    var paint = Paint()
    var centerX = 0f
    var centerY = 0f
    var path = Path()
    val random = Random()
    private val pathMeasure = PathMeasure()//路径，用于测量扩散圆某一处的X,Y值
    private var pos = FloatArray(2) //扩散圆上某一点的x,y
    private val tan = FloatArray(2)//扩散圆上某一点切线

    private var animator = ValueAnimator.ofFloat(0f, 1f)

    init {
        animator.duration = 2000
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateParticle(it.animatedValue as Float)
            invalidate()//重绘界面
        }
    }

    private fun updateParticle(value: Float) {
        particleList.forEach { particle ->
            if (particle.offset > particle.maxOffset) {
                particle.offset = 0f
                particle.speed = (random.nextInt(10) + 5).toFloat()
            }
            particle.alpha = ((1f - particle.offset / particle.maxOffset) * 225f).toInt()
            particle.x = (centerX + cos(particle.angle) * (280f + particle.offset)).toFloat()
            if (particle.y > centerY) {
                particle.y = (sin(particle.angle) * (280f + particle.offset) + centerY).toFloat()
            } else {
                particle.y = (centerY - sin(particle.angle) * (280f + particle.offset)).toFloat()
            }
            particle.offset += particle.speed.toInt()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        particleList.forEach {
            //设置画笔的透明度
            paint.alpha = it.alpha
            canvas.drawCircle(it.x, it.y, it.radius, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        path.addCircle(centerX, centerY, 280f, Path.Direction.CCW)
        pathMeasure.setPath(path, false)
        var nextX = 0f
        var speed=0f
        var nextY=0f
        var angle=0.0
        var offSet=0
        var maxOffset=0
        for (i in 0..2000) {
            pathMeasure.getPosTan(i / 2000f * pathMeasure.length, pos, tan)
            nextX = pos[0]+random.nextInt(6) - 3f
            nextY=  pos[1]+random.nextInt(6) - 3f
            angle=acos(((pos[0] - centerX) / 280f).toDouble())
            speed= random.nextInt(2) + 2f
            offSet = random.nextInt(200)
            maxOffset = random.nextInt(200)
            particleList.add(
                Particle(
                    nextX,
                    nextY,
                    2f,
                    speed,
                    100,
                    offSet.toFloat(),
                    angle,
                    maxOffset.toFloat()
                )
            )
        }
        animator.start()
    }

}
