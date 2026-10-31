# PetTeleport-OG

PetTeleport-OG keeps pets from being left behind. The server unloads chunks that no player is in, so a pet left standing in one is stranded when its owner teleports away. PetTeleport-OG teleports each standing tamed pet to its owner when the chunk the pet is in unloads, or when a player leaves the world the pet is in.

It is the [TrueOG Network](https://trueog.net) fork of [NotAShelf/pet-teleport](https://github.com/NotAShelf/pet-teleport), which is itself a fork of Chailotl's original pet-teleport (that repository is no longer online).

<div align="center">
  <img src="https://i.imgur.com/nbE7F5i.gif" alt="PetTeleport-OG in action">
</div>

## Target

[Purpur](https://git.trueog.net/trueog/Purpur) 1.19.4, Java 17.

## Authors

Raus, NotAShelf, NotAlexNoyle.

## Differences from upstream

- Targets Purpur 1.19.4 and builds with Gradle instead of Maven.
- Ships the TrueOG Network live configuration.
- Adds a WorldGuard `pet-teleport` region flag.
- Does not teleport pets to a player who is in a [Duels-OG](https://git.trueog.net/trueog/Duels-OG) match or a [Spleef-OG](https://git.trueog.net/trueog/Spleef-OG) game.

## Usage

Tame a pet and leave it standing; sitting pets stay where they are. The plugin works automatically and has no commands or permissions.

Only pets that can sit (wolves, cats, and parrots) are teleported, and only while their owner is online.

## Configuration

Each option is documented inline in [`config.yml`](src/main/resources/config.yml), including what it does and the shipped default value.

```yaml
# Whether to log pet teleports to the server console.
# Default: true
logPetTeleports: true

# Worlds that pets may or may not teleport to, depending on isBlacklist below.
# Default: [world, world_nether, world_the_end]
worldList:
  - world
  - world_nether
  - world_the_end

# Whether worldList acts as a blacklist.
# false = whitelist mode, so pets may only teleport to worlds in worldList.
# true = blacklist mode, so pets may not teleport to worlds in worldList.
# Default: false
isBlacklist: false
```

The shipped defaults are a whitelist of the three vanilla worlds (`world`, `world_nether`, `world_the_end`), rather than the
sample blacklist (`lobby`, `hub`, `some_other_world`) used by upstream.

## WorldGuard integration

If [WorldGuard](https://git.trueog.net/trueog/WorldGuard) is installed, PetTeleport-OG registers a `pet-teleport` state flag
you can set per region to gate pet teleports into that region:

```
/region flag <region> pet-teleport allow
/region flag <region> pet-teleport deny
```

The flag defaults to `allow`, so regions without it explicitly set behave exactly as they did before. When set to
`deny`, pets will not be teleported to a player standing inside the region. This only applies when WorldGuard is
installed; otherwise PetTeleport-OG continues without region checks.

## Duels-OG and Spleef-OG

Both are optional. While a player is in a Duels-OG match or a Spleef-OG game, their pets are not teleported to them, so pets are never pulled into an arena.

## Building

```sh
./gradlew build
```

The build uses a GraalVM JDK 17 toolchain. The plugin jar is written to `build/libs/PetTeleport-OG-<version>.jar`.

## License

Following the original license, this repository is also licensed under the [GPL-3.0 license](LICENSE).
