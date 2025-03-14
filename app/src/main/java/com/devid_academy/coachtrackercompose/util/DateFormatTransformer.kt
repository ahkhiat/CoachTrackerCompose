
import java.text.SimpleDateFormat

import java.util.*
import java.util.Calendar
import java.util.Date
import java.util.Locale


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

//fun getPartialDate(date: String, pattern: DatePattern): String {
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//    val parsedDate: Date? = dateFormat.parse(date)
//
//    return parsedDate?.let {
//        SimpleDateFormat(pattern.pattern, Locale.getDefault())
//            .format(it)
//            .replaceFirstChar { it.uppercase() }
//    } ?: "Date invalide"
//}

sealed class DatePattern(val pattern: String) {
    object WeekDay: DatePattern("EEEE")
    object Day: DatePattern("dd")
    object Month: DatePattern("MMM")
    object Year: DatePattern("yyyy")
    object Hour: DatePattern("HH:mm")

}

fun getPartialDate(date: String, pattern: DatePattern, offsetHours: Int = 0): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val parsedDate = dateFormat.parse(date) ?: return "Date invalide"

    val calendar = Calendar.getInstance().apply {
        time = parsedDate
        add(Calendar.HOUR_OF_DAY, offsetHours)
    }

    return SimpleDateFormat(pattern.pattern, Locale.getDefault()).format(calendar.time)
        .replaceFirstChar { it.uppercase() }
}