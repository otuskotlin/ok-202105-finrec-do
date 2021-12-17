package com.finyou.fintrack.backend.common.models

data class FinPrincipalModel(
    val id: UserIdModel = UserIdModel.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<FinUserGroup> = emptySet()
) {
    companion object {
        val NONE = FinPrincipalModel()
    }
}
