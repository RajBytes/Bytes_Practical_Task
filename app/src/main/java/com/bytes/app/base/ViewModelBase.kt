package com.bytes.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class ViewModelBase : ViewModel() {

    var snackbarMessage = MutableLiveData<Any>()
    var progressBarDisplay = MutableLiveData<Boolean>()
    var toolBarModel = MutableLiveData<ToolbarModel>()
    private var hideKeyBoardEvent = MutableLiveData<Any>()

    fun getHideKeyBoardEvent(): MutableLiveData<Any> {
        return hideKeyBoardEvent
    }

    /**
     * This method is used to display the Snake Bar Message
     *
     * @param message string object to display.
     */
    fun showSnackbarMessage(message: Any) {
        snackbarMessage.postValue(message)
    }

    /**
     * This method is used to display the Progressbar
     *
     * @param display Boolean flag to display or not.
     */
    fun showProgressBar(display: Boolean) {
        progressBarDisplay.postValue(display)

    }

    /**
     * This method is used to update the Title of Each Screen as Toolbar
     *
     * @param toolbarModel Customizable Model to manage the Toolbar/Titlebar.
     */
    fun setToolbarItems(toolbarModel: ToolbarModel) {
        toolBarModel.postValue(toolbarModel)
    }

    /**
     * This method is used to hide the Keyboard
     */
    fun hideKeyboard() {
        getHideKeyBoardEvent().value = true
    }

}
