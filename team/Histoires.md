# Histoires
Informations récapitulatives concernant les différentes histoires.

#### Quelques précisions
Un point correspond à une heure de travail par binôme (approximatif).  Par itération il faut accomplir X points.

----------------------


## Pondération

| Priority/3 | N° | Description | Risk/3 | Hours/? | Points |
| ------ | ------ | ------ | ------ | ------ | ------ |
| 1 | [1](#1-create-a-user-login-and-password) | Create a user, login and password | 2 | 38 | 24 |
|   | [4](#4-creation-of-diagrams) | Creation of diagrams | - | - | -- |
| 2 | [2](#2-project-management) | Project management: saving, changing the project name, displaying information | - | - | -- |
|  | [3](#3-importation-and-exportation-of-files) | Importation and exportation of files | 3 | 17 | 10 |
| 3 | - | Story X | - | -- | -- |
|   | - | Story X | - | -- | -- |

----------------------


## Description

### 1 Create a user, login and password

**Original instructions:**

- When starting the program, a visitor can create a new account. To do so, the visitor must first accept the terms of service defined by the system administrator.
- Subsequently, the visitor indicates his personal information (e.g. last name, first name, user name, e-mail address and desired password). The account will be created upon confirmation of the validity of the data entered.
-After confirmation, the visitor becomes a user who can log in to the system and modify his profile information. Any changes made to his profile require "confirmation of the validity of the new data", a procedure that must be triggered before the changes take effect.
- Only one user can be logged in at any time. 

**Tasks:**   

- Set up of project and database to save user information.
- Registration.
- Login.
- Modification of user info.

**Extra Tasks:**
- Creation of dashboard.

:question: **Question:**       

### 2 Project management

**Original instructions:**

- Each user logged in to the system can create a diagram only inside of a project. The user has two possibilities to do this:
    - create a new project by specifying its name;
    - use an already existing project.
- All the projects created or shared with the user are visible on the user's projects. This screen displays, for each project: 
    - its title
    - the name of its creator 
    - the date of last modification. 
- From this screen, the user can select one or several projects to: 
    - copy
    - delete
    - save them to a directory 
    - share them with another user. 
- In addition, the user can select a project to open it and enter in the edit mode or to change its title. When a project is in edit mode, the system keeps track of all changes made and automatically records them when the file itself is closed.


**Tasks:** 

**Extra Tasks:**

:question: **Question:** 

### 3 Importation and exportation of files

**Original instructions:**

- The user can import several files at once or a directory or a compressed archive (.tar.gz ) from his computer. Files already loaded by the user via the directory should not be loaded a second time.

**Tasks:** 

- Importation.
- Exportation.

**Extra Tasks:**

:question: **Question:** 

### 4 Creation of diagrams

**Original instructions:**

- The user has two possibilities to create a diagram :
    - **use the point-and-click method:** the user must first define the initial characteristics of the nodes and links he wants to use, thanks to a configuration panel where the user can choose the type of node (circle, rectangle, triangle, ...), the type of link (arc, edge, ...) and their graphical characteristics (color, thickness, label, ...). When an element (node/link) is defined, the user can select it and place it in a point-and-click frame containing a canvas. This action has the effect of placing the element on the canvas and at the same time producing the corresponding TikZ code which is visible in a second associated text frame.  
    - **use the TikZ language:** the user can use the text frame to describe his diagram in TikZ language. When the user completes his code, the frame containing the canvas will produce drawing the diagram automatically.

**Tasks:** 


**Extra Tasks:**

:question: **Question:** 