package com.indieme.kalorie.utils

import com.indieme.kalorie.data.model.DateDTO
import com.indieme.kalorie.data.model.DateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


object TimingUtils {

    enum class TimeFormats(val timeFormat: String) {
        DD_MM_YYYY("dd/MM/yyyy"), DD__MM__YYYY("dd-MM-yyyy"), DD_MM_YYYY_HH_MM_A("dd/MM/yyyy hh:mm a"), DD_MMM_YYYY(
            "dd MMM yyyy"
        ),
        MMM_YYYY("MMM, yyyy"), MMM("MMM"), MM("MM"), EEEE("EEE"), DD("dd"), YYYY("yyyy"), DD_MMMM_YYYY(
            "dd MMMM yyyy"
        ),
        DD__MM__YYYY_HH_MM_A("dd-MM-yyyy hh:mm a"), MM_DD_YYYY("MM/dd/yyyy"), YYYY_MM_DD("yyyy/MM/dd"), CUSTOM_YYYY_MM_DD(
            "yyyy-MM-dd"
        ),
        YYYY_MM_DD_HH_MM_A("yyyy/MM/dd, hh:mm a"), CUSTOM_YYYY_MM_DD_HH_MM_A("yyyy/MM/dd hh:mm a"), YYYY_MM_DD_HH_MM_S(
            "yyyy-MM-dd hh:mm:ss"
        ),
        MONTH_NUMBER("MM"), DD_MMM("dd MMM"), EEE_MM_DD_YYYY_HH_MM("E MMM dd yyyy dd HH:mm"), YYYY_MM_DD_T_00(
            "yyyy-MM-dd'T'HH:mm:ss.sssZ"
        ),
        E_MMM_DD_HH_SS_Z_YYYY("E MMM dd HH:mm:ss Z yyyy"), MONTH_FULL_NAME("MMMM"), MONTH_SHORT_NAME(
            "MMM"
        ),
        MMM_dd_hh_mm_a("MMM dd, hh:mm a"), YEAR("yyyy"), DATE("dd"), HH_24("HH:mm"), HH_12("hh:mm a"), HOUR_24(
            "HH"
        ),
        HOUR_12("hh"), MINUTE("mm"), AM_PM("a"), DAY_NAME("EEEE"), MONTH_DD_YEAR("MMMM dd yyyy"), CUSTOM_DAY(
            "EEEE-dd-MMM"
        ),
        CUSTOM_DATE_TIME("dd-MM-yyyy hh:mm");

    }

    fun getTimeInString(timeInSeconds: Long, timeFormat: TimeFormats): String {
        val date = Date(timeInSeconds * 1000L)
        val sdf = SimpleDateFormat(timeFormat.timeFormat, Locale.getDefault())
        return sdf.format(date)
    }

    fun getTimeInUnixStamp(time: String?, timeFormat: TimeFormats): Long {
        return try {
            if (time == null) return 0
            val sdf = SimpleDateFormat(timeFormat.timeFormat, Locale.getDefault())
            val date = sdf.parse(time)
            date.time / 1000
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getTodayDateInMillis(): Long {
        val instance = Calendar.getInstance()
        return instance.timeInMillis;

    }

    suspend fun getDatesFromToday(): DateResult {
        return withContext(Dispatchers.IO) {
            val listOfDates = mutableListOf<DateDTO>()
            val instance = Calendar.getInstance();
            val actualCurrentDate =
                getTimeInString(instance.timeInMillis / 1000, TimeFormats.DD_MM_YYYY);

            instance.set(Calendar.MONTH, 0)
            val currentYear = instance.get(Calendar.YEAR);
            val maxYear = currentYear + 100;

            var i = currentYear;
            var selectedDate = 0;
            var index = 0;
            while (i <= maxYear) {
                val dateDTO = DateDTO(instance.timeInMillis / 1000, 0)
                dateDTO.selected = if (actualCurrentDate == getTimeInString(
                        instance.timeInMillis / 1000,
                        TimeFormats.DD_MM_YYYY
                    )
                ) {
                    selectedDate = index;
                    index
                } else {
                    0
                }

                listOfDates.add(dateDTO)

                instance.add(Calendar.DATE, 1)
                i = instance.get(Calendar.YEAR)
                index++
            }
            return@withContext DateResult(listOfDates, selectedDate);
        }


    }

    fun getStartOfADayInMillis(day: Long): Long {
        val dayInMillis = day * 1000;

        val oneDayInMillis = (24 * 60 * 60 * 1000).toLong()
        return Date((dayInMillis / oneDayInMillis) * oneDayInMillis).time / 1000
    }

    fun getEndOfADayInMillis(day: Long): Long {
        val dayInMillis = day * 1000

        val oneDayInMillis = (24 * 60 * 60 * 1000).toLong()
        return Date((dayInMillis / oneDayInMillis + 1) * oneDayInMillis - 1).time / 1000
    }

}