INSERT INTO systemEntity (public, owner, version) VALUES (TRUE, 762401, 0);
INSERT INTO theme (id, name, themePage) VALUES (LAST_INSERT_ID(), 'Consumers Union', 'cu.jsp');
INSERT INTO organization_theme (organization, theme) VALUES (2, LAST_INSERT_ID());
