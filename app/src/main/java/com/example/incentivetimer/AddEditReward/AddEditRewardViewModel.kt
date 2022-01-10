package com.example.incentivetimer.AddEditReward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.data.RewardDao
import com.example.incentivetimer.ui.IconKey
import com.example.incentivetimer.ui.defaultRewardIconKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRewardViewModel @Inject constructor(
    private val rewardDao: RewardDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), AddEditRewardScreenActions {
    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)
    val isEditMode = rewardId != null

    private val rewardNameInputLiveData =
        savedStateHandle.getLiveData<String>("rewardNameLiveData", "")
    val rewardNameInput: LiveData<String> = rewardNameInputLiveData

    private val rewardNameInputIsErrorLiveData = savedStateHandle.getLiveData<Boolean>("rewardNameInputIsError", false)
    val rewardNameInputIsError : LiveData<Boolean> = rewardNameInputIsErrorLiveData

    private val chanceInPercentInputLiveData =
        savedStateHandle.getLiveData<Int>("chanceInPercentInputLiveData", 10)
    val chanceInPercentInput: LiveData<Int> = chanceInPercentInputLiveData

    private val rewardIconSelectionLiveData =
        savedStateHandle.getLiveData<IconKey>("rewardIconSelectionLiveData", defaultRewardIconKey)
    val rewardIconKeySelection: LiveData<IconKey> = rewardIconSelectionLiveData

    private val showRewardIconSelectionDialogLiveData =
        savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialogLiveData", false)
    val showRewardIconSelectionDialog: LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    sealed class AddEditRewardEvent {
        object RewardCreated : AddEditRewardEvent()
    }

    override fun onChangeInPercentInputChanged(input: Int) {
        chanceInPercentInputLiveData.value = input
    }

    override fun onRewardNameInputChanged(input: String) {
        rewardNameInputLiveData.value = input
    }

    override fun onRewardIconButtonClicked() {
        showRewardIconSelectionDialogLiveData.value = true
    }

    override fun onRewardIconSelected(iconKey: IconKey) {
        rewardIconSelectionLiveData.value = iconKey
    }

    override fun onRewardIconDialogDismissRequest() {
        showRewardIconSelectionDialogLiveData.value = false
    }

    override fun onSaveClicked() {
        val rewardNameInput = rewardNameInput.value
        val chanceInPercentInput = chanceInPercentInput.value
        val rewardIconKeySelection = rewardIconKeySelection.value

        rewardNameInputIsErrorLiveData.value = false

        viewModelScope.launch {
            if (!rewardNameInput.isNullOrBlank() && chanceInPercentInput != null && rewardIconKeySelection != null) {
                if (rewardId != null) {
                    //updateReward()
                } else {
                    createReward(
                        Reward(
                            rewardIconKeySelection,
                            rewardNameInput,
                            chanceInPercentInput
                        )
                    )
                }
            } else {
                if (rewardNameInput.isNullOrBlank()){
                    rewardNameInputIsErrorLiveData.value = true
                }
            }
        }
    }

    private fun validateInput(){

    }

    private suspend fun updateReward(reward: Reward) {
    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }

}

const val ARG_REWARD_ID = "ARG_REWARD_ID"