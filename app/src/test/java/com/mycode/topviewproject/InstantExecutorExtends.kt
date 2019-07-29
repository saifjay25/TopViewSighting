package com.mycode.topviewproject

import androidx.annotation.NonNull
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.ArchTaskExecutor
import org.junit.jupiter.api.extension.ExtensionContext



class InstantExecutorExtends : AfterEachCallback, BeforeEachCallback {

    @Throws(Exception::class)
    override fun afterEach(context: ExtensionContext) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Throws(Exception::class)
    override fun beforeEach(context: ExtensionContext) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(@NonNull runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(@NonNull runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }
}