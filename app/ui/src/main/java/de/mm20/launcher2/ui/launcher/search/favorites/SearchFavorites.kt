package de.mm20.launcher2.ui.launcher.search.favorites

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.mm20.launcher2.search.SavableSearchable
import de.mm20.launcher2.search.data.Tag
import de.mm20.launcher2.ui.R
import de.mm20.launcher2.ui.common.FavoritesTagSelector
import de.mm20.launcher2.ui.component.Banner
import de.mm20.launcher2.ui.launcher.search.common.grid.SearchResultGrid
import de.mm20.launcher2.ui.locals.LocalCardStyle

fun LazyListScope.SearchFavorites(
    favorites: List<SavableSearchable>,
    pinnedTags: List<Tag>,
    selectedTag: String?,
    tagsExpanded: Boolean,
    onExpandTags: (Boolean) -> Unit,
    onSelectTag: (String?) -> Unit,
    editButton: Boolean,
    reverse: Boolean,
) {
    item(
        key = "favorites",
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Column(
                modifier = Modifier
                    .padding(
                        top = if (reverse) 8.dp else 0.dp,
                        bottom = if (reverse) 0.dp else 8.dp,
                    )
                    .background(
                        MaterialTheme.colorScheme.surface.copy(
                            LocalCardStyle.current.opacity
                        ),
                        MaterialTheme.shapes.medium
                    )
            ) {
                if (favorites.isNotEmpty()) {
                    SearchResultGrid(favorites)
                } else {
                    Banner(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(
                            if (selectedTag == null) R.string.favorites_empty else R.string.favorites_empty_tag
                        ),
                        icon = if (selectedTag == null) Icons.Rounded.Star else Icons.Rounded.Tag,
                    )
                }
                if (pinnedTags.isNotEmpty() || editButton) {
                    FavoritesTagSelector(
                        tags = pinnedTags,
                        selectedTag = selectedTag,
                        editButton = editButton,
                        reverse = false,
                        onSelectTag = onSelectTag,
                        scrollState = rememberScrollState(),
                        expanded = tagsExpanded,
                        onExpand = onExpandTags,
                    )
                }
            }
        }
    }
}