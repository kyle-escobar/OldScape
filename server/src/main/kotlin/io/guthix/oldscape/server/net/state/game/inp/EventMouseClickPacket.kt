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

import io.guthix.oldscape.server.event.MouseClickEvent
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.GamePacketDecoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class EventMouseClickPacket : GamePacketDecoder(37, FixedSize(6)) {
    override fun decode(data: ByteBuf, size: Int, ctx: ChannelHandlerContext): MouseClickEvent {
        val bitPack = data.readShort().toInt()
        val mouseX = data.readShort().toInt()
        val mouseY = data.readShort().toInt()
        return MouseClickEvent(bitPack and 0x1 == 1, bitPack shr 1, mouseX, mouseY)
    }
}