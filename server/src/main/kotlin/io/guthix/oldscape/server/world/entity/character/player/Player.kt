/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.server.world.entity.character.player

import io.guthix.oldscape.server.action.Action
import io.guthix.oldscape.server.action.ConditionalContinuation
import io.guthix.oldscape.server.action.InitialCondition
import io.guthix.oldscape.server.net.state.game.outp.*
import io.guthix.oldscape.server.world.entity.Entity
import io.guthix.oldscape.server.world.entity.character.Character
import io.guthix.oldscape.server.world.mapsquare.floor
import io.guthix.oldscape.server.world.mapsquare.zone.tile.Tile
import io.guthix.oldscape.server.world.mapsquare.zone.tile.tile
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.reflect.KProperty

data class Player(
    val index: Int,
    var priority: Int,
    val username: String,
    var ctx: ChannelHandlerContext,
    override val attributes: MutableMap<KProperty<*>, Any?> = mutableMapOf()
) : Character(attributes), Comparable<Player> {
    val events = ConcurrentLinkedQueue<() -> Unit>()

    val actions = PriorityQueue<Action>()

    var rights = 0

    override val updateFlags: MutableList<PlayerInfoPacket.UpdateType> = mutableListOf()

    fun addAction(type: Action.Type, action: suspend Action.() -> Unit) {
        val cont = Action(type, this)
        cont.next = ConditionalContinuation(InitialCondition, action.createCoroutineUnintercepted(cont, cont))
        actions.add(cont)
    }

    fun initializeInterest(worldPlayers: Map<Int, Player>, xteas: List<IntArray>) {
        ctx.write(InterestInitPacket(this, worldPlayers, xteas, position.inZones))
    }

    fun setTopInterface(topInterface: Int) {
        ctx.write(IfOpentopPacket(topInterface))
    }

    fun setSubInterface(parentInterface: Int, slot: Int, childInterface: Int, isClickable: Boolean) {
        ctx.write(IfOpensubPacket(parentInterface, slot, childInterface, isClickable))
    }

    fun setInterfaceText(parentInterface: Int, slot: Int, text: String) {
        ctx.write(IfSettext(parentInterface, slot, text))
    }

    override fun compareTo(other: Player) = when {
        priority < other.priority -> -1
        priority > other.priority -> 1
        else -> 0
    }

    fun handleEvents() {
        while(events.isNotEmpty()) {
            events.poll().invoke()
        }
        actions.forEach { it.resumeIfPossible() }
        ctx.flush()
    }
}