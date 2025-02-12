package com.laixer.sample.domain.usecase

import com.laixer.sample.domain.model.Vlog
import com.laixer.sample.domain.model.User
import com.laixer.sample.domain.repository.VlogRepository
import com.laixer.sample.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction

/**
 * The standard library provides Pair and Triple.
 * In most cases, though, named data classes are a better design choice.
 * This is because they make the code more readable by providing meaningful names for properties.
 */
data class CombinedUserVlog(val user: User, val vlog: Vlog)

class UsersVlogsUseCase constructor(
    private val userRepository: UserRepository,
    private val vlogRepository: VlogRepository
) {

    fun get(refresh: Boolean): Single<List<CombinedUserVlog>> =
        Single.zip(userRepository.get(refresh), vlogRepository.get(refresh),
            BiFunction { userList, vlogList -> map(userList, vlogList) })
}

class UserVlogUseCase constructor(
    private val userRepository: UserRepository,
    private val vlogRepository: VlogRepository
) {

    fun get(userId: String, vlogId: String, refresh: Boolean): Single<CombinedUserVlog> =
        Single.zip(userRepository.get(userId, refresh), vlogRepository.get(vlogId, refresh),
            BiFunction { user, vlog -> map(user, vlog) })
}

/**
 * To obtain the user from a vlog we need to use the userId from the vlog to find it in the user list.
 * This is a limitation that comes from the network API and this specific use case requires both sample and users.
 */
fun map(user: User, vlog: Vlog): CombinedUserVlog = CombinedUserVlog(user, vlog)

fun map(userList: List<User>, vlogList: List<Vlog>): List<CombinedUserVlog> =
    vlogList.map { vlog -> CombinedUserVlog(userList.first { vlog.userId == it.id }, vlog) }
