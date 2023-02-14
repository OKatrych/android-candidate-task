# NordLocker - Android Tech Task

Hello, in this task you will finish a ToDo List app :D

(1) Implement the main ToDo List screen
  - [X] Fetch from the API and show to the user. 
  - [X] User can click in a list item and navigate to Details (details screen) of that ToDo
  - [X] Make the list available offline.
  - [X] Sort list: Users can be able to sort the list to see recently updated or not completed tasks at the top.
    
(2) Implement a ToDo details screen
  - [X] Show the details from the ToDo to the user.
  - [X] User can be able to edit the information.
  - [X] User can be able to complete the ToDo.
    
(3) Modularization
  - [X] Modularize the network part so we can separate this part from the rest of the application.

(4) 
 - [X] Unit test at least 1 class.
 
 Short description of what was done:
 1. `TodoStorageService` is the app's source of truth. The main reason why the network service is not the source of truth is that it returns incomplete data (missing date fields, etc.).
2. I used the use cases in this app to reduce the repeating of logic.
3. I used the Groupie library to handle the RecyclerView boilerplate code, it uses DiffUtil behind the hood so we don't have to worry about it.
4. I tried to use the two different ways of defining the screen state with the `StateFlow` (see `TodoDetailsViewModel` and `TodoListViewModel`)
5. There are two sample tests: `LoadTodosUseCaseTest` and `TodoResponseMapperTest`.
6. The `LoadTodosUseCase` constantly observes the storage changes and if there is network connection it will also update the storage with the todos received from the network service.
