# FeedBuzz : Projet de génie logiciel et gestion de projet (INFO-F-307)

TO DO: Description du projet

# Utilisation

TO DO: Informations sur le système de build et la version du Java/Librairies utilisés dans l'implementation.

## Compilation

TO DO: Informations sur la façon de compiler votre projet 

## Démarrage 

TO DO: Informations sur la façon d'éxecuter votre projet

<p>
1) On the menu above, click on "file" and then select "projectstructure":
<ul>
	<li>In Modules-> groupe09-> select the "sources" tab and mark the folder "src" as Sources.</li>
	<li>In Project-> project set JDK (choose the jdk of your choice)</li>
	<li>In Project-> fill the field "project compiler output" with the path to the folder where you want the output to be generated. We recommand creating a folder name "out" in the root folder for that </li>
	<li>In Libraries -> press "+" -> then add the folder "src/lib" as a java library. Once done it is necessary to click on "change version" and to confirm in order to load most libraries.</li>
	<li>In Libraries -> press "+" -> in lib, select "sqlite-jdbc-3.30.1.jar" and add it as java.</li>
	<li>In Libraries -> press "+" -> in lib, select "spring-secuity-crypto/5.2.1.RELEASE/spring-security-crypto-5.2.1.RELEASE.jar" and add it as java.</li>
</ul></p>

<p>
2) On the menu above, click on "Add Configuration" -> "+" -> "Application". On the form, fill the field "Main class" with the path to the main file from /src (i.e. the value is "Main"). 
</p>

<p>
3) Since JavaFX is not included in SDK since the version 10, it is necessary to download the appropriate version for the host OS on the JavaFX website.
The javaFX SDK can be downloaded from here https://gluonhq.com/products/javafx/, and unziped anywhere on the desktop.
</p>
<p>
4) On the menu above, click on "file", select "projectstructure"  and then the "Libraries" tab. There you will be able to press "+" to add the JavaFX as a java library (just give the path to the lib folder of the SDK).
</p>
<p>
5) Finally (for SDK > 11), still on the menu above, select "run" -> "edit configuartion" and add the following line in "VM option" (put your own path):<br>
	&nbsp;&nbsp;&nbsp;&nbsp; --module-path %PATH_TO_FX%/lib --add-modules=javafx.controls,javafx.fxml
</p>
<p>
Fin) You will now be able to build the project by clicking on the green hammer in the menu above and then to run the software by clicking on the green arrow.
</p>

# Configuration :

## Serveur 

TO DO: Informations sur la configuration du serveur

## Client

TO DO: Informations sur la configuration du client

# Tests

TO DO: Informations sur la façon d'executer les tests

# Misc

## Développement

## Screenshot

## License
