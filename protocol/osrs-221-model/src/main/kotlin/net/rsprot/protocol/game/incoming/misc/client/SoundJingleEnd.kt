package net.rsprot.protocol.game.incoming.misc.client

import net.rsprot.protocol.message.IncomingMessage

/**
 * Sound jingle end packet is sent when a jingle finishes playing in the client,
 * used to resume normal music from the start again (basically informs the server
 * that it needs to reset its internal play-time counter back to zero).
 */
public class SoundJingleEnd(
    public val jingleId: Int,
) : IncomingMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundJingleEnd

        return jingleId == other.jingleId
    }

    override fun hashCode(): Int {
        return jingleId
    }

    override fun toString(): String {
        return "SoundJingleEnd(jingleId=$jingleId)"
    }
}