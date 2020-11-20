package xyz.podd.piholestats

import android.os.Parcel
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.ArgumentMatchers.*
import org.mockito.invocation.InvocationOnMock

/**
 * Wraps around the [Parcel] and stores the values in-memory.
 * https://gist.github.com/milosmns/7f6448a3602595948449d3bfaff9b005
 */
class MockParcel {

    companion object {
        @JvmStatic
        fun obtain(): Parcel {
            return MockParcel().mockedParcel
        }
    }

    private var position = 0
    private val store = mutableListOf<Any>()
    private val mockedParcel = mock<Parcel>()

    init {
        setupWrites()
        setupReads()
        setupOthers()
    }

    private fun setupWrites() {
        val answer = { i: InvocationOnMock ->
            with(store) {
                add(i.arguments[0])
                get(lastIndex)
            }
        }

        whenever(mockedParcel.writeString(anyString())).thenAnswer(answer)
        whenever(mockedParcel.writeValue(any())).thenAnswer(answer)
    }

    private fun setupReads() {
        val answer = { _: InvocationOnMock ->
            store[position++]
        }

        whenever(mockedParcel.readString()).thenAnswer(answer)
        whenever(mockedParcel.readValue(anyOrNull())).thenAnswer(answer)
    }

    private fun setupOthers() {
        val answer = { i: InvocationOnMock ->
            position = i.arguments[0] as Int
            null
        }

        whenever(mockedParcel.setDataPosition(anyInt())).thenAnswer(answer)
    }

}