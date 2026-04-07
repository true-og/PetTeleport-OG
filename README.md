<h1 align="center">🐾 PetTeleport-OG</h1>

<div align="center">
  <img src="https://i.imgur.com/Bwili3Z.png">
</div>

<div align="center">
  <code><img title="License" href="LICENSE" src="https://img.shields.io/github/license/true-og/PetTeleport-OG?style=flat-square"></code>
  <code><img title="Issues" href="https://github.com/true-og/PetTeleport-OG/issues" src="https://img.shields.io/github/issues/true-og/PetTeleport-OG?style=flat-square"></code>
</div>

Ever teleported somewhere in Minecraft only for your pets to not follow you? I have! The problem is that the server instantly unloads unoccupied chunks, often with
your pet in stow! This plugin ensures that all standing pets teleport to you if they ever end up in an unloaded chunk. Handy!

`PetTeleport-OG` is the [TrueOG Network](https://trueog.net) maintained fork of [NotAShelf/pet-teleport](https://github.com/NotAShelf/pet-teleport), which is itself a fork of [Chailotl/pet-teleport](https://github.com/Chailotl/pet-teleport).

## Differences from Upstream

- Built for Purpur 1.19.4.
- Builds with TrueOG Network live config file.
- Adds WorldGuard integration via the `pet-teleport` region flag.

## Usage

<div align="center">
  <a><img src="https://i.imgur.com/nbE7F5i.gif"></a>
  <br/>
</div>
<br>

Tame your desired pets and the plugin will automatically work when you teleport. There is practically no manual input from the player required.
Other than, well, moving around with your pets.

### Commands

None!

### Permissions

None!

### Configuration

```yaml
logPetTeleports: true

worldList:
  - world
  - world_nether
  - world_the_end

isBlacklist: false
```

| Option          | Description                                                                                                           | Type      |
| --------------- | --------------------------------------------------------------------------------------------------------------------- | --------- |
| logPetTeleports | Whether to log pet teleports in the server console                                                                    | `boolean` |
| worldList       | A list of worlds that pets may or may not teleport to                                                                 | `list`    |
| isBlacklist     | Whether the `worldList` option will act as a blacklist. <br>If false, pets may only teleport to the world in the list | `bool`    |

The shipped defaults are a whitelist of the three vanilla worlds (`world`, `world_nether`, `world_the_end`), rather than the
sample blacklist (`lobby`, `hub`, `some_other_world`) used by upstream.

### WorldGuard Integration

If [WorldGuard](https://enginehub.org/worldguard) is installed, PetTeleport-OG registers a `pet-teleport` state flag
you can set per region to gate pet teleports into that region:

```
/region flag <region> pet-teleport allow
/region flag <region> pet-teleport deny
```

The flag defaults to `allow`, so regions without it explicitly set behave exactly as they did before. When set to
`deny`, pets will not be teleported to a player standing inside the region.

## License

Following the original license, this repository is also licensed under the [GPL-3.0 license](LICENSE).
