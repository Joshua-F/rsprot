package net.rsprot.protocol.game.incoming.codec.clan

import net.rsprot.buffer.JagByteBuf
import net.rsprot.protocol.ClientProt
import net.rsprot.protocol.game.incoming.clan.AffinedClanSettingsAddBannedFromChannelMessage
import net.rsprot.protocol.game.incoming.prot.GameClientProt
import net.rsprot.protocol.message.codec.MessageDecoder
import net.rsprot.protocol.metadata.Consistent

@Consistent
public class AffinedClanSettingsAddBannedFromChannelDecoder :
    MessageDecoder<AffinedClanSettingsAddBannedFromChannelMessage> {
    override val prot: ClientProt = GameClientProt.AFFINEDCLANSETTINGS_ADDBANNED_FROMCHANNEL

    override fun decode(buffer: JagByteBuf): AffinedClanSettingsAddBannedFromChannelMessage {
        val clanId = buffer.g1()
        val memberIndex = buffer.g2()
        val name = buffer.gjstr()
        return AffinedClanSettingsAddBannedFromChannelMessage(
            name,
            clanId,
            memberIndex,
        )
    }
}