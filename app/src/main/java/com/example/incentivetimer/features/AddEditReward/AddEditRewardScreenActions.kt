package com.example.incentivetimer.features.AddEditReward

import com.example.incentivetimer.core.ui.IconKey

interface AddEditRewardScreenActions {
    fun onRewardNameInputChanged(input: String)
    fun onChangeInPercentInputChanged(input: Int)
    fun onRewardIconButtonClicked()
    fun onRewardIconSelected(iconKey: IconKey)
    fun onRewardIconDialogDismissed()
    fun onSaveClicked()
    fun onDeleteRewardClicked()
    fun onDeleteRewardConfirmed()
    fun onDeleteRewardDialogDismissed()
}