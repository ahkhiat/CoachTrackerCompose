
import java.text.SimpleDateFormat

import java.util.*

final class DateFormatTransformer(date: String) {
    val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        .parse(date)
}

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val dateToTransform = inputFormat.parse(date)
    return outputFormat.format(dateToTransform!!)
}