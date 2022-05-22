# Multi-Agent Surveillance

A more descriptive readme file will follow in phase 3 :)

## Gamemodes
### Exploration
The Guards have to explore as much of the map as they can. The game ends if no coverage progress is made in a certain amount of time.

### Guards vs. Intruders
The game ends when either the guards have captured all intruders, or when all alive intruders have been to the target area at least once.


## Agent types

- **Random**  
Constantly walking while changing rotation arbitrarily.
- **Remote**  
Controllable by the user. Move forward using `W` and turn left or right with `A` and `D`.
- **Bug-0**  
Exploration agent, has a specific target direction in mind and tries to walk to it.
- **Ant colony**  
to be elaborated upon in phase 3...
- **NEAT**  
Movement & rotation determined via a neural network. This is a pretty neat agent...

## Running
With IntelliJ, a GUI instance can be started via the 'run' task in the Gradle menu. If not using IntelliJ, simply use the command `gradle run` in the main directory.

**Suggested setup:**

| Item          | Value                   |
|---------------|-------------------------|
| Gamemode      | Guards vs Intruders     |
| Guard type    | NEAT Agent              |
| Intruder type | Bug Agent               |
| Map           | File map (phase2_2.txt) |


## Related stuff
- [M.A.S. Map Editor](https://github.com/christophersch/MAS_Map_Editor), used to create custom maps in our experiments.


## Authors
Martin Aviles, Lena Feiler, Willem Ploegstra, Konstantin Sandfort, Christopher Schiffmann