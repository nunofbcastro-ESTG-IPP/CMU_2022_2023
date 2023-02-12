package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.openEmail

@Composable
fun AvatarAndEmail(
    user: User,
    removeUser: () -> Unit,
    context: Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                imageRequest = user.avatar,
                size = 50.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(
                onClick = {
                    openEmail(
                        context = context,
                        emails = listOf(
                            user.email
                        ),
                        subject = "",
                        body = "",
                    )
                }
            ) {
                Text(
                    text = user.email,
                    color = MaterialTheme.myCustomColors.subtitle,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 15.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        MyButtonAndText(
            icon = Icons.Filled.Delete,
            size = 40.dp,
            background = MaterialTheme.colors.secondary,
            iconColor = MaterialTheme.colors.onSecondary,
            onClick = {
                removeUser()
            }
        )
    }
}

@Preview
@Composable
fun PreviewAvatarAndEmailSuccessImage() {
    val user = User(
        email = "preview@example.com",
        name = "Preview",
        avatar = "https://randomuser.me/api/portraits/men/34.jpg",
    )
    CMU_07_8200398_8200591_8200592Theme {
        AvatarAndEmail(
            user = user,
            removeUser = {},
            context = LocalContext.current,
        )
    }
}

@Preview
@Composable
fun PreviewAvatarAndEmailErrorImage() {
    val user = User(
        email = "preview@example.com",
        name = "Preview",
        avatar = "",
    )
    CMU_07_8200398_8200591_8200592Theme {
        AvatarAndEmail(
            user = user,
            removeUser = {},
            context = LocalContext.current,
        )
    }
}