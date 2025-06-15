package com.target.targetcasestudy.feature_deals.ui.dealslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.theme.DarkGray
import com.target.targetcasestudy.core.ui.theme.LightGray
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first


@Composable
fun DealsListContent(
    deals: List<Deals>,
    searchQuery: String,
    onDealClick: (String) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    // Scroll to top when search query is cleared and all deals are displayed
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            // Wait until LazyColumn has items before attempting to scroll
            snapshotFlow { deals }
                .filter { it.isNotEmpty() }
                .first()

            // Delay a frame to ensure the UI has composed the items
            withFrameNanos { }
            listState.scrollToItem(0)
        }
    }

    if (deals.isEmpty()) {
        val message = if (searchQuery.isNotBlank()) {
            stringResource(id = R.string.no_deals_found, searchQuery)
        } else {
            stringResource(id = R.string.no_deals_available)
        }
        Text(
            text = message,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = TextStyle(fontFamily = RobotoFontFamily, fontSize = 16.sp, color = DarkGray)
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState // Pass the state from the parent for control
        ) {
            itemsIndexed(
                items = deals,
                key = { _, item -> item.id } // Provide stable keys for efficient recomposition
            ) { index, dealItem ->
                Column {
                    DealItemCard(
                        deal = dealItem,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDealClick
                    )
                    // Add a divider after each item except the last one
                    if (index < deals.lastIndex) {
                        Divider(
                            color = LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}