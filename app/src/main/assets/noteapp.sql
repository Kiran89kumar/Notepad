DROP TABLE IF EXISTS notebook;
CREATE TABLE notebook (_id integer primary key, heading text not null, description text not null, remove integer);
INSERT INTO notebook (heading, description, remove) VALUES("Note1", "Note 1: Android is the Linux Operative system.", 0);
INSERT INTO notebook (heading, description, remove) VALUES("Note2", "Note 2:", 0);
INSERT INTO notebook (heading, description, remove) VALUES("Note3", "Note 3:", 0);
