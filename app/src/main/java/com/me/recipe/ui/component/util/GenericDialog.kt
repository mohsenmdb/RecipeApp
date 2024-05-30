package com.me.recipe.ui.component.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun GenericDialog(errors: GenericDialogInfo) {
    GenericDialog(
        onDismiss = errors.onDismiss,
        title = errors.title,
        description = errors.description,
        positiveAction = errors.positiveAction,
        negativeAction = errors.negativeAction,
    )
}

@Composable
internal fun GenericDialog(
    onDismiss: () -> Unit,
    @StringRes title: Int,
    positiveAction: PositiveAction?,
    negativeAction: NegativeAction?,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = title)) },
        text = {
            if (description != null) {
                Text(text = description)
            }
        },
        confirmButton = {
            if (positiveAction != null) {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = positiveAction.onPositiveAction,
                ) {
                    Text(text = stringResource(id = positiveAction.positiveBtnTxt))
                }
            }
        },
        dismissButton = {
            if (negativeAction != null) {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError),
                    onClick = negativeAction.onNegativeAction,
                ) {
                    Text(text = negativeAction.negativeBtnTxt)
                }
            }
        },
    )
}

data class PositiveAction(
    @StringRes val positiveBtnTxt: Int,
    val onPositiveAction: () -> Unit,
)

data class NegativeAction(
    val negativeBtnTxt: String,
    val onNegativeAction: () -> Unit,
)

class GenericDialogInfo private constructor(builder: Builder) {

    @StringRes val title: Int
    val onDismiss: () -> Unit
    val description: String?
    val positiveAction: PositiveAction?
    val negativeAction: NegativeAction?

    init {
        if (builder.title == null) {
            throw Exception("GenericDialog title cannot be null.")
        }
        if (builder.onDismiss == null) {
            throw Exception("GenericDialog onDismiss function cannot be null.")
        }
        this.title = builder.title!!
        this.onDismiss = builder.onDismiss!!
        this.description = builder.description
        this.positiveAction = builder.positiveAction
        this.negativeAction = builder.negativeAction
    }

    class Builder {

        @StringRes var title: Int? = null
            private set

        var onDismiss: (() -> Unit)? = null
            private set

        var description: String? = null
            private set

        var positiveAction: PositiveAction? = null
            private set

        var negativeAction: NegativeAction? = null
            private set

        fun title(@StringRes title: Int): Builder {
            this.title = title
            return this
        }

        fun onDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun description(
            description: String,
        ): Builder {
            this.description = description
            return this
        }

        fun positive(
            positiveAction: PositiveAction?,
        ): Builder {
            this.positiveAction = positiveAction
            return this
        }

        fun negative(
            negativeAction: NegativeAction,
        ): Builder {
            this.negativeAction = negativeAction
            return this
        }

        fun build() = GenericDialogInfo(this)
    }
}
