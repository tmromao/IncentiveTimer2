package com.example.incentivetimer.AddEditReward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditResourceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)

    val isEditMode = rewardId != null

    private val rewardNameInputLiveData = savedStateHandle.getLiveData<String>("rewardNameLiveData","")
    val rewardNameInput: LiveData<String> = rewardNameInputLiveData

    private val chanceInPercentInputLiveData = savedStateHandle.getLiveData<Int>("chanceInPercentInputLiveData", 10)
    val chanceInPercentInput: LiveData<Int> = chanceInPercentInputLiveData

    fun onRewardNameInputChanged(input: String) {
        rewardNameInputLiveData.value = input
    }

    fun onChangeInPercentInputChanged(input: Int) {
        chanceInPercentInputLiveData.value = input
    }
}

const val ARG_REWARD_ID = "ARG_REWARD_ID"