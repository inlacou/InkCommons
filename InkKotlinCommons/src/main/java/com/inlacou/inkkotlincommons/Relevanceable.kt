package com.inlacou.inkkotlincommons

interface Relevanceable {
	val algorithm: (now: Long, item: Long) -> Float
    /**
     * Timestamp of each usage instance.
     */
	val usages: MutableList<Long>
    /**
     * Relevance of the item.
     */
	val relevance: Float
		get() {
			val now = System.currentTimeMillis()

			return if(usages.isEmpty()) 0f else usages.map { usage ->
				//Apply algorithm to each usage
				algorithm(now, usage)
			}.reduce { acc, usageRelevance -> acc+usageRelevance }
		}

	companion object {
		private const val SECOND = 1000L
		private const val HALF_MINUTE = SECOND * 30
		private const val MINUTE = SECOND * 60
		private const val HOUR = MINUTE * 60
		private const val DAY = HOUR * 24
		private const val WEEK = DAY * 7
		private const val MONTH = DAY * 30
		private const val YEAR = DAY * 365
		
		val ALGORITHM_1: (now: Long, item: Long) -> Float = { now: Long, item: Long ->
			var last = now - MINUTE
			var multiplier = 1f
			while (last > item && multiplier>0.0001f) {
				multiplier /= when {
					last < now - MINUTE -> 3f
					last < now - HOUR -> 10f
					last < now - DAY -> 25f
					last < now - WEEK -> 50f
					last < now - MONTH -> 100f
					last < now - YEAR -> 1000f
					else -> 1.5f
				}
				last -= when {
					last < now - MINUTE -> MINUTE
					last < now - HOUR -> HOUR
					last < now - DAY -> DAY
					last < now - WEEK -> WEEK
					last < now - MONTH -> MONTH
					last < now - YEAR -> YEAR
					else -> HALF_MINUTE
				}
			}
			multiplier
		}

        val ALGORITHM_1_1: (now: Long, usageTimestamp: Long) -> Float = { now, timestamp ->
            val age = now - timestamp
            when {
                age < MINUTE -> 1f
                age < HOUR -> 1f / 3f
                age < DAY -> 1f / 10f
                age < WEEK -> 1f / 25f
                age < MONTH -> 1f / 50f
                age < YEAR -> 1f / 100f
                else -> 0f
            }
        }

        /**
         * Exponential decay alternative (smoother)
         */
        val ALGORITHM_EXPONENTIAL: (now: Long, usageTimestamp: Long) -> Float = { now, timestamp ->
            val age = now - timestamp
            val halfLife = DAY * 7  // Relevance halves every week
            kotlin.math.exp(-(age.toDouble() / halfLife) * kotlin.math.ln(2.0)).toFloat()
        }
	}
}