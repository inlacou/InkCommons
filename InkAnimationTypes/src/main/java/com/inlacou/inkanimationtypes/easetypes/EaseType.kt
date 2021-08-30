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
	None(com.inlacou.inkswitch.animations.easetypes.NoEase::class.java),

	EaseInSine(com.inlacou.inkswitch.animations.easetypes.EaseInSine::class.java),
	EaseOutSine(com.inlacou.inkswitch.animations.easetypes.EaseOutSine::class.java),
	EaseInOutSine(com.inlacou.inkswitch.animations.easetypes.EaseInOutSine::class.java),

	EaseInQuad(com.inlacou.inkswitch.animations.easetypes.EaseInQuad::class.java),
	EaseOutQuad(com.inlacou.inkswitch.animations.easetypes.EaseOutQuad::class.java),
	EaseInOutQuad(com.inlacou.inkswitch.animations.easetypes.EaseInOutQuad::class.java),

	EaseInCubic(com.inlacou.inkswitch.animations.easetypes.EaseInCubic::class.java),
	EaseOutCubic(com.inlacou.inkswitch.animations.easetypes.EaseOutCubic::class.java),
	EaseInOutCubic(com.inlacou.inkswitch.animations.easetypes.EaseInOutCubic::class.java),

	EaseInQuart(com.inlacou.inkswitch.animations.easetypes.EaseInQuart::class.java),
	EaseOutQuart(com.inlacou.inkswitch.animations.easetypes.EaseOutQuart::class.java),
	EaseInOutQuart(com.inlacou.inkswitch.animations.easetypes.EaseInOutQuart::class.java),

	EaseInQuint(com.inlacou.inkswitch.animations.easetypes.EaseInQuint::class.java),
	EaseOutQuint(com.inlacou.inkswitch.animations.easetypes.EaseOutQuint::class.java),
	EaseInOutQuint(com.inlacou.inkswitch.animations.easetypes.EaseInOutQuint::class.java),

	EaseInExpo(com.inlacou.inkswitch.animations.easetypes.EaseInExpo::class.java),
	EaseOutExpo(com.inlacou.inkswitch.animations.easetypes.EaseOutExpo::class.java),
	EaseInOutExpo(com.inlacou.inkswitch.animations.easetypes.EaseInOutExpo::class.java),

	EaseInCirc(com.inlacou.inkswitch.animations.easetypes.EaseInCirc::class.java),
	EaseOutCirc(com.inlacou.inkswitch.animations.easetypes.EaseOutCirc::class.java),
	EaseInOutCirc(com.inlacou.inkswitch.animations.easetypes.EaseInOutCirc::class.java),

	EaseInBack(com.inlacou.inkanimationtypes.easetypes.EaseInBack::class.java),
	EaseOutBack(com.inlacou.inkswitch.animations.easetypes.EaseOutBack::class.java),
	EaseInOutBack(com.inlacou.inkswitch.animations.easetypes.EaseInOutBack::class.java),

	EaseInElastic(com.inlacou.inkswitch.animations.easetypes.EaseInElastic::class.java),
	EaseOutElastic(com.inlacou.inkswitch.animations.easetypes.EaseOutElastic::class.java),
	EaseInOutElastic(com.inlacou.inkswitch.animations.easetypes.EaseInOutElastic::class.java),

	EaseInBounce(com.inlacou.inkswitch.animations.easetypes.EaseInBounce::class.java),
	EaseOutBounce(com.inlacou.inkswitch.animations.easetypes.EaseOutBounce::class.java),
	EaseInOutBounce(com.inlacou.inkswitch.animations.easetypes.EaseInOutBounce::class.java),

	Linear(com.inlacou.inkanimationtypes.easetypes.Linear::class.java);
	
	fun newInstance() = easingType.newInstance()
}
