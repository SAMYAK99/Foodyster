package com.projects.trending.foodyster.data

import com.projects.trending.foodyster.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


//Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
@ActivityRetainedScoped
class Repository @Inject constructor( remoteDataSource: RemoteDataSource) {

    val remote = remoteDataSource
}