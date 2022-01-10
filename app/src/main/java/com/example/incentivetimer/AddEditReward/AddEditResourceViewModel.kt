package com.example.incentivetimer.AddEditReward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.data.RewardDao
import com.example.incentivetimer.ui.IconKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditResourceViewModel @Inject constructor(
    private val rewardDao: RewardDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)

    val isEditMode = rewardId != null

    private val showRewardIconSelectionDialog =
        savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialog",false)

    private val rewardNameInputLiveData =
        savedStateHandle.getLiveData<String>("rewardNameLiveData", "")
    val rewardNameInput: LiveData<String> = rewardNameInputLiveData

    private val chanceInPercentInputLiveData =
        savedStateHandle.getLiveData<Int>("chanceInPercentInputLiveData", 10)
    val chanceInPercentInput: LiveData<Int> = chanceInPercentInputLiveData

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    sealed class AddEditRewardEvent {
        object RewardCreated : AddEditRewardEvent()
    }

    fun onRewardNameInputChanged(input: String) {
        rewardNameInputLiveData.value = input
    }

    fun onChangeInPercentInputChanged(input: Int) {
        chanceInPercentInputLiveData.value = input
    }

    fun onRewardIconButtonClicked(){
        showRewardIconSelectionDialog.value = true
    }

    fun onRewardIconDialogDismissed(){
        showRewardIconSelectionDialog.value = false
    }

    fun onSavedClicked() {
        val rewardNameInput = rewardNameInput.value
        val chanceInPercentInput = chanceInPercentInput.value

        viewModelScope.launch {
            if (rewardNameInput != null && chanceInPercentInput != null && rewardNameInput.isNotBlank()) {
                if (rewardId != null) {
                    //updateReward()
                } else {
                    //TODO:19/12/2021 Set the icon the add/edit screen
                    createReward(Reward(IconKey.CAKE, rewardNameInput, chanceInPercentInput))
                }
            } else {
                // Show Snackbar
            }
        }
    }

    private suspend fun updateReward(reward: Reward) {

    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }


}

const val ARG_REWARD_ID = "ARG_REWARD_ID"