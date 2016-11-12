--
-- This SQL script builds a monopoly database, deleting any pre-existing version.
--
-- @author kvlinden and Mark Davis mjd85
-- lab8, CS 262 Calvin College
-- @version Summer, 2015
--

-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS PlayerGame;
DROP TABLE IF EXISTS Game;
DROP TABLE IF EXISTS Properties;
DROP TABLE IF EXISTS Player;


-- Create the schema.
CREATE TABLE Game (
	ID integer PRIMARY KEY, 
	time timestamp
	);

CREATE TABLE Player (
	ID integer PRIMARY KEY, 
	emailAddress varchar(50) NOT NULL,
	name varchar(50),
	cash integer,
	pieceLocation integer
	);

CREATE TABLE PlayerGame (
	gameID integer REFERENCES Game(ID),
	playerID integer REFERENCES Player(ID),
	score integer
	);

CREATE TABLE Properties (
	playerID integer REFERENCES Player(ID),
	propertyName varchar(50),
	houses integer,
	hotel boolean
	);

-- Allow users to select data from the tables.
GRANT SELECT ON Game TO PUBLIC;
GRANT SELECT ON Player TO PUBLIC;
GRANT SELECT ON PlayerGame TO PUBLIC;
GRANT SELECT ON Properties TO PUBLIC;

-- Add sample records.
INSERT INTO Game VALUES (1, '2006-06-27 08:00:00');
INSERT INTO Game VALUES (2, '2006-06-28 13:20:00');
INSERT INTO Game VALUES (3, '2006-06-29 18:41:00');

INSERT INTO Player(ID, emailAddress, cash, pieceLocation) VALUES (1, 'me@calvin.edu', 300, 8);
INSERT INTO Player VALUES (2, 'king@gmail.edu', 'The King', 100, 15);
INSERT INTO Player VALUES (3, 'dog@gmail.edu', 'Dogbreath', 200, 17);

INSERT INTO PlayerGame VALUES (1, 1, 0.00);
INSERT INTO PlayerGame VALUES (1, 2, 0.00);
INSERT INTO PlayerGame VALUES (1, 3, 2350.00);
INSERT INTO PlayerGame VALUES (2, 1, 1000.00);
INSERT INTO PlayerGame VALUES (2, 2, 0.00);
INSERT INTO PlayerGame VALUES (2, 3, 500.00);
INSERT INTO PlayerGame VALUES (3, 2, 0.00);
INSERT INTO PlayerGame VALUES (3, 3, 5500.00);

INSERT INTO Properties VALUES (1, 'Boardwalk', 2, false);
INSERT INTO Properties VALUES (1, 'Park Place', 2, false);
INSERT INTO Properties VALUES (2, 'Baltic Avenue', 0, false);
INSERT INTO Properties VALUES (3, 'New York', 0, false);

------EXERCISE 8.1-------

--A)
--SELECT *
--FROM Game
--ORDER BY time DESC;

--B)
--SELECT *
--FROM Game
--WHERE time > '2016-10-21';

--C)
--SELECT playerID
--FROM PlayerGame
--WHERE gameID IS NOT NULL;

--D)
--SELECT *
--FROM PlayerGame
--WHERE score > 200;

--E)
--SELECT *
--FROM Player
--WHERE emailAddress LIKE '%gmail%';

------EXERCISE 8.2------

--A)
--SELECT score
--FROM Player, PlayerGame
--WHERE Player.ID = PlayerGame.playerID
--   AND Player.name = 'The King' ORDER BY score DESC;

--B)
--SELECT Player.name, score
--FROM Player, PlayerGame, Game
--WHERE Player.ID = PlayerGame.playerID
--   AND PlayerGame.gameID = Game.ID
--   AND Game.time = '2006-06-28 13:20:00'
--   ORDER BY score DESC LIMIT 1;

--C)
-- The P1 < P2 is making sure the IDs are not the same

--D)
--Comparing two elements of the same table.