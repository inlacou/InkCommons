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

	EaseInSine(EaseInSine::class.java),
	EaseOutSine(EaseOutSine::class.java),
	EaseInOutSine(EaseInOutSine::class.java),

	EaseInQuad(EaseInQuad::class.java),
	EaseOutQuad(EaseOutQuad::class.java),
	EaseInOutQuad(EaseInOutQuad::class.java),

	EaseInCubic(EaseInCubic::class.java),
	EaseOutCubic(EaseOutCubic::class.java),
	EaseInOutCubic(EaseInOutCubic::class.java),

	EaseInQuart(EaseInQuart::class.java),
	EaseOutQuart(EaseOutQuart::class.java),
	EaseInOutQuart(EaseInOutQuart::class.java),

	EaseInQuint(EaseInQuint::class.java),
	EaseOutQuint(EaseOutQuint::class.java),
	EaseInOutQuint(EaseInOutQuint::class.java),

	EaseInExpo(EaseInExpo::class.java),
	EaseOutExpo(EaseOutExpo::class.java),
	EaseInOutExpo(EaseInOutExpo::class.java),

	EaseInCirc(EaseInCirc::class.java),
	EaseOutCirc(EaseOutCirc::class.java),
	EaseInOutCirc(EaseInOutCirc::class.java),

	EaseInBack(com.inlacou.inkanimationtypes.easetypes.EaseInBack::class.java),
	EaseOutBack(EaseOutBack::class.java),
	EaseInOutBack(EaseInOutBack::class.java),

	EaseInElastic(EaseInElastic::class.java),
	EaseOutElastic(EaseOutElastic::class.java),
	EaseInOutElastic(EaseInOutElastic::class.java),

	EaseInBounce(EaseInBounce::class.java),
	EaseOutBounce(EaseOutBounce::class.java),
	EaseInOutBounce(EaseInOutBounce::class.java),

	Linear(com.inlacou.inkanimationtypes.easetypes.Linear::class.java);
	
	fun newInstance() = easingType.newInstance()
}
