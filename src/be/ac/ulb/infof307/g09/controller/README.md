# Controller package :

Here you will find the packages for everything concerning the controller part of the application.

## Packages :

### UCC :
<p>
The UCC or "Use Case Controllers" are the controllers. There is one accessible method per use case, the methods are grouped by object they process. 
(eg. all the use case related to the user are in UserUCC) 
</p>

<p>
The DTO (Data Transfer Objects)  are used to communicate between the controllers and the view/model. 
DTO are simple class made only of getters and setters. Using DTO avoid the usage of too many parameters in method call, thus making refactoring easier.  
</p>

<p>
The factories are classes that return a new instance of the best implementation of a DTO based on the given parameters
</p>

<p>
Canvas: is the link between the shapes on screen and the Tikz editor
</p>

<p>
Shape: is the java model for the code Tikz
</p>
