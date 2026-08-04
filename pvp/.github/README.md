# Polyp

Configurable PvP mechanics for [Minestom](https://minestom.net/), built for mixed-version servers: modern (1.21.x)
and legacy (1.8, via ViaVersion/ViaBackwards/ViaRewind) clients play the same 1.8-style combat on one
modern server, with the mechanics themselves tunable per scope down to individual knockback constants.

## What's in the box

- **Combat systems** — attack, damage, knockback, blocking, attributes/enchants, projectiles (bow, snowball, egg,
  pearl, splash potion, fishing rod, fireball), explosions/TNT, hunger and natural regen, consumables, death handling.
  Every system installs independently and reads its tuning from a profile.
- **Profiles** — a `MechanicsProfile` bundles per-system configs and resolves per player → world → instance → global,
  so one server can run different mechanics per arena, kit, or player.
- **Presets** — `Vanilla18` (source-accurate 1.8 baseline), plus measured recreations of live servers in
  `io.github.term4.polyp.presets`: `Mmc18` (MineMenClub) and `Hypixel`, reverse-engineered from packet
  captures. `Preset.MMC18.profile()` is a working server setup.
- **Cross-version compat** (`Compat18`) — makes a modern client behave like 1.8: attack-box stamping, sword-block
  pose, throw-swing suppression, bare-fist swing fill, elytra/offhand/sprint/pose/placement restrictions, 1.8 eye
  heights and hitboxes. Legacy clients get wire-accurate 1.8 velocity and a set of client-fix packets.
- **TPS-independent** — combat windows and physics scale with the configured tick rate.

## Quick start

```java
Polyp polyp = Polyp.getInstance();
polyp.init();

polyp.profiles().setGlobal(Preset.MMC18.profile().toBuilder()
        .set(MechanicsKeys.COMPAT, Compat18.config())   // 1.8 behavior for modern clients
        .set(MechanicsKeys.FIXES, Fixes18.config())     // legacy-client wire fixes
        .build());

AttackSystem.install(polyp);
DamageSystem.install(polyp);
KnockbackSystem.install(polyp);
ProjectileSystem.install(polyp);
// ...each system is optional; see test/ExampleServer.java for a full setup
```

Requires the published `net.minestom:minestom` artifact (see `minestomVersion` in the build) — no Minestom fork.

## Status

Pre-release. APIs move; presets are updated as captures refine the measured values.
