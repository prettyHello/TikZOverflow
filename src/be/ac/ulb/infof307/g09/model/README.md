# Model package :

Here you will find packages for everything concerning the model part of the application.

## Classes :
### DalBackEndServices :
<p>
This interface is part of the DAL (Data Access Layer). It is only exposed to the DAOs (Data
Access Objects) and is used to fetch prepared statements in order to execute queries. It
shouldn't be used by UCCs (Use-case controllers).
</p>

### DalServices :
<p>
This interface is part of the DAL (Data Access Layer). It is exposed to the UCCs (Use-case
Controllers) to manage the connections to the database.
</p>

### DAO :
<p>
This interface exposes the basic CRUD operations performed on the database that are shared by all
the DAOs (Data Access Objects). It is intended to by extended by more specific DAOs if those
operations are not sufficient.
</p>

### ProjectDAO :
<p>
Interface for database action regarding projects.
</p>

### UserDAO :
<p>
Interface for database action regarding users.
</p>
