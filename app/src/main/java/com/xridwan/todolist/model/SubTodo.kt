package com.xridwan.todolist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubTodo(
    val subId: String? = null,
    val subTitle: String? = null,
    val subDesc: String? = null
) : Parcelable