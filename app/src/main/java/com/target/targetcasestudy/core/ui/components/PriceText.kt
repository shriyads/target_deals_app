package com.target.targetcasestudy.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.target.targetcasestudy.core.ui.theme.DarkRed
import com.target.targetcasestudy.core.ui.theme.DarkestGray
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily


@Composable
fun PriceText(
    salePrice: String,
    regularPrice: String?,
    modifier: Modifier = Modifier,
    salePriceTextStyle: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        color = DarkRed
    ),
    regularPriceTextStyle: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = DarkestGray,
        textDecoration = TextDecoration.LineThrough
    )
) {
    Row(modifier = modifier) {
        Text(
            text = salePrice,
            style = salePriceTextStyle
        )

        if (!regularPrice.isNullOrBlank()) {
            Text(
                text = buildAnnotatedString {
                    append(" reg. ")
                    withStyle(
                        style = SpanStyle(
                            textDecoration = regularPriceTextStyle.textDecoration,
                            fontFamily = regularPriceTextStyle.fontFamily,
                            fontWeight = regularPriceTextStyle.fontWeight,
                            fontSize = regularPriceTextStyle.fontSize,
                            color = regularPriceTextStyle.color
                        )
                    ) {
                        append(regularPrice)
                    }
                }
            )
        }
    }
}