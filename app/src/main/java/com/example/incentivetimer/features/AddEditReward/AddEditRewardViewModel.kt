package com.example.incentivetimer.features.AddEditReward

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.incentivetimer.core.ui.IconKey
import com.example.incentivetimer.core.ui.defaultRewardIconKey
import com.example.incentivetimer.core.ui.screenspecs.AddEditRewardScreenSpec
import com.example.incentivetimer.core.ui.screenspecs.NO_REWARD_ID
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.data.RewardDao

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

    private val rewardId = AddEditRewardScreenSpec.getRewardIdFromSavedStateHandle(savedStateHandle)

    //Assuming Reward is Parcelable
    private val rewardLiveData = savedStateHandle.getLiveData<Reward>(KEY_REWARD_LIVE_DATA)

    //TO GUARANTEE EDIT MODE : rewardID IS EQUAL TO THE REWARD SELECTED
    val isEditMode = rewardId != NO_REWARD_ID

    val rewardNameInput: LiveData<String> = rewardLiveData.map { it.name }
    val chanceInPercentInput: LiveData<Int> = rewardLiveData.map { it.changeInPercent }
    val rewardIconKeySelection: LiveData<IconKey> = rewardLiveData.map { it.iconKey }

    private val rewardNameInputIsErrorLiveData =
        savedStateHandle.getLiveData<Boolean>("rewardNameInputIsError", false)
    val rewardNameInputIsError: LiveData<Boolean> = rewardNameInputIsErrorLiveData

    private val showRewardIconSelectionDialogLiveData =
        savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialogLiveData", false)
    val showRewardIconSelectionDialog: LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val showDeleteRewardConfirmationDialogLiveData =
        savedStateHandle.getLiveData<Boolean>("showDeleteRewardConfirmationDialogLiveData")
    val showDeleteRewardConfirmationDialog: LiveData<Boolean> =
        showDeleteRewardConfirmationDialogLiveData

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        //NOTICE : "rewardLiveData" its the exact key name
        if (!savedStateHandle.contains("rewardLiveData")) {
            if (rewardId != null && isEditMode) {
                viewModelScope.launch {
                    rewardLiveData.value = rewardDao.getRewardById(rewardId)
                }
            } else {
                rewardLiveData.value =
                    Reward(name = "", changeInPercent = 10, iconKey = defaultRewardIconKey)
            }

        }
    }

    sealed class AddEditRewardEvent {
        object RewardCreated : AddEditRewardEvent()
        object RewardUpdated : AddEditRewardEvent()
        object RewardDeleted : AddEditRewardEvent()
    }

    override fun onRewardNameInputChanged(input: String) {
        rewardLiveData.value = rewardLiveData.value?.copy(name = input)
    }

    override fun onChangeInPercentInputChanged(input: Int) {
        rewardLiveData.value = rewardLiveData.value?.copy(changeInPercent = input)
    }

    override fun onRewardIconSelected(iconKey: IconKey) {
        rewardLiveData.value = rewardLiveData.value?.copy(iconKey = iconKey)
    }

    override fun onRewardIconButtonClicked() {
        showRewardIconSelectionDialogLiveData.value = true
    }

    override fun onRewardIconDialogDismissed() {
        showRewardIconSelectionDialogLiveData.value = false
    }

    override fun onSaveClicked() {
        //RETURN IF REWARD ITS NULL!!!!
        val reward = rewardLiveData.value ?: return
        rewardNameInputIsErrorLiveData.value = false

        viewModelScope.launch {
            if (reward.name.isNotBlank()) {
                if (isEditMode) {
                    updateReward(reward = reward)
                } else {
                    createReward(reward)
                }
            } else {
                rewardNameInputIsErrorLiveData.value = true
            }
        }
    }

    private suspend fun updateReward(reward: Reward) {
        rewardDao.updateReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardUpdated)
    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }

    override fun onDeleteRewardClicked() {
        showDeleteRewardConfirmationDialogLiveData.value = true
    }

    override fun onDeleteRewardConfirmed() {
        showDeleteRewardConfirmationDialogLiveData.value = false
        viewModelScope.launch {
            val reward = rewardLiveData.value
            if (reward != null) {
                rewardDao.deleteReward(reward)
                eventChannel.send(AddEditRewardEvent.RewardDeleted)
            }
        }
    }

    override fun onDeleteRewardDialogDismissed() {
        showDeleteRewardConfirmationDialogLiveData.value = false
    }
}

/*const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L*/

private const val KEY_REWARD_LIVE_DATA = "KEY_REWARD_LIVE_DATA"

const val ADD_EDIT_REWARD_RESULT = "ADD_EDIT_REWARD_RESULT"
const val RESULT_REWARD_ADDED = "RESULT_REWARD_ADDED"
const val RESULT_REWARD_UPDATED = "RESULT_REWARD_UPDATED"
const val RESULT_REWARD_DELETE = "RESULT_REWARD_DELETED"