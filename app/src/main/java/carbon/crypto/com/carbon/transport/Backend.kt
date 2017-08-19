package carbon.crypto.com.carbon.transport;

import java.io.Closeable

interface Backend : Closeable {
    fun sendApdu(apdu: ByteArray): ByteArray
    val persistent: Boolean
}