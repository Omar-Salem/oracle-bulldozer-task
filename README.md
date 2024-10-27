# Site Clearing Simulation

## Design
The class `Simulation` represents the body of the program, It stores information about the `Bulldozer` and the `ConstructionSite`. The `Square` class represents one cell of the sites's grid.

## Approach
General approach was "_How do I split this into models? how would they interact with each other? what should each class be responsible for?_"
- Began with figuring out the kinematics and odometery of the `Bulldozer` model, assuring accuracy of its pose (*position, direction*) as it moves.
- Created `Square` to represent the site blocks.
- Creating `ConstructionSite` to hold map data.
- Creating `Simulation` and how it interacts with `ConstructionSite` and `Bulldozer`.
- Updated `Square` to handle penalty logic and updating status when visited, idea was separating concern between how a map gets updated and how penalty is computed.
- *Test and verification with every step.*

## Run Tests
```bash
mvn site
```
report will be available at `target/site/surefire.html`

## Run App
```bash
mvn clean package -DskipTests
java -jar target/oracle-bulldozer-task-1.0.0.jar <map path>
```


## Assumptions
- Map file exists, is not malformed, no illegal characters
- Map fits in memory
- Number of operations fits in memory
- Operations' arguments are legal (*supplied and of correct type*)