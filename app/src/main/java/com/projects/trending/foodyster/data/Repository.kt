package com.projects.trending.foodyster.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


//Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {


    val remote = remoteDataSource
    val local = localDataSource
}