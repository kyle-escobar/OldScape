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
package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteSUB
import io.guthix.buffer.writeShortLEADD
import io.guthix.oldscape.server.dimensions.FloorUnit
import io.guthix.oldscape.server.dimensions.TileUnit
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class MapAnimPacket(
    private val id: Int,
    private val delay: Int,
    private val floor: FloorUnit,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 26

    override val enclOpcode = 2

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeByteSUB(floor.value)
        buf.writeShortLEADD(delay)
        buf.writeShortLEADD(id)
        buf.writeByte(posBitPack)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Byte.SIZE_BYTES + Short.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES
    }
}