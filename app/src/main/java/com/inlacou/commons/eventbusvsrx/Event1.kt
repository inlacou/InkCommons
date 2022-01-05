package com.inlacou.commons.eventbusvsrx

import com.inlacou.inkkotlinextensions.rx.EventBusChannel

data class Event1(val s: String): EventBusChannel.Event
