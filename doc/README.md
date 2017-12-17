# voogasalad

### Contributors 

* Adithya Raghunathan (ra102@duke.edu)
* Ben Schwennesen (bas65@duke.edu) 
* Ben Welton (bw144@duke.edu)
* Matthew Mosca (mjm141@duke.edu)
* Matthew O'Boyle (mo121@duke.edu)
* Michael Scruggs (mts40@duke.edu)
* Santo Grillo (sdg12@duke.edu)
* Tyler Yam (tjy8@duke.edu)
* Venkat Subramaniam (vms23@duke.edu)
 

### Timeline

The project began in earnest on 9 November, 2017, when we wrote our team contract, and completed on 15 December, 2017. We estimate that, on *average*, each team member spent 120 hours on the project (with a wide variation range among individual contributors).

### Roles 

* Adithya Raghunathan:
* Ben Schwennesen: overall my role was designer and implementer of the backend (game engine), working closesly with Adi. My primary role during the first sprint was engine API design and working on the game actors, specifically their construction, the passing and receiving of the properties that was needed for them in the frontend, coordination of their behavior, and saving to easily edited files; I also worked on controllers (which featured heavily in the API design), serialization, behavior objects, and the game loop with other group members during the first sprint. Finally, I worked on a chat window and server during this sprint. In addition to expanding my work in these during the second sprint, I worked on element upgrading, exporting games to JAR files and pushing them to Google Drive, levels and waves, integrating the paths defined in the frontend into the game engine, some GUI simplification by moving buttons into a dropdown menu (among others), and finally many, many bug fixes.
* Ben Welton:
* Matthew Mosca:
* Matthew O'Boyle:
* Michael Scruggs:
* Santo Grillo:
* Tyler Yam:
* Venkat Subramaniam:
 

### Resources Used 

See [src/README.md](src/README.md) for some image credits. Some references are also included in code documentation for sites used in developing some resources.


### Starting the Project

Use [src/main/Main.java](src/main/Main.java) to launch the project (the exported JAR uses the other class in [src/main](src/main).


### Testing 

The [test](/test) directory contains test code for the project. Error the project should handle without crashing: 
* Not setting all the fields in a game element definition.
* Failure to export the project in full.
* 


### Data

The program uses a few data files for saving. All of these—excluding exported game files which are included in JARs in (data/games/)[data/games] (note: see known bugs)—are included in the [authoring](/authoring) directory. Files included: 
* [.voog files](authoring/BasicGame.voog): these JSON files contain high level information about the game, such as its name, description, victory conditions, and genre specific information like resources and health-per-level, unit costs, etc.
* [Element template files](authoring/sprite-templates/BasicGame/): these properties file specify the properties chosen for each game element (actor, or even background object) defined in the authroing environmnet, such as speed, collision effects, image dimensions, health, etc. These can be easily edited by people without code knowledge.
* [Wave files](authoring/waves/BasicGame/): these define the types of waves included in each level (other information about the waves themselves are included in element template files) and their spawn location. Again, these are easily edited to move the spawn location or exclude a wave from a level.
* [Path serializations](authoring/serializations/): these files were used to persist the paths created during authoring; they are not human readable because, due to a need for circular referenced in supporting circular paths, XML/JSON serialization libraries struggled to handle them, so Java's native serialization functionality was utilized.
 

### Using the Program

Special key presses: 
* To move an object in authoring you must select the button in the top-center with the four arrows pointing in the cardinal directions.
* 
* 


### Assumptions and Simplifications

* In order to get multiplayer and collaborative editing working without paying for a domain, we assumed that authors and players would be on the same network. (We mostly used localhost for using these features but had a Duke VM server prepared for this, which we didn't end up using because the server code changed so often.)
* A major assumption we made was that the user would always save their data to the same location. This simplified loading for us, but should have been made more clear by simply asking for a save name for the user instead of opening a file chooser when they tried to save.


### Known Bugs 

* Due to last minute changes that used resource bundles in various unexpected ways as well as the inclusion of media such as audio clips (which cannot be loaded as streams), the exported JAR files currently do not run correctly. At the time this feature was written (when it was shipped as our utility), it worked perfectly. 

### Extra Features

* Exporting (buggy due to last minute changes to IO)
* Collaborative editing
* Multiplayer
* Change of language


### Impressions

We all agreed that this project would be much more enjoyable and would end up being much better if one of the middle projects (cell society or SLogo) were eliminated or shortened (e.g., only one implementation sprint for cell society and SLogo) to make the timeline for completion more realistic for a full-fledged game authoring environment. (At the moment, it seems like the teams that have more people in less difficult classes would end up with much better products than the teams whose members have very full schedules already.)