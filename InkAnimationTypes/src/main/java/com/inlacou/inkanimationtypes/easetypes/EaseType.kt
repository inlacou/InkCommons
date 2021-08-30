package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.Interpolable

/**
 * Created by Weiping on 03/03/2016.
 * Updated by Inlacou on 27/11/2019.
 * Visual representation here: https://easings.net/en
 */
enum class EaseType

/**
 * ease animation helps to make the movement more real
 * @param easingType
 */
constructor(private val easingType: Class<out Interpolable>) {
	None(NoEase::class.java),

	EaseInSine(com.inlacou.inkanimationtypes.easetypes.EaseInSine::class.java),
	EaseOutSine(com.inlacou.inkanimationtypes.easetypes.EaseOutSine::class.java),
	EaseInOutSine(com.inlacou.inkanimationtypes.easetypes.EaseInOutSine::class.java),

	EaseInQuad(com.inlacou.inkanimationtypes.easetypes.EaseInQuad::class.java),
	EaseOutQuad(com.inlacou.inkanimationtypes.easetypes.EaseOutQuad::class.java),
	EaseInOutQuad(com.inlacou.inkanimationtypes.easetypes.EaseInOutQuad::class.java),

	EaseInCubic(com.inlacou.inkanimationtypes.easetypes.EaseInCubic::class.java),
	EaseOutCubic(com.inlacou.inkanimationtypes.easetypes.EaseOutCubic::class.java),
	EaseInOutCubic(com.inlacou.inkanimationtypes.easetypes.EaseInOutCubic::class.java),

	EaseInQuart(com.inlacou.inkanimationtypes.easetypes.EaseInQuart::class.java),
	EaseOutQuart(com.inlacou.inkanimationtypes.easetypes.EaseOutQuart::class.java),
	EaseInOutQuart(com.inlacou.inkanimationtypes.easetypes.EaseInOutQuart::class.java),

	EaseInQuint(com.inlacou.inkanimationtypes.easetypes.EaseInQuint::class.java),
	EaseOutQuint(com.inlacou.inkanimationtypes.easetypes.EaseOutQuint::class.java),
	EaseInOutQuint(com.inlacou.inkanimationtypes.easetypes.EaseInOutQuint::class.java),

	EaseInExpo(com.inlacou.inkanimationtypes.easetypes.EaseInExpo::class.java),
	EaseOutExpo(com.inlacou.inkanimationtypes.easetypes.EaseOutExpo::class.java),
	EaseInOutExpo(com.inlacou.inkanimationtypes.easetypes.EaseInOutExpo::class.java),

	EaseInCirc(com.inlacou.inkanimationtypes.easetypes.EaseInCirc::class.java),
	EaseOutCirc(com.inlacou.inkanimationtypes.easetypes.EaseOutCirc::class.java),
	EaseInOutCirc(com.inlacou.inkanimationtypes.easetypes.EaseInOutCirc::class.java),

	EaseInBack(com.inlacou.inkanimationtypes.easetypes.EaseInBack::class.java),
	EaseOutBack(com.inlacou.inkanimationtypes.easetypes.EaseOutBack::class.java),
	EaseInOutBack(com.inlacou.inkanimationtypes.easetypes.EaseInOutBack::class.java),

	EaseInElastic(com.inlacou.inkanimationtypes.easetypes.EaseInElastic::class.java),
	EaseOutElastic(com.inlacou.inkanimationtypes.easetypes.EaseOutElastic::class.java),
	EaseInOutElastic(com.inlacou.inkanimationtypes.easetypes.EaseInOutElastic::class.java),

	EaseInBounce(com.inlacou.inkanimationtypes.easetypes.EaseInBounce::class.java),
	EaseOutBounce(com.inlacou.inkanimationtypes.easetypes.EaseOutBounce::class.java),
	EaseInOutBounce(com.inlacou.inkanimationtypes.easetypes.EaseInOutBounce::class.java),

	Linear(com.inlacou.inkanimationtypes.easetypes.Linear::class.java);
	
	fun newInstance() = easingType.newInstance()
}
