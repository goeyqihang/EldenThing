# Elden Thing

![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)
![Build: Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Tests: JUnit 6](https://img.shields.io/badge/tests-JUnit%206-25A162?logo=junit5&logoColor=white)

A turn-based, text-mode roguelike written in Java: a lone farmer tries to heal a cursed valley, one
turn at a time.

The gameplay is simple; the focus of the project is **object-oriented design**. It was built by a
team of five with a design-first workflow: each game system was specified up front with UML class
and sequence diagrams and a design rationale comparing alternatives, then implemented on top of a
lightweight turn-based engine. The game grew over two development milestones by extending the
engine's classes, aiming for SOLID designs that stay open to new content.

```text
xxxx...xxxxxxxxxxxxxxxxxxxxxxx........xx
xxx.....xxxxxxx..xxxxxxxxxxxxx.........x
..........xxxx....xxxxxxxxxxxxxx.......x
....xxx...........xxxxxxxxxxxxxxx.....xx
...xxxxx...........xxxxxxxxxxxxxx.....xx
...xxxxxxxsxx.......xxxxxxxx...xx......x
....xxxxxxxxxx......m.xxxxxx...xxx......
....xxxxxxxxxxx.........xxx....xxxx.....
....xxxxxxxxxxx................xxxx.....
...xxxx...xxxxxx.....#####.....xxx......
...xxx....xxxxxxx.b.y#___#.....xx.......
..xxxx...xxxxxxxxx...#_@o#....xx........
xxxxx...xxxxxxxxxx...##_##...xxx...k...x
xxxxxg.xxxxxxxxxxx....~....xxxxx......xx
xxxxx..xxxxxxxxxxxx....A..xxxxxx......xx
Farmer Health: (100/100) Stamina: (200/200) Mana: (200/200) Runes: 10000
Page 1/1
8: Farmer moves North
9: Farmer moves North-East
6: Farmer moves East
2: Farmer moves South
4: Farmer moves West
7: Farmer moves North-West
5: Farmer does nothing
a: Farmer drops the Poison Spell
b: Farmer casts Heal Spell on Farmer (Cost: 30 Mana)
c: Farmer drops the Heal Spell
d: Farmer casts Teleport Spell on Farmer (Cost: 20 Mana)
...
```

## Features

- **Two connected maps**: the *Valley of the Inheritree* and *Limveld*, linked by teleportation
  gates.
- **Curing and farming**: use a Talisman to turn cursed Blight into Soil, then plant an Inheritree
  (purifies the Blight around it and heals anyone standing next to it) or a Bloodrose (hurts
  everything around it).
- **Creatures with life cycles**: Spirit Goats and Omen Sheep suffer from Crimson Rot and collapse
  unless cured. Omen Sheep lay eggs that hatch after a few turns, Spirit Goats multiply near blessed
  trees, and Golden Beetles follow the farmer and lay eggs that hatch next to cursed ground. Eggs
  and beetles can be eaten for rewards.
- **NPCs with conditional dialogue**: Kale's lines change depending on your runes, your inventory
  and whether cursed ground is nearby. Guts attacks anyone healthy enough to be worth fighting.
- **Merchants**: Sellen and Kale sell weapons, and the same weapon comes with different side
  effects depending on who sells it. For example, a Broadsword from Sellen raises your maximum
  health, while the same sword from Kale raises your maximum stamina.
- **Spellcasting**: a mana system and four spell books: Fire (area damage that leaves the ground
  burning), Heal, Poison (damage over time) and Teleport.
- **Fishing**: fish at the pond with a rod. Every catchable item has its own catch chance, from a
  useless Old Boot to a rare Golden Fish worth 1000 runes, and a Shovel digs up plants.
- **A boss that grows**: whenever nobody stands next to it, the Bed of Chaos in Limveld sprouts
  Branches and Leaves. Branches grow more parts, Leaves heal it, and every part makes its attacks
  stronger.

## Design highlights

| Idea | Where | Why |
| --- | --- | --- |
| **Strategy** | `BehaviourSelector` (`PriorityBehaviourSelector`, `RandomBehaviourSelector`), injected into every `Creature` | The same creature acts by priority in the valley and at random in Limveld; new selection strategies need no changes to creature classes. |
| **Template method** | `Creature.initializeBehaviours()`, `Egg.getHatchingRules()` | The base class owns the workflow (choosing an action each turn, hatching); subclasses fill in only what differs. |
| **Composite-style boss parts** | `BossPart`, implemented by `Branch` and `Leaf` | The boss's damage is the sum of its parts, and each part decides how it grows. |
| **Capabilities instead of `instanceof`** | `GeneralCapability`, `GroundCapability` | Anything that can cure, fish, dig, be followed or burn declares it with a capability, so interactions don't depend on concrete classes. |
| **Small role interfaces** | `Curable`, `Rotatable`, `Eatable`, `Fishable`, `Purchasable`, `Growable`, `ActorProducible`, `Speakable` | Classes opt in to exactly the roles they play; `SpiritGoat`, for example, is `Curable`, `Rotatable` and `ActorProducible`. |
| **Conditions as objects** | `Condition` (`LowHealthCondition`, `NearbyCapabilityCondition`, `TurnBasedCondition`, ...) | NPC dialogue and egg hatching rules are assembled from reusable conditions instead of hard-coded `if`s. |
| **Effects as objects** | `Effect` (`HealEffect`, `SpawnActorEffect`, `IncreaseMaxStaminaEffect`, ...), combined in a `MerchantOffer` | An item's own effects and each merchant's bonuses combine freely. |
| **Wrapper** | `TemporaryGround` | Turns any ground into another one for a few turns (burning ground after a Fire Spell), then restores the original. |
| **Factories via `Supplier`** | `HatchingRules`, `SpawnActorEffect`, `MerchantOffer`, `Pond` | Constructors such as `OmenSheep::new` are passed around, so every hatch, purchase and catch creates a fresh object. |

## My role

I was one of the five developers. My main contributions:

- **Project foundation**: my implementation of the first milestone (Blight and Soil, curing with
  the Talisman, Crimson Rot on Spirit Goats and Omen Sheep, planting Inheritrees and Bloodroses)
  was chosen as the base of the team's codebase.
- **Conditions and hatching rules**: designed the `Condition` interface and its implementations,
  replacing earlier tightly coupled environment checks, and the `HatchingRules` system that lets
  each egg declare when and into what it hatches.
- **Shared role interfaces**: extracted `Eatable`, `Speakable` and the effect-based `Purchasable`
  model, so items and creatures can be eaten, NPCs listened to and weapons sold in one consistent
  way.
- **Bed of Chaos**: restructured the boss around `BossPart` (`Branch`, `Leaf`), moved its growth
  into a separate `growingparts` package, and made its damage scale with its parts.
- **Fishing and digging**: reworked the catchable items into the `Fishable` role interface and
  added the `Shovel` and `DigAction`.
- **Design and documentation**: drew the class and sequence diagrams for the
  [boss](docs/design/bed-of-chaos) and [fishing](docs/design/fishing) systems, and wrote Javadoc
  for over 60 classes.

## Getting started

Requires **JDK 17 or newer**. Maven does not need to be installed: the included Maven Wrapper
downloads it on first use.

```bash
./mvnw package
java -jar target/elden-thing.jar
```

On Windows:

```powershell
.\mvnw.cmd package
java -jar target\elden-thing.jar
```

`package` also runs the test suite; `./mvnw test` runs the tests on their own. In IntelliJ IDEA,
open the folder as a Maven project and run `game.Application`.

## How to play

Each turn the game draws the map and lists everything you can do. Type a key and press **Enter**.

- Move with the number keys in a numpad layout: `8` north, `2` south, `4` west, `6` east, and
  `7` `9` `1` `3` for diagonals. `5` waits a turn.
- Every other action (pick up, cure, plant, buy, listen, cast, fish, attack...) gets a letter.
- `>` and `<` switch pages when there are more than 26 actions.

The farmer starts with every spell book, a fishing rod, a shovel, both kinds of seed and 10,000
runes, so every system can be tried straight away. The Talisman (`o`) is right next to you: step
onto it and pick it up, because you need it to cure Blight and sick creatures. The game ends when
the farmer dies.

| Symbol | Meaning | Symbol | Meaning |
| --- | --- | --- | --- |
| `@` | The farmer (you) | `x` | Blight (cursed ground) |
| `.` | Soil | `#` / `_` | Wall / floor |
| `~` | Pond | `A` | Teleportation gate |
| `t` / `w` | Inheritree / Bloodrose | `^` | Burning ground |
| `y` / `m` / `b` | Spirit Goat / Omen Sheep / Golden Beetle | `T` | Bed of Chaos (boss) |
| `s` / `k` / `g` | Sellen / Kale / Guts | `o` / `0` / `*` | Talisman / egg / seed |

## Tests

The JUnit test suite in [`src/test/java`](src/test/java/game) has one test class per game system:
creature life cycles and reproduction, the boss's growth and damage scaling, NPC dialogue and
attack decisions, merchant offers and their stacked effects, conditions, fishing and digging,
temporary terrain, egg hatching, curing, planting and every spell. Randomness is handled by testing
invariants (for example, a fishing attempt never catches more than one item) rather than exact
outcomes, so the tests are deterministic.

[GitHub Actions](.github/workflows/ci.yml) builds the project and runs the tests on Java 17 and 21
for every push and pull request.

## Project structure

```text
.
├── pom.xml                          Maven build: JUnit tests and a runnable jar
├── mvnw, mvnw.cmd                   Maven Wrapper, so Maven does not need to be installed
├── .github/workflows/ci.yml         Builds and tests every push on Java 17 and 21
├── src/main/java/
│   ├── edu/monash/fit2099/engine/   Turn-based engine provided by the FIT2099 teaching team
│   └── game/                        All game content
│       ├── Application.java         Entry point: builds the maps and places everything
│       ├── actors/                  Player, creatures, the boss and its parts, NPCs
│       ├── behaviours/              AI behaviours and behaviour-selection strategies
│       ├── buying/                  Merchant offers and purchasing
│       ├── capabilities/            Capability flags shared across the game
│       ├── conditions/              Reusable conditions for dialogue and hatching
│       ├── eating/                  Eating
│       ├── effects/                 Effects applied by purchases, spells and catches
│       ├── fishing/                 Pond, fishing rod, catches and digging
│       ├── grounds/                 Terrain: Blight, Soil, walls, burning ground...
│       ├── growingparts/            Growth mechanics used by the boss
│       ├── hatching/                Eggs and their hatching rules
│       ├── healing/                 Curing with the Talisman
│       ├── plants/                  Seeds, plants and planting
│       ├── spells/                  Spell books and casting
│       ├── teleport/                Teleportation gates
│       ├── ui/                      Title banner
│       └── weapons/                 Weapons and attacking
├── src/test/java/game/              Unit tests, one class per game system
└── docs/
    ├── engine-uml.png               Class diagram of the engine
    └── design/                      One folder of design documents per game system
```

## Design documentation

Every system was designed before it was built. Each one has its own folder under
[`docs/design`](docs/design) with a class diagram, a sequence diagram of its key interaction, and a
design rationale that compares alternative designs against SOLID principles.

| System | Design rationale | Class diagram | Sequence diagram |
| --- | --- | --- | --- |
| Map travel and creature AI | [PDF](docs/design/map-travel-and-ai/design-rationale.pdf) | [PNG](docs/design/map-travel-and-ai/class-diagram.png) | [PNG](docs/design/map-travel-and-ai/sequence-diagram.png) |
| Creature reproduction and eggs | [PDF](docs/design/creature-reproduction/design-rationale.pdf) | [PNG](docs/design/creature-reproduction/class-diagram.png) | [PNG](docs/design/creature-reproduction/sequence-diagram.png) |
| Golden Beetle | [PDF](docs/design/golden-beetle/design-rationale.pdf) | [PNG](docs/design/golden-beetle/class-diagram.png) | [PNG](docs/design/golden-beetle/sequence-diagram.png) |
| NPCs and conditional dialogue | [PDF](docs/design/npc-dialogue/design-rationale.pdf) | [PNG](docs/design/npc-dialogue/class-diagram.png) | [PNG](docs/design/npc-dialogue/sequence-diagram.png) |
| Merchants and trading | [PDF](docs/design/trading/design-rationale.pdf) | [PNG](docs/design/trading/class-diagram.png) | [PNG](docs/design/trading/sequence-diagram.png) |
| Spellcasting | [PDF](docs/design/spellcasting/design-rationale.pdf) | [PNG](docs/design/spellcasting/class-diagram.png) | [PNG](docs/design/spellcasting/sequence-diagram.png) |
| Fishing | [PDF](docs/design/fishing/design-rationale.pdf) | [PNG](docs/design/fishing/class-diagram.png) | [PNG](docs/design/fishing/sequence-diagram.png) |
| Bed of Chaos boss | [PDF](docs/design/bed-of-chaos/design-rationale.pdf) | [PNG](docs/design/bed-of-chaos/class-diagram.png) | [PNG](docs/design/bed-of-chaos/sequence-diagram.png) |

The documents capture each system as it was originally designed; a few names have been tidied up
in the code since.

## Background

Elden Thing was developed in 2025 by a team of five as a semester-long project for Monash
University's FIT2099 (Object-Oriented Design and Implementation). The turn-based engine in
`src/edu/monash/fit2099/engine` and parts of the starter code (see the `@author` tags) were provided
by the FIT2099 teaching team; the game in `src/main/java/game` is built on top of it. The names and setting
are a light-hearted nod to *Elden Ring*.
