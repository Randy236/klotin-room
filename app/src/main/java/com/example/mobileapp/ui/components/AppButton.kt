package com.example.mobileapp.ui.components

/*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobileapp.ui.theme.*

enum class ButtonType {
    Filled,
    Outlined
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Filled,
    backgroundColor: Color = Primary,
    contentColor: Color = Color.White,
    borderColor: Color = Primary,
    cornerRadius: Dp = 18.dp
) {
    val shape = RoundedCornerShape(cornerRadius)

    when (type) {
        ButtonType.Filled -> {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                shape = shape,
                modifier = modifier
            ) {
                Text(text = text, color = contentColor)
            }
        }

        ButtonType.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                shape = shape,
                border = BorderStroke(1.dp, borderColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = borderColor),
                modifier = modifier
            ) {
                Text(text = text)
            }
        }
    }
}
*/