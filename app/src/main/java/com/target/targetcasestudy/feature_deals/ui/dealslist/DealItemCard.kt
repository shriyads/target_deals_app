package com.target.targetcasestudy.feature_deals.ui.dealslist


// Imports for Button
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.widget.Guideline
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.components.GenericImage
import com.target.targetcasestudy.core.ui.components.PriceText
import com.target.targetcasestudy.core.ui.theme.DarkGray
import com.target.targetcasestudy.core.ui.theme.Dimens.GuidelineLarge
import com.target.targetcasestudy.core.ui.theme.Dimens.GuidelineSmall
import com.target.targetcasestudy.core.ui.theme.Dimens.MarginSmall
import com.target.targetcasestudy.core.ui.theme.Dimens.PaddingSmall
import com.target.targetcasestudy.core.ui.theme.Dimens.Padding_10
import com.target.targetcasestudy.core.ui.theme.Dimens.Padding_20
import com.target.targetcasestudy.core.ui.theme.Dimens.RadiusMedium
import com.target.targetcasestudy.core.ui.theme.Dimens.TextSmall
import com.target.targetcasestudy.core.ui.theme.Dimens.Text_12
import com.target.targetcasestudy.core.ui.theme.Green
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily
import com.target.targetcasestudy.feature_deals.domain.model.Deals


@Composable
fun DealItemCard(
    deal: Deals,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit // Pass the ID for navigation
) {
    ConstraintLayout(
        modifier = modifier
            .clickable { onClick(deal.id.toString()) } // Handle click event
            .padding(vertical = PaddingSmall) // Add some vertical padding for better spacing around the button
    ) {
        val (imageRef, aisleRef, availabilityRef, fulfillmentRef, titleRef, priceRef, addToCartButtonRef) = createRefs()
        // Guideline to align text content beside the image
        val guideline = createGuidelineFromStart(GuidelineLarge + GuidelineSmall)

        // Deal Image using the new reusable AppAsyncImage
        GenericImage(
            imageUrl = deal.imageUrl,
            contentDescription = deal.title,
            modifier = Modifier
                .size(150.dp) // Specific size for this card
                .shadow(4.dp, RoundedCornerShape(RadiusMedium)) // Apply shadow
                .constrainAs(imageRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            // contentScale defaults to Crop
            // placeholder and error images use the defaults provided in AppAsyncImage
        )

        // Prices using the new reusable PriceText composable
        PriceText(
            salePrice = deal.salePrice,
            regularPrice = deal.regularPrice,
            modifier = Modifier.constrainAs(priceRef) {
                top.linkTo(parent.top)
                start.linkTo(guideline)
            }
        )

        // Fulfillment info
        Text(
            text = deal.fulfillment,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = Text_12,
                color = DarkGray
            ),
            modifier = Modifier.constrainAs(fulfillmentRef) {
                start.linkTo(guideline)
                top.linkTo(priceRef.bottom, margin = 5.dp)
            }
        )

        // Deal Title
        Text(
            text = deal.title,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = TextSmall
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(guideline)
                top.linkTo(fulfillmentRef.bottom, margin = MarginSmall)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        // Availability info
        Text(
            text = deal.availability,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = Text_12,
                color = Green
            ),
            modifier = Modifier.constrainAs(availabilityRef) {
                start.linkTo(guideline)
                top.linkTo(titleRef.bottom, margin = MarginSmall)
            }
        )

        // Aisle info
        Text(
            text = "in aisle ${deal.aisle}",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = Text_12,
                color = DarkGray
            ),
            modifier = Modifier.constrainAs(aisleRef) {
                start.linkTo(availabilityRef.end, margin = 5.dp)
                top.linkTo(availabilityRef.top) // Align with availability text
            }
        )

        // Add to Cart Button
        Button(
            onClick = { /* Handle add to cart logic here */ }, // TODO: Add actual add-to-cart logic
            colors = ButtonDefaults.buttonColors(backgroundColor = com.target.targetcasestudy.core.ui.theme.PrimaryRed),
            shape = RoundedCornerShape(Padding_20),
            modifier = Modifier
                .width(130.dp)
                .height(35.dp)
                .constrainAs(addToCartButtonRef) {
                    top.linkTo(availabilityRef.bottom, margin = Padding_10)
                    start.linkTo(guideline)
                    end.linkTo(parent.end)
                    width =
                        Dimension.preferredWrapContent
                }
        ) {
            Text(
                text = stringResource(R.string.add_to_cart),
                color = Color.White,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSmall
                )
            )
        }
    }
}