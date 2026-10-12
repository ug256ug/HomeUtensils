package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.WarmCreamSurface

@Composable
fun UtensilThumbnail(
    imageResName: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Household Utensil"
) {
    val context = LocalContext.current
    val resId = rememberDrawableResId(imageResName, context)

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier.background(WarmCreamSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Kitchen,
                contentDescription = contentDescription,
                tint = DeepTeal,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
private fun rememberDrawableResId(name: String, context: android.content.Context): Int {
    return androidx.compose.runtime.remember(name) {
        val sanitized = name.substringBeforeLast(".")
        val id = context.resources.getIdentifier(sanitized, "drawable", context.packageName)
        if (id != 0) id else {
            // Fallback to hero banner or kettle if not found
            val fallback = context.resources.getIdentifier("hero_utensils_banner_1790683214191", "drawable", context.packageName)
            if (fallback != 0) fallback else R.drawable.ic_launcher_foreground
        }
    }
}
