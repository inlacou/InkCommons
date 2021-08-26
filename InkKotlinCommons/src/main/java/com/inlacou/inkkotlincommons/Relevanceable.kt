package com.inlacou.inkkotlincommons

interface Relevanceable {
	val algorithm: (now: Long, item: Long) -> Float
	val usages: MutableList<Long>
	val relevance: Float
		get() {
			val now = System.currentTimeMillis()
			
			return usages.map { usage ->
				//Apply algorithm to each usage
				algorithm(now, usage)
			}.reduce { acc, usageRelevance -> acc+usageRelevance }
		}

	companion object {
		private const val SECOND = 1000L
		private const val HALF_MINUTE = SECOND *30
		private const val MINUTE = SECOND *60
		private const val HALF_HOUR = MINUTE *30
		private const val HOUR = MINUTE *60
		private const val DAY = HOUR *24
		private const val WEEK = DAY *7
		private const val MONTH = WEEK *30
		private const val YEAR = MONTH *12
		
		val ALGORITHM_1: (now: Long, item: Long) -> Float = { now: Long, item: Long ->
			var last = now- MINUTE
			var multiplier = 1f

			while (last>item && multiplier>0.0001f) {
				multiplier /= when {
					last<now- MINUTE -> 3f
					last<now- HALF_HOUR -> 5f
					last<now- HOUR -> 10f
					last<now- DAY -> 25f
					last<now- WEEK -> 50f
					last<now- MONTH -> 100f
					last<now- YEAR -> 1000f
					else -> 1.5f
				}
				last -= when {
					last<now- MINUTE -> MINUTE
					last<now- HALF_HOUR -> HALF_HOUR
					last<now- HOUR -> HOUR
					last<now- DAY -> DAY
					last<now- WEEK -> WEEK
					last<now- MONTH -> MONTH
					last<now- YEAR -> YEAR
					else -> HALF_MINUTE
				}
			}

			multiplier
		}
		
	}
	
}