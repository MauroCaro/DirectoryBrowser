package com.base.directorybrowser.util

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
inline fun <reified T> any(): T = Mockito.any(T::class.java)