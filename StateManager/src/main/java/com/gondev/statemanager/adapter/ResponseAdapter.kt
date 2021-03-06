package com.gondev.statemanager.adapter

import com.gondev.statemanager.state.Status
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * https://medium.com/shdev/retrofit에-calladapter를-적용하는-법-853652179b5b
 */
class ResponseAdapter<T>(
    private val successType : Type
) : CallAdapter<T, Call<Status<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Status<T>> = ResponseCall(call)

    class Factory : CallAdapter.Factory() {
        override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

            // 먼저 리턴 타입의 로우 타입이 Call인지 확인한다.
            if (Call::class.java != getRawType(returnType)) return null
            // 이후 리턴타입이 제네릭 인자를 가지는지 확인한다. 리턴 타입은 Call<?>가 돼야 한다.
            check(returnType is ParameterizedType) {
                "return type must be parameterized as Call<Result<Foo>> or Call<Result<out Foo>>"
            }

            // 리턴 타입에서 첫 번째 제네릭 인자를 얻는다.
            val responseType = getParameterUpperBound(0, returnType)
            // 기대한것 처럼 동작하기 위해선 추출한 제네릭 인자가 Result 타입이어야 한다.
            if (getRawType(responseType) != Status::class.java) return null
            // Result 클래스가 제네릭 인자를 가지는지 확인한다. 제네릭 인자로는 응답을 변환할 클래스를 받아야 한다.
            check(responseType is ParameterizedType) {
                "Response must be parameterized as Result<Foo> or Result<out Foo>"
            }

            // 마지막으로 Result의 제네릭 인자를 얻어서 CallAdapter를 생성한다.
            val successType = getParameterUpperBound(0, responseType)

            return ResponseAdapter<Any>(successType)
        }
    }
}