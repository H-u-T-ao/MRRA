package com.mrra.base

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

abstract class BaseFragment : Fragment(), CoroutineScope by MainScope() {
}