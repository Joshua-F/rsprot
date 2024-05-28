package net.rsprot.protocol.game.incoming.codec.events

import net.rsprot.buffer.JagByteBuf
import net.rsprot.protocol.ClientProt
import net.rsprot.protocol.game.incoming.events.EventNativeMouseMove
import net.rsprot.protocol.game.incoming.events.util.MouseMovements
import net.rsprot.protocol.game.incoming.prot.GameClientProt
import net.rsprot.protocol.message.codec.MessageDecoder
import net.rsprot.protocol.metadata.Consistent
import net.rsprot.protocol.tools.MessageDecodingTools

@Suppress("DuplicatedCode")
@Consistent
public class EventNativeMouseMoveDecoder : MessageDecoder<EventNativeMouseMove> {
    override val prot: ClientProt = GameClientProt.EVENT_NATIVE_MOUSE_MOVE

    override fun decode(
        buffer: JagByteBuf,
        tools: MessageDecodingTools,
    ): EventNativeMouseMove {
        val averageTime = buffer.g1()
        val remainingTime = buffer.g1()
        val array = threadLocalArray.get()
        var count = 0
        while (buffer.isReadable) {
            var packed = buffer.g1()
            var deltaX: Int
            var deltaY: Int
            var timeSinceLastMovement: Int
            if (packed and 0xE0 == 0xE0) {
                timeSinceLastMovement = packed and 0x1f shl 8 or buffer.g1()
                deltaX = buffer.g2s()
                deltaY = buffer.g2s()
                if (deltaY == 0 && deltaX == -0x8000) {
                    deltaX = -1
                    deltaY = -1
                }
            } else if (packed and 0xC0 == 0xC0) {
                timeSinceLastMovement = packed and 0x3f
                deltaX = buffer.g2s()
                deltaY = buffer.g2s()
                if (deltaY == 0 && deltaX == -0x8000) {
                    deltaX = -1
                    deltaY = -1
                }
            } else if (packed and 0x80 == 0x80) {
                timeSinceLastMovement = packed and 0x7f
                deltaX = buffer.g1() - 128
                deltaY = buffer.g1() - 128
            } else {
                packed = (packed shl 8) or (buffer.g1())
                timeSinceLastMovement = (packed ushr 12) and 0x7
                deltaX = ((packed shr 6) and 0x3F) - 32
                deltaY = (packed and 0x3F) - 32
            }
            val change =
                MouseMovements.MousePosChange(
                    timeSinceLastMovement,
                    deltaX,
                    deltaY,
                )
            array[count++] = change.packed
        }
        val slice = array.copyOf(count)
        return EventNativeMouseMove(
            averageTime,
            remainingTime,
            MouseMovements(slice),
        )
    }

    private companion object {
        /**
         * Utilizing a thread-local initial long array, as the number of
         * mouse movements is unknown (relies on remaining bytes in buffer,
         * which in turn uses compression methods so each entry can be 2-4 bytes).
         * As Netty's threads decode this, a thread-local implementation is
         * perfectly safe to utilize, and will save us some memory in return.
         */
        private val threadLocalArray =
            ThreadLocal.withInitial {
                LongArray(128)
            }
    }
}
