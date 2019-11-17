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
package io.guthix.oldscape.server.world.mapsquare.zone

import io.guthix.oldscape.server.world.mapsquare.HeightDim
import io.guthix.oldscape.server.world.mapsquare.MapSquare
import kotlin.math.abs

class Zone(val z: HeightDim, val x: ZoneDim, val y: ZoneDim) {
    val mapSquare = MapSquare(z, x.md, y.md)

    fun withInDistanceOf(other: Zone, distance: ZoneDim) = if (z == other.z) {
        abs((other.x - x).dim) <= distance.dim && abs((other.y - y).dim) <= distance.dim
    } else {
        false
    }
}