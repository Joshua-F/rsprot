package net.rsprot.protocol.api.login

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import net.rsprot.buffer.extensions.g1
import net.rsprot.crypto.cipher.NopStreamCipher
import net.rsprot.crypto.cipher.StreamCipher
import net.rsprot.protocol.ClientProt
import net.rsprot.protocol.api.NetworkService
import net.rsprot.protocol.api.decoder.IncomingMessageDecoder
import net.rsprot.protocol.api.logging.networkLog
import net.rsprot.protocol.message.codec.incoming.MessageDecoderRepository

/**
 * The decoder for any login messages.
 */
public class LoginMessageDecoder(
    public val networkService: NetworkService<*>,
) : IncomingMessageDecoder() {
    override val decoders: MessageDecoderRepository<ClientProt> =
        networkService
            .decoderRepositories
            .loginMessageDecoderRepository
    override val streamCipher: StreamCipher = NopStreamCipher

    override fun readOpcode(
        ctx: ChannelHandlerContext,
        input: ByteBuf,
    ) {
        if (!networkService.loginHandlers.suppressInvalidLoginProts) {
            return super.readOpcode(ctx, input)
        }
        this.opcode = (input.g1() - streamCipher.nextInt()) and 0xFF
        val decoder = decoders.getDecoderOrNull(opcode)
        if (decoder == null) {
            networkLog(logger) {
                "Invalid login packet from channel ${ctx.channel()}': ${this.opcode}"
            }
            ctx.close()
            return
        }
        this.decoder = decoder
        this.length = this.decoder.prot.size
        state =
            if (this.length >= 0) {
                State.READ_PAYLOAD
            } else {
                State.READ_LENGTH
            }
    }

    private companion object {
        private val logger: InlineLogger = InlineLogger()
    }
}