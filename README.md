# SunflowerFields
This plugin brings to game ability to create sunflower farms. You can grow up new sunflowers using sunflower seeds.

## Features
* Sunflowers seed are based on melon seeds (just renamed). Sunflower stems are melon stems, which will turn into sunflower after maturation.
* You must put sunflower seed into soil and it will grow.
* New recipe to get sunflower seeds from sunflower.
* You can nibble sunflowers seeds :)

## Commands
`/sunflower reload` — reload configuration

## Permission
`sunflowerseeds.nibble` — allows to nibble sunflower seeds

## Configuration
```
sunflower:
  seed-name: '&7&6&8&6Sunflower seed' # Name of sunflower seed
  stem-max-age: 5 # Age of stem when it will turn into sunflower 
  seed-craft-enable: true # Enable (true) sunflower seed recipe
  allow-manual-place: false # Allows (true) to place sunflowers as a block
  nibble:
    food-level-per-seed: 1 # HP amount restored by one consumed sunflower seed
    max-food-level-restore: 10 # Max HP-level that could be restored by nibbling sunflower seeds
```

## Download
* [Spigot](https://www.spigotmc.org/resources/sunflowerfields.15849/)
* [BukkitDev](https://dev.bukkit.org/projects/sunflowers)
* [RuBukkit](http://rubukkit.org/threads/fun-sunflowerfields-luzgaem-semechki-sazhaem-podsolnuxi-bukkitdev.129800/)