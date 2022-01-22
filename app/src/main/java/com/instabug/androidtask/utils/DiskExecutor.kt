package com.instabug.androidtask.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskExecutor : Executor {
    private val diskExecutor: Executor
    override fun execute(runnable: Runnable) {
        diskExecutor.execute(runnable)
    }

    init {
        diskExecutor = Executors.newSingleThreadExecutor()
    }
}