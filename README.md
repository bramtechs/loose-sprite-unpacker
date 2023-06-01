# loose-sprite-unpacker

LooseSpriteUnpacker is a single program that detects loose sprites in a sprite sheet and exports them to a target
folder.

I got tired of manually cutting out sprites from sprite sheets and couldn't find any program that does this
automatically, so I made one myself.

> NOTE: Make sure your sprite sheet background is fully transparent!

![Example screenshot](screenshot.png)

## Usage

```bash
java -jar sprite-unpacker.jar # opens up a GUI interface 
java -jar sprite-unpacker.jar ./spritesheet.png ./output # runs the program in CLI mode
```

## Installation

Grab the release jar on the right of the screen or compile the program yourself with ```./gradlew shadowJar```
