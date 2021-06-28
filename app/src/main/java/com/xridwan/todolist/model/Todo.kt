package com.xridwan.todolist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: String? = null,
    val title: String? = null,
    val desc: String? = null
) : Parcelable