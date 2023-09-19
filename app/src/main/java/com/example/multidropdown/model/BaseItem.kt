package com.example.multidropdown.model

import com.example.multidropdown.utils.ViewType
import java.io.Serializable

data class BaseItem(
    val type: ViewType,
    val heading: String? = null,
    val text: String? = null,
    val imageRes: String? = null,
    val imageUrl: String? = null,
    val isChecked: Boolean? = null
): Serializable
