# Histoires
Informations récapitulatives concernant les différentes histoires.

#### Quelques précisions
Un point correspond à une heure de travail par binôme (approximatif).  Par itération il faut accomplir X points.

----------------------


## Pondération

| Priority/3 | N° | Description | Risk/3 | Hours/? | Points | Status | Introduced in iteration | 
| ------ | ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| 1 | [1](#1-create-a-user-login-and-password) | Create a user, login and password | 2 | 38 | 24 | Done | 1 |
|   | [4](#4-creation-of-diagrams) | Creation of diagrams | 1 | 26 | 40 | Ongoing | 2
| 2 | [2](#2-project-management) | Project management: saving, changing the project name, displaying information | 2 | - | 37 | To do | - | 
|   | [3](#3-importation-and-exportation-of-files) | Importation and exportation of files | 3 | 25 | 10 | Done | 1 |
|   | [6](#6-overview) | Overview | 2 | - | 15 | To do | - |
|   | [7](#7-drag-and-drop) | Drag and Drop | 2 | - | 15 | To do | - |
|   | [10](#10-support-for-specific-tikz-libraries) | Support for specific TikZ libraries | 1 | - | 15 per library | To do | - |
| 3 | [5](#5-relative-placement) | Relative placement | 2 | - | 10 | To do | - |
|   | [8](#8-integration-with-existing-cloud-services) | Integration with existing cloud services | 3 | - | 14 + 6 per platform | To do | - |
|   | [9](#9-versioning) | Versioning | 2 | - | 8 | To do | - |
|   | [11](#11-tikz-syntax-highlighting) | TikZ syntax highlighting | 2 | - | 20 | To do | - |
|   | [12](#12-help Section) | Help Section | 3 | - | 13 | To do | - |
|   | [13](#13-data-security) | Data security | 1 | - | 16 | To do | - |
|   | [14](#14-data-integrity) | Data integrity | 1 | - | 8 | To do | - |
|   | [15](#15-copy-paste) | Copy-paste | 2 | - | 10 | To do | - |


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

- Create a conversion architecture from canvas to TikZ and vice versa.
- Save the current diagram.
- Close the current diagram.
- Automatically write the TikZ code of the diagram.
- Point and click:
  - Draw different shapes (square, circle, triangle and lines).
  - Select fill and stroke color of a shape. 
  - Delete a shape.
  - Change the color of an existing shape.

**Extra Tasks:**

:question: **Question:** 

### 5 Relative placement

**Original instructions:**

- The TikZ language allows to describe the placement of nodes in a relative way. The user can choose to use this description for his diagram, i.e. during the creation of a diagram, the user can describe the placement of a node relative to the position of another existing node, either by using the TkiZ language syntax in the text frame or by using the point-and-click method on the canvas.
In both cases, the system must be able to produce the TikZ code with the placement corresponding relative. 

**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 6 Overview

**Original instructions:**

- The system offers a preview functionality in which it is possible to generate a pdf/image file of the diagram.
To do this, the system performs a LATEX compilation of the TkiZ code described in the diagram and generates the corresponding image/pdf file. The user can immediately view the result.
 
**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 7 Drag and Drop

**Original instructions:**

- The user can build a TikZ diagram in a modular way, using the following elements diagram graphics, predefined in the software (e.g. round knots, square knots, arrows, etc.), axes, ...).
To do this, you must first display these predefined elements in a dedicated part of the graphical interface. 
Then, the user clicks on a graphical element with the main mouse button (the left in right-handed mode, the right in left-handed mode), and, by keeping this button pressed, it will drag the object to its destination on the diagram.
Once the target is reached, the user will release the button and the desired object will appear. in the diagram.
When the drag and drop has been completed, the code corresponding to the diagram should be updated in the part of the graphical interface provided for this purpose.
 
**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 8 Integration with existing cloud services

**Original instructions:**

- Each user has an amount of disk space defined by the administrator, in order to to be able to manage the storage of these projects.
- This space can be extended through integration with cloud storage services (e.g. Dropbox, Google Drive, Github).
- The user must be able to export projects to the desired web service as well as to import file stored on the web service in the system.
- The export/import can be done for one or more files at a time. The system will have to notify the user if he is downloading a file that already exists. in the system.
- The integration will have to be done using the dedicated APIs provided by the the same.
 
**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 9 Versioning

**Original instructions:**

- The user has access to a versioning system via the application.
- The version management system allows the user to follow the progress of a project TikZ by providing the following features :
    - connect the connection of a project.
    - commit committing changes made to a certain branch with a message outlining the changes
    - revert undoes one or more previous releases, to restore a version previous.
    - merge two branches, changes in one are carried over to the other.
    - diff presents the difference (in terms of TikZ code) between the current version and a Previous commit specified.
- Commits will be bound to the user who made the commit.
- The user will have access to these features through a designated menu in the application.
 
**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 10 Support for specific TikZ libraries

**Original instructions:**

- In addition to the traditional functionality of drawing a diagram (i.e. placement of nodes and arcs between nodes), system users can access dedicated diagram construction modes, which will use specific TikZ libraries. Specifically :
    - *mindmap:* Library for drawing an idea map. The idea map will be structured as a tree where the root is the main concept of the map and all other concepts will be encoded as child nodes in the tree.
    - *trees:* Bookstore for drawing a tree. The tree is described as a set of nodes and the relationships between them.
    - *matrix:* Library for arranging nodes on a grid (i.e. placing nodes in rows and columns, like elements of a matrix). 
 
**Tasks:** 


**Extra Tasks:**

:question: **Question:**

### 11 TikZ syntax highlighting

**Original instructions:**

- Users can take advantage of a syntax highlighting feature for LaTeX code associated with the diagram they are working on. 
- This feature should color the keywords of the TikZ language in a different way than the rest of the code, in order to simplify the management of the code for the user.
- In addition, when the user selects a graphical element in the dedicated section of the the GUI, the code corresponding to this element must be highlighted in the section displaying the LaTeX/TikZ code.

**Tasks:** 


**Extra Tasks:**

:question: **Question:** 

### 12 Help Section

**Original instructions:**

- Users can access a help section in the program. With this tool, users can obtain more detailed information and explanations on how to use the various features offered by the program. Eventually, a tutorial demonstrating the use of certain features could be started from this section.

**Tasks:** 


**Extra Tasks:**

:question: **Question:**

### 13 Data security

**Original instructions:**

- The software has to meet specific requirements regarding computer security. 
- First of all, the saved data must not be accessible to third parties (i.e. the data must be saved confidentially).
- The application offers the possibility to password-protect a file or a project export. The application will only be able to open or import the protected file or project if the correct password is entered.

**Tasks:** 


**Extra Tasks:**

:question: **Question:**  

### 14 Data integrity

**Original instructions:**

- The software must guarantee that the data stored within the application cannot be altered in a fortuitous, illicit or malicious manner. The software must therefore guarantee the absence of unauthorized modifications.

**Tasks:** 


**Extra Tasks:**

:question: **Question:**  

### 15 Copy-paste

**Original instructions:**

- The user can select a section of the diagram, copy it, and then paste it at the current cursor position. 
- The copy action and the paste action will have respective keyboard shortcuts.

**Tasks:** 


**Extra Tasks:**

:question: **Question:**  