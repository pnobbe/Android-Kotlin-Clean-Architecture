package com.laixer.sample.datasource.cache

import com.laixer.cache.ReactiveCache
import com.laixer.sample.data.datasource.VlogCacheDataSource
import com.laixer.sample.domain.model.Vlog
import io.reactivex.Single

class VlogCacheDataSourceImpl constructor(
    private val cache: ReactiveCache<List<Vlog>>
) : VlogCacheDataSource {
    val key = "Vlog List"

    override fun get(): Single<List<Vlog>> =
        cache.load(key)

    override fun set(list: List<Vlog>): Single<List<Vlog>> =
        cache.save(key, list)

    override fun get(vlogId: String): Single<Vlog> =
        cache.load(key)
            .map { it.first { it.id == vlogId } }

    override fun set(item: Vlog): Single<Vlog> =
        cache.load(key)
            .map { it.filter { it.id != item.id }.plus(item) }
            .flatMap { set(it) }
            .map { item }
}
