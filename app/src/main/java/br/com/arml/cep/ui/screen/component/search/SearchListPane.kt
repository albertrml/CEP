package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.screen.component.search.listpane.SearchComponent
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTab
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTabSaver
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTabValues
import br.com.arml.cep.ui.theme.dimens

@Composable
fun SearchListPane(
    modifier: Modifier = Modifier,
    selectedTab: SearchTab,
    onChangeTab: (SearchTab) -> Unit = {},
    onSearchCep: (String) -> Unit = {},
    onSearchAddress: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val selectedTabIndex by remember(selectedTab) {
        derivedStateOf{ SearchTabValues.indexOf(selectedTab) }
    }

    val modifierSearchComponent = if(
        LocalConfiguration.current.smallestScreenWidthDp >= 600
    ) Modifier.width(488.dp) else Modifier.fillMaxWidth()

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(
                title = stringResource(R.string.searchListPane_header_label),
                logo = Icons.Default.Search
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = modifierSearchComponent
                    .padding(MaterialTheme.dimens.mediumSpacing)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                ) {
                    SearchTabValues.forEachIndexed { index, tab ->
                        Tab(
                            selected = tab == selectedTab,
                            onClick = { onChangeTab(SearchTabValues[index]) },
                            text = {
                                Text(
                                    text = stringResource(tab.label),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing)
                )
                SearchComponent(
                    modifier = Modifier.width(488.dp),
                    selectedTab = selectedTab,
                    onSearchCep = onSearchCep,
                    onSearchAddress = onSearchAddress
                )
            }
        }
    }
}

@Preview(
    name = "Smart Phone Portrait",
    showBackground = true,
    device = "spec:width=360dp,height=640dp"
)
@Preview(
    name = "Compact Phone Portrait",
    showBackground = true,
    device = "spec:width=412dp,height=924dp"
)
@Preview(
    name = "Medium Tablet Portrait",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp"
)
@Preview(
    name = "Smart Phone Landscape",
    showBackground = true,
    device = "spec:width=640dp,height=360dp"
)
@Preview(
    name = "Compact Phone Landscape",
    showBackground = true,
    device = "spec:width=924dp,height=412dp"
)
@Preview(
    name = "Medium Tablet Landscape",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp"
)
@Composable
fun SearchScreenPreview() {
    var selectedTab by rememberSaveable(stateSaver = SearchTabSaver) {
        mutableStateOf(SearchTab.CEP)
    }
    SearchListPane(
        modifier = Modifier.fillMaxSize(),
        selectedTab = selectedTab,
        onChangeTab = { selectedTab = it },
        onSearchCep = {}
    )
}
