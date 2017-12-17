# Design 

### Design Goals

Our main design goal for the program was flexibility in the type of game being implemented. This included making sure the user could create many different types of game agents, have infinite levels, and expand extensible to multiple genres. We wanted to make sure that the data files our program used would be easily configured by non-programmers. We also wanted to make sure that the program would be robust to bad user input. Finally, we wanted the communication between frontend and backend modules to be as abstracted and simple as possible, so as to allow for collaborative editing and multiplayer extensions. 


### Adding new features

* To add new types of game elements, one would need to define new collision, firing, and/or behavior objects for them to use. This would entail implementing the interfaces defined for these behaviors (abstract classes between the interfaces and concrete classes are there and could be used to reduce the effort of making the new object, if applicable). 
    ** Caveat: The behavior object would need to include the [game element property annotation](src/engine/game_elements/ElementProperty.java) for each element specified in its constructor. For more complex types of objects which include circular references, note that there will need to be (GSON type adapters)[https://google.github.io/gson/apidocs/com/google/gson/TypeAdapter.html] included in the [serialization utils](src/utils/io/SerializationUtils.java)' GSON handler (i.e., via the [registerTypeAdapter() method](https://google.github.io/gson/apidocs/com/google/gson/GsonBuilder.html#registerTypeAdapter-java.lang.reflect.Type-java.lang.Object-)). Finally, maps and other collections will either need their own type adapters or would need to only use strings (which would be unpacked at construction time into the desired type, e.g., double), which would allow GSON to handle them automatically. 

* 

### Design choices 

* Grid based vs precise coordinate based movement: 


### Assumptions 


