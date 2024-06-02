package jp.aqua.shizen

import jp.aqua.shizen.dictionary.network.image.ImageApi
import jp.aqua.shizen.dictionary.network.image.ImageRequest
import jp.aqua.shizen.dictionary.network.image.ImageResponse
import jp.aqua.shizen.dictionary.network.translation.TranslateApi
import jp.aqua.shizen.dictionary.network.translation.TranslateRequest
import jp.aqua.shizen.dictionary.network.translation.TranslateResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTest {
    @Test
    fun yandex_api_works() {
        runBlocking {
            val request = TranslateRequest(texts = listOf("Insist"), to = "ru")
            var response: TranslateResponse
            println("translate " + measureTimeMillis {
                response = TranslateApi.retrofitService.translate(request)
            })
            assert(response.translations != null)
            assert(response.translations?.isNotEmpty() == true)
        }
    }

    @Test
    fun google_api_works() {
        runBlocking {
            val request = ImageRequest(q = "Apple")
            var response: ImageResponse
            println("image " + measureTimeMillis {
                response = ImageApi.retrofitService.image(request.q)
            })
            assert(response.items?.isNotEmpty() == true)
        }
    }
}

