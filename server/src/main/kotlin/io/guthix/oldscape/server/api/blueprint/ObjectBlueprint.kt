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
package io.guthix.oldscape.server.api.blueprint

import io.guthix.oldscape.cache.config.ObjectConfig
import io.guthix.oldscape.server.blueprints.ExtraObjectConfig
import io.guthix.oldscape.server.blueprints.ObjectBlueprint
import mu.KotlinLogging
import java.io.IOException

private val logger = KotlinLogging.logger {  }

object ObjectBlueprints {
    private lateinit var blueprints: Map<Int, ObjectBlueprint>

    operator fun get(index: Int): ObjectBlueprint {
        return blueprints[index] ?: throw IOException("Could not find blueprint $index.")
    }

    fun load(cacheConfigs: Map<Int, ObjectConfig>, extraObjConfigs: List<ExtraObjectConfig>) {
        val bps = mutableMapOf<Int, ObjectBlueprint>()
        extraObjConfigs.forEach { extraConfig ->
            extraConfig.ids.forEach {  id ->
                val cacheConfig = cacheConfigs[id] ?: error("Extra config for id $id is not found in the cache.")
                bps[id] = ObjectBlueprint(
                    cacheConfig.id,
                    cacheConfig.name,
                    extraConfig.weight,
                    extraConfig.examine,
                    cacheConfig.stackable,
                    cacheConfig.tradable,
                    cacheConfig.notedId,
                    cacheConfig.isNoted,
                    cacheConfig.placeholderId,
                    cacheConfig.isPlaceHolder,
                    cacheConfig.iop,
                    cacheConfig.groundActions,
                    extraConfig.equipment
                )
            }
        }
        blueprints = bps.toMap()
        logger.info { "Loaded ${blueprints.size} object blueprints" }
    }
}