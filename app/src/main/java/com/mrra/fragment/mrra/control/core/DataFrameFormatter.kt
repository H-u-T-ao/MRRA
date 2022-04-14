package com.mrra.fragment.mrra.control.core

import android.bluetooth.BluetoothGattCharacteristic
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.math.PI

class DataFrameFormatter(characteristic: BluetoothGattCharacteristic) {

    private val data: CharArray

    init {
        when {
            characteristic.value == null -> {
                throw NullPointerException("the value of the characteristic is null")
            }
            characteristic.value.size != 11 -> {
                throw IllegalArgumentException("ths size of the characteristic is not 11")
            }
            else -> {
                data = characteristic.value.let {
                    val cs = Charset.forName("ASCII")
                    val bb = ByteBuffer.allocate(it.size)
                    bb.put(it)
                    bb.flip()
                    val cb = cs.decode(bb)
                    cb.array()
                }
            }
        }
    }

    val head: Char get() = data[0]

    val mode: Char get() = data[1]

    val p: Triple<Int, Int, Int>
        get() = Triple(
            (data[2].code + (data[3].code.shl(8))).toShort().toUnsigned(),
            (data[4].code + (data[5].code.shl(8))).toShort().toUnsigned(),
            (data[6].code + (data[7].code.shl(8))).toShort().toUnsigned()
        )

    val initMode: Int get() = data[8].code

    val stopFlag: Int get() = data[9].code

    val tail: Char get() = data[10]

    object ByteArrayBuilder {

        private fun setAll(mode: Int, stop: Int, initMode: Int): ByteArray {
            return ByteArray(11) {
                when (it) {
                    0 -> 'L'.code.toByte()
                    1 -> mode.toByte()
                    2, 3, 4, 5, 6, 7 -> 0
                    8 -> initMode.toByte()
                    9 -> stop.toByte()
                    10 -> 'X'.code.toByte()
                    else -> 0
                }
            }
        }

        fun setInitiative(initMode: Int): ByteArray {
            return setAll(1, 1, initMode)
        }

        fun setPassive(): ByteArray {
            return setAll(2, 1, 0)
        }

        fun setMemory(): ByteArray {
            return setAll(3, 1, 0)
        }

        fun setStop(): ByteArray {
            return setAll(0, 0, 0)
        }

    }

    companion object {

        private const val param = 32768

        fun Short.toUnsigned(): Int {
            return if (this >= 0) this.toInt()
            else this + (2 * (Short.MAX_VALUE.toInt() + 1))
        }

        private fun Int.formatPToRad(): Float {
            return (12.5F * (this - param)) / param
        }

        fun Int.formatPToAngle(): Float {
            return 180 * formatPToRad() / PI.toFloat()
        }
    }

}