/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.server.net.state.game.inp

import io.guthix.buffer.readStringCP1252
import io.guthix.oldscape.server.net.state.game.ClientEvent
import io.guthix.oldscape.server.event.ClientCheatEvent
import io.guthix.oldscape.server.net.state.game.GamePacketDecoder
import io.guthix.oldscape.server.net.state.game.VarByteSize
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class ClientCheatPacket : GamePacketDecoder(34, VarByteSize) {
    override fun decode(data: ByteBuf, size: Int, ctx: ChannelHandlerContext): ClientEvent {
        return ClientCheatEvent(data.readStringCP1252())
    }
}