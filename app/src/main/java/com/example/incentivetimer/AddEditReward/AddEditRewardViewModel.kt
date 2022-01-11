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
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class AddEditRewardViewModel @Inject constructor(
    private val rewardDao: RewardDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), AddEditRewardScreenActions {
    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)
    private var reward: Reward? = null

    val isEditMode = rewardId != NO_REWARD_ID


    private val rewardNameInputLiveData =
        savedStateHandle.getLiveData<String>("rewardNameLiveData")
    val rewardNameInput: LiveData<String> = rewardNameInputLiveData

    private val chanceInPercentInputLiveData =
        savedStateHandle.getLiveData<Int>("chanceInPercentInputLiveData")

    val chanceInPercentInput: LiveData<Int> = chanceInPercentInputLiveData
    private val rewardIconKeySelectionLiveData =
        savedStateHandle.getLiveData<IconKey>("rewardIconSelectionLiveData")

    val rewardIconKeySelection: LiveData<IconKey> = rewardIconKeySelectionLiveData

    private val showRewardIconSelectionDialogLiveData =
        savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialogLiveData", false)

    private val rewardNameInputIsErrorLiveData =
        savedStateHandle.getLiveData<Boolean>("rewardNameInputIsError", false)
    val rewardNameInputIsError: LiveData<Boolean> = rewardNameInputIsErrorLiveData

    val showRewardIconSelectionDialog: LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        logcat { "rewardId: $rewardId" }
        if (rewardId != null && rewardId != NO_REWARD_ID) {
            viewModelScope.launch {
                reward = rewardDao.getRewardById(rewardId)
                populateEmptyInputValuesWithRewardData()
            }
        } else {
            populateInputValuesWithDefaultValues()
        }
    }

    private fun populateEmptyInputValuesWithRewardData() {
        val reward = this.reward
        if (reward != null) {
            if (rewardNameInputLiveData.value == null) {
                rewardNameInputLiveData.value = reward.name
            }
            if (chanceInPercentInputLiveData.value == null) {
                chanceInPercentInputLiveData.value = reward.changeInPercent
            }
            if (rewardIconKeySelectionLiveData.value == null) {
                rewardIconKeySelectionLiveData.value = reward.iconKey
            }
        }
    }

    private fun populateInputValuesWithDefaultValues() {
        if (rewardNameInputLiveData.value == null) {
            rewardNameInputLiveData.value = ""
        }
        if (chanceInPercentInputLiveData.value == null) {
            chanceInPercentInputLiveData.value = 10
        }
        if (rewardIconKeySelectionLiveData.value == null) {
            rewardIconKeySelectionLiveData.value = defaultRewardIconKey
        }
    }

    sealed class AddEditRewardEvent {
        object RewardCreated : AddEditRewardEvent()
        object RewardUpdated : AddEditRewardEvent()
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
        rewardIconKeySelectionLiveData.value = iconKey
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
                val reward = reward
                if (reward != null) {
                    updateReward(
                        reward.copy(
                            name = rewardNameInput,
                            changeInPercent = chanceInPercentInput,
                            iconKey = rewardIconKeySelection
                        )
                    )
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
                if (rewardNameInput.isNullOrBlank()) {
                    rewardNameInputIsErrorLiveData.value = true
                }
            }
        }
    }

    private fun validateInput() {

    }

    private suspend fun updateReward(reward: Reward) {
        rewardDao.updateReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardUpdated)
        //TODO: 20/12/2021 Show confirmation message

    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
        //TODO: 20/12/2021 Show confirmation message
    }

}

const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L

const val ADD_EDIT_REWARD_RESULT = "ADD_EDIT_REWARD_RESULT"
const val RESULT_REWARD_ADDED = "RESULT_REWARD_ADDED"
const val RESULT_REWARD_UPDATED = "RESULT_REWARD_UPDATED"