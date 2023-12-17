package com.anafthdev.comdeo.foundation.extension

import com.google.gson.Gson

fun Any.toJson(): String = Gson().toJson(this)
