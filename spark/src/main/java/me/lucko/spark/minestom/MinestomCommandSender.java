/*
 * This file is part of spark.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucko.spark.minestom;

import java.util.function.BiPredicate;
import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

final class MinestomCommandSender extends AbstractCommandSender<CommandSender> {

    private final @NotNull BiPredicate<CommandSender, String> permissionHandler;

    public MinestomCommandSender(CommandSender delegate, @NotNull BiPredicate<CommandSender, String> permissionHandler) {
        super(delegate);
        this.permissionHandler = permissionHandler;
    }

    @Override
    public String getName() {
        if (this.delegate instanceof Player player) return player.getUsername();
        else if (this.delegate instanceof ConsoleSender) return "Console";
        return "unknown:" + this.delegate.getClass().getSimpleName();
    }

    @Override
    public UUID getUniqueId() {
        if (super.delegate instanceof Player player) return player.getUuid();
        return null;
    }

    @Override
    public void sendMessage(Component message) {
        this.delegate.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.permissionHandler.test(this.delegate, permission);
    }

}
