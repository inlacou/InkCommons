package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.sqrt

class GestureListener(
		private val doubleClickThreshold: Long = 500L,
		private val longClickThreshold: Long = 1000L,
		private val rotateThreshold: Float = 20f,
		val onEvent: (motionEvent: MotionEvent, fingers: Int, doubleClick: Boolean, horizontalMovement: Float?, verticalMovement: Float?, value: Float?, movement: Move) -> Unit,
		val activity: AppCompatActivity? = null
): View.OnTouchListener {
	
	companion object {
		val simpleMovements = Move.values().filter { it.type==MoveType.MOVEMENT }
		val pinchMovements = Move.values().filter { it.type==MoveType.PINCH }
		val rotateMovements = Move.values().filter { it.type==MoveType.ROTATE }
		val clickMovements = Move.values().filter { it.type==MoveType.CLICK }
	}
	
	enum class Move(val type: MoveType? = null) {
		NONE,
		LEFT(MoveType.MOVEMENT), RIGHT(MoveType.MOVEMENT), TOP(MoveType.MOVEMENT), DOWN(MoveType.MOVEMENT),
		LEFT_TOP(MoveType.MOVEMENT), LEFT_DOWN(MoveType.MOVEMENT),
		RIGHT_TOP(MoveType.MOVEMENT), RIGHT_DOWN(MoveType.MOVEMENT),
		PINCH_IN(MoveType.PINCH), PINCH_OUT(MoveType.PINCH),
		ROTATE_CLOCKWISE(MoveType.ROTATE), ROTATE_COUNTER_CLOCKWISE(MoveType.ROTATE),
		CLICK(MoveType.CLICK), LONG_CLICK(MoveType.CLICK)
	}
	
	enum class MoveType {
		MOVEMENT, PINCH, ROTATE, CLICK
	}
	
	private var rotating = false
	private var startingPosition: PointF? = null
	private var startingAngle: Float = 1337f
	private var oldCenter: PointF? = null
	private var currentCenter: PointF? = null
	private var previousTouchPoints: Int = 1
	private var touchPoints = 0
	private var startClickStamp = 0L
	private var doubleClick = false
	private var lastPosition0: PointF? = null
	private var lastPosition1: PointF? = null
	private var movements: MutableList<Move> = mutableListOf()
	
	override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
		if(motionEvent==null) return false
		touchPoints = motionEvent.pointerCount
		when(motionEvent.action) {
			MotionEvent.ACTION_DOWN -> {
				doubleClick = abs(startClickStamp-System.currentTimeMillis())<doubleClickThreshold
				startClickStamp = System.currentTimeMillis()
				startingPosition = motionEvent.getPosition()
				if(touchPoints==2 && startingAngle==1337f) {
					startingAngle = angleBetweenPoints(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1))
				}
				work(motionEvent)
				previousTouchPoints = touchPoints
				oldCenter = currentCenter
			}
			MotionEvent.ACTION_MOVE -> {
				if(touchPoints==2 && startingAngle==1337f) {
					startingAngle = angleBetweenPoints(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1))
				}
				work(motionEvent)
				previousTouchPoints = touchPoints
				oldCenter = currentCenter
			}
			MotionEvent.ACTION_UP -> {
				work(motionEvent)
				previousTouchPoints = touchPoints
				oldCenter = currentCenter
				lastPosition0 = null
				lastPosition1 = null
				rotating = false
				startingAngle = 1337f
				movements = mutableListOf()
			}
		}
		
		return when(motionEvent.action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> true
			else -> false
		}
	}
	
	private fun work(motionEvent: MotionEvent) {
		val lastPos0 = lastPosition0
		val lastPos1 = lastPosition1
		val x0 = motionEvent.getX(0)
		val y0 = motionEvent.getY(0)
		val newPosition0 = PointF(x0, y0)
		lastPosition0 = newPosition0
		currentCenter = if (previousTouchPoints!=touchPoints) null else motionEvent.centerPosition()
		if(lastPos0==null) {
			return
		}
		if(touchPoints==2) {
			val x1 = motionEvent.getX(1)
			val y1 = motionEvent.getY(1)
			val newPosition1 = PointF(x1, y1)
			lastPosition1 = newPosition1
			if(lastPos1==null){
				return
			}else {
				//Basic
				val x0Old = lastPos0.x
				val x1Old = lastPos1.x
				val y0Old = lastPos0.y
				val y1Old = lastPos1.y
				val oldCenterX = (x0Old+x1Old)/2
				val oldCenterY = (y0Old+y1Old)/2
				val newCenterX = (x0+x1)/2
				val newCenterY = (y0+y1)/2
				//Calculate rotation
				val oldAngle = angleBetweenPoints(x0, y0, x1, y1)
				val newAngle = angleBetweenPoints(x0Old, y0Old, x1Old, y1Old)
				val diffAngle = newAngle-oldAngle
				if(rotating || (startingAngle-newAngle).absoluteValue>rotateThreshold) {
					trigger(motionEvent, touchPoints, doubleClick, null, null, diffAngle, if(newAngle>oldAngle) Move.ROTATE_COUNTER_CLOCKWISE else Move.ROTATE_CLOCKWISE)
					rotating = true
				}
				//Calculate pinch
				val distanceBetweenCenters = sqrt((oldCenterX - newCenterX) * (oldCenterX - newCenterX) + (oldCenterY - newCenterY) * (oldCenterY - newCenterY))
				val oldDistanceBetweenFingers = sqrt((x1Old - x0Old) * (x1Old - x0Old) + (y1Old - y0Old) * (y1Old - y0Old))
				val newDistanceBetweenFingers = sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0))
				val diffBetweenFingers = newDistanceBetweenFingers-oldDistanceBetweenFingers
				if(distanceBetweenCenters>abs(diffBetweenFingers)) {
					//Do nothing
				} else {
					when {
						diffBetweenFingers < 0 -> Move.PINCH_OUT
						diffBetweenFingers > 0 -> Move.PINCH_IN
						else -> null
					}?.let {
						trigger(motionEvent, touchPoints, doubleClick, null, null, diffBetweenFingers, it)
						return //Pinch movements are exclusive
					}
				}
			}
		}
		if(oldCenter!=null && currentCenter!=null) {
			if (previousTouchPoints==touchPoints) {
				val horizontalMovement: Float = currentCenter!!.x-oldCenter!!.x
				val verticalMovement: Float = currentCenter!!.y-oldCenter!!.y
				when {
					horizontalMovement == 0f && verticalMovement  < 0f -> Move.TOP
					horizontalMovement == 0f && verticalMovement  > 0f -> Move.DOWN
					horizontalMovement  < 0f && verticalMovement == 0f -> Move.LEFT
					horizontalMovement  > 0f && verticalMovement == 0f -> Move.RIGHT
					horizontalMovement  < 0f && verticalMovement  < 0f -> Move.LEFT_TOP
					horizontalMovement  < 0f && verticalMovement  > 0f -> Move.LEFT_DOWN
					horizontalMovement  > 0f && verticalMovement  > 0f -> Move.RIGHT_DOWN
					horizontalMovement  > 0f && verticalMovement  < 0f -> Move.RIGHT_TOP
					horizontalMovement == 0f && verticalMovement == 0f && System.currentTimeMillis() - startClickStamp > longClickThreshold -> Move.LONG_CLICK
					horizontalMovement == 0f && verticalMovement == 0f -> Move.CLICK
					else -> null
				}?.let {
					trigger(motionEvent, touchPoints, doubleClick, horizontalMovement, verticalMovement, null, it)
				}
			}else{
				trigger(motionEvent, touchPoints, doubleClick, null, null, null, Move.NONE)
			}
		}else{
			trigger(motionEvent, touchPoints, doubleClick, null, null, null, Move.NONE)
		}
		
	}
	
	private fun trigger(motionEvent: MotionEvent, fingers: Int, doubleClick: Boolean, horizontalMovement: Float?, verticalMovement: Float?, value: Float?, movement: Move) {
		movements.add(movement)
		onEvent.invoke(motionEvent, fingers, doubleClick, horizontalMovement, verticalMovement, value, movement)
	}
	
	private fun angleBetweenPoints(a: PointF, b: PointF): Float {
		return angleBetweenPoints(x0 = a.x, y0 = a.y, x1 = b.x, y1 = b.y)
	}
	private fun angleBetweenPoints(x0: Float, y0: Float, x1: Float, y1: Float): Float {
		val deltaY = y1 - y0
		val deltaX = x1 - x0
		return Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
	}
	
}

fun MotionEvent.getPosition() = PointF(x, y)
fun MotionEvent.centerPosition() = (0 until this.pointerCount).map { PointF(getX(it), getY(it)) }.calculateCenter()

private fun List<PointF>.calculateCenter(): PointF {
	val acc = reduce { acc, pointF -> PointF(acc.x+pointF.x, acc.y+pointF.y) }
	val x = acc.x/size
	val y = acc.y/size
	return PointF(x, y)
}