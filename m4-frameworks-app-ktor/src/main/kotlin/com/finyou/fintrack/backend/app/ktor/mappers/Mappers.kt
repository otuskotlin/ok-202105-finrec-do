package com.finyou.fintrack.backend.app.ktor.mappers

import com.finyou.fintrack.backend.common.models.FinPrincipalModel
import com.finyou.fintrack.backend.common.models.FinUserGroup
import com.finyou.fintrack.backend.common.models.UserIdModel
import io.ktor.auth.jwt.*

fun JWTPrincipal?.toModel() = this?.run {
    FinPrincipalModel(
        id = payload.getClaim("id").asString()?.let { UserIdModel(it) } ?: UserIdModel.NONE,
        fname = payload.getClaim("fname").asString() ?: "",
        mname = payload.getClaim("mname").asString() ?: "",
        lname = payload.getClaim("lname").asString() ?: "",
        groups = payload
            .getClaim("groups")
            ?.asList(String::class.java)
            ?.mapNotNull {
                try {
                    FinUserGroup.valueOf(it)
                } catch (e: Throwable) {
                    null
                }
            }?.toSet() ?: emptySet()
    )
} ?: FinPrincipalModel.NONE