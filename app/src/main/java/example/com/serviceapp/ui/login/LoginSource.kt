package example.com.serviceapp.ui.login

import example.com.serviceapp.utils.GetService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginSource @Inject constructor(
    private val api: GetService,
) {
    fun userValidation() {
        CoroutineScope(Dispatchers.IO).launch {
            // will be add
        }
    }
}
