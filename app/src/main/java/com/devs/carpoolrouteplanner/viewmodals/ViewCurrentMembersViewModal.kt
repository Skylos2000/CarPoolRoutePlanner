package com.devs.carpoolrouteplanner.viewmodals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class MemberModel(val name:String,val latitude:Double,val longitude:Double)

class ViewCurrentMembersViewModal: ViewModel() {
    public val currentMembers: MutableLiveData<Array<MemberModel>> = MutableLiveData<Array<MemberModel>>()

    public fun loadMembers(){
        currentMembers.value = arrayOf(MemberModel("John Doe",12.22,223.11)
                ,MemberModel("Jane Doe",12.22,223.11),
            MemberModel("James Bond",12.22,223.11)
        )
    }
}