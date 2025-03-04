
import java.text.SimpleDateFormat

import java.util.*

final class DateFormatTransformer(date: String) {
    val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        .parse(date)
//    public static fun getDayFromDate(parsedDate)


}