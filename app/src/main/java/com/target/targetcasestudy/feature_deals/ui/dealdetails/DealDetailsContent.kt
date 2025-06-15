package com.target.targetcasestudy.feature_deals.ui.dealdetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.components.GenericImage
import com.target.targetcasestudy.core.ui.components.PriceText
import com.target.targetcasestudy.core.ui.theme.Black
import com.target.targetcasestudy.core.ui.theme.DarkGray
import com.target.targetcasestudy.core.ui.theme.Dimens
import com.target.targetcasestudy.core.ui.theme.Green
import com.target.targetcasestudy.core.ui.theme.LightGray
import com.target.targetcasestudy.core.ui.theme.PrimaryRed
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily
import com.target.targetcasestudy.core.ui.theme.TextPrimary
import com.target.targetcasestudy.feature_deals.domain.model.Deals


@Composable
fun DealDetailsContent(
    deal: Deals,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    var showSeeMore by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = { Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()},
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryRed),
                shape = RoundedCornerShape(Dimens.PaddingSmall),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.height_l)
                    .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.Padding_10)
            ) {
                Text(
                    text = stringResource(R.string.add_to_cart),
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.TextMedium
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp) // Padding to prevent overlap with bottom bar
        ) {
            GenericImage(
                imageUrl = deal.imageUrl,
                contentDescription = deal.title,
                modifier = Modifier
                    .padding(top = Dimens.PaddingMedium, start = Dimens.PaddingMedium, end = Dimens.PaddingMedium)
                    .height(328.dp)
                    .shadow(6.dp, RoundedCornerShape(Dimens.RadiusMedium)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(25.dp))

            Text(
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium),
                text = deal.title,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Black
                )
            )

            Spacer(Modifier.height(8.dp))

            PriceText(
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium),
                salePrice = deal.salePrice,
                regularPrice = deal.regularPrice,
                salePriceTextStyle = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = PrimaryRed
                ),
                regularPriceTextStyle = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = DarkGray,
                    textDecoration = TextDecoration.LineThrough
                )
            )

            Spacer(Modifier.height(5.dp))

            Text(
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium),
                text = deal.fulfillment,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = DarkGray
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium),
                text = deal.availability,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Green
                )
            )

            Spacer(Modifier.height(16.dp))

            Divider(
                color = LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            Text(
                text = stringResource(R.string.product_details_title),
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                ),
                modifier = Modifier.padding(top = Dimens.PaddingMedium, start = Dimens.PaddingMedium)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = deal.description,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = DarkGray,
                    lineHeight = 24.sp
                ),
                modifier = Modifier
                    .padding(horizontal = Dimens.PaddingMedium)
                    .background(Color.White),
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (result.didOverflowHeight) {
                        showSeeMore = true
                    }
                }
            )

            if (showSeeMore) {
                val seeMoreLessText = if (expanded) {
                    stringResource(id = R.string.see_less_title)
                } else {
                    stringResource(id = R.string.see_more_title)
                }

                Text(
                    text = seeMoreLessText,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PrimaryRed
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.MarginSmall)
                        .clickable { expanded = !expanded }
                )
            } else {
                Spacer(Modifier.height(Dimens.PaddingMedium))
            }

        }
    }
}


