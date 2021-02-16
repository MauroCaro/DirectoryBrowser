package com.base.directorybrowser.dagger.injector

import com.base.directorybrowser.dagger.component.ApplicationComponent

class Injector {
    companion object {
        private lateinit var component: ApplicationComponent

        fun init(messageComponent: ApplicationComponent) {
            component = messageComponent
        }

        fun component(): ApplicationComponent {
            return component
        }
    }
}