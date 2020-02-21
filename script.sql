CREATE TABLE IF NOT EXISTS users (
                                     user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     first_name TEXT NOT NULL,
                                     last_name TEXT NOT NULL,
                                     email TEXT NOT NULL UNIQUE,
                                     phone TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL,
                                     salt TEXT NOT NULL UNIQUE,
                                     register_date TEXT NOT NULL

);


CREATE TABLE IF NOT EXISTS  projects (
                                         project_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         name TEXT NOT NULL UNIQUE,
                                         creation_date TEXT NOT NULL,
                                         modification_date TEXT NOT NULL

);


CREATE TABLE IF NOT EXISTS  user_projects(
                                             user_id INTEGER,
                                             project_id INTEGER,
                                             PRIMARY KEY (user_id, project_id),
                                             FOREIGN KEY (user_id)
                                                 REFERENCES users (user_id)
                                                 ON DELETE CASCADE
                                                 ON UPDATE NO ACTION,
                                             FOREIGN KEY (project_id)
                                                 REFERENCES projects (project_id)
                                                 ON DELETE CASCADE
                                                 ON UPDATE NO ACTION
);