package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.stream.Collectors


fun openEmail(
    emails: List<String>,
    subject: String,
    body: String,
    context: Context
){
    val emailsString: String = emails.stream().map { obj: Any -> obj.toString() }
        .collect(Collectors.joining("; "))
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$emailsString"))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    context.startActivity(intent)
}