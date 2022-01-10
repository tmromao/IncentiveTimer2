package com.example.incentivetimer.rewardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.incentivetimer.data.RewardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel @Inject constructor(
    private val rewardDao: RewardDao,
) : ViewModel() {

    val rewards = rewardDao.getAllRewards().asLiveData()

 /*   private val dummyRewardsLiveData = MutableLiveData<List<Reward>>()
    val dummyRewards: LiveData<List<Reward>> = dummyRewardsLiveData

    init {
        val dummyRewards = mutableListOf<Reward>()
        repeat(12) { index ->
            dummyRewards += Reward(iconKey = IconKeys.CAKE, title = "Item $index", index)
        }
        dummyRewardsLiveData.value = dummyRewards
    }*/
}