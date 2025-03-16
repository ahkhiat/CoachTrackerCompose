package com.devid_academy.coachtrackercompose.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.devid_academy.coachtrackercompose.R

//fun Fragment.navController() = findNavController()

fun Context.getStatus(status: Int): String {
    return when (status) {
        0 -> getString(R.string.pending)
        1 -> getString(R.string.accepted)
        2 -> getString(R.string.rejected)
        else -> getString(R.string.status_undefined)
    }
}

fun <T> Fragment.fillSpinner(liveData: LiveData<List<T>>, spinner: Spinner, getDisplayText: (T) -> String){
    liveData.observe(viewLifecycleOwner) {
        if (it.isNotEmpty()) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.map { getDisplayText(it) }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}

fun makeToast(context: Context ,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


//fun returnEventTypeInt(selectedResId: Int): Int {
//    return when (selectedResId) {
//        R.id.create_radio_btn1 -> 4
//        R.id.create_radio_btn2 -> 5
//        else -> -1
//    }
//}

sealed class ViewModelEvent {
    data object NavigateToMainScreen: ViewModelEvent()
    data class ShowSnackBar(val resId: Int): ViewModelEvent()
}
