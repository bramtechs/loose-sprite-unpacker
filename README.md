# loose-sprite-unpacker
LooseSpriteUnpacker is a single program that detects loose sprites in a spritesheet and exports them to a target folder.

Make sure your tilesheet background is fully transparent.

> NOTE: GUI is only tested on Windows.

![Example screenshot](screenshot.png)

## Usage
```bash
java -jar sprite-unpacker.jar # opens up a GUI interface 
java -jar sprite-unpacker.jar ./spritesheet.png ./output
```

## Installation
Grab the release jar on the right of the screen or compile the program yourself with ```./gradlew jar```
