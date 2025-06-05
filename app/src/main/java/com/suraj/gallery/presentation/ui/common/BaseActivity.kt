package com.suraj.gallery.presentation.ui.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by Suraj Bahadur on 6/5/2025.
 */

abstract class BaseActivity : AppCompatActivity() {

    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun showLoading(show: Boolean) {
        // Implement loading indicator
    }
}