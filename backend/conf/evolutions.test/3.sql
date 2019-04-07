

-- Ensure user_id is reset

ALTER TABLE user AUTO_INCREMENT = 1;



-- User mock data

INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Bondie', 'Sandy', 'Antcliff', 753069370, 'Male', 'santcliff0@imdb.com', 'AUXpFDFs', 1529196155, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Ruttger', 'Thorin', 'Purvess', 1021230040, 'Male', 'tpurvess1@networksolutions.com', 'Qhzr1oOIY', 1541403267, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Swen', 'Bengt', 'Reinisch', 1190871962, 'Male', 'breinisch2@ucsd.edu', 'LeivviIYlEf', 1534910690, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Cher', 'Alta', 'Linnard', 1254397407, 'Female', 'alinnard3@toplist.cz', 'eC5BJ6jLtSOG', 1542190323, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Dom', 'Caddric', 'Syer', 608951389, 'Male', 'csyer4@webs.com', 'qtncrCAE', 1522885073, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Bertina', 'Kerri', 'Mercy', 5058911, 'Female', 'kmercy5@google.pl', 'aZgksEHy6Nuk', 1548103813, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Paulette', 'Vernice', 'Rhys', 52709545, 'Female', 'vrhys6@mozilla.com', 'BTIaZEdQi4jp', 1537025133, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Ewart', 'Hubey', 'Gerhardt', 738889126, 'Male', 'hgerhardt7@independent.co.uk', 'pTaQhIdzl5O2', 1525213126, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Nanci', 'Ina', 'Ayllett', 1051415270, 'Female', 'iayllett8@lycos.com', 'BixB16OE', 1538304526, 0);
INSERT INTO user (first_name, middle_name, last_name, date_of_birth, gender, email, password, timestamp, account_type) VALUES ('Guthrey', 'Floyd', 'Baccas', 762675634, 'Male', 'fbaccas9@zdnet.com', 'hCPQoIK', 1535063738, 0);



-- User traveller type mock data

insert into user_traveller_type (user_id, traveller_type_id) values (1, 1);
insert into user_traveller_type (user_id, traveller_type_id) values (2, 2);
insert into user_traveller_type (user_id, traveller_type_id) values (3, 2);
insert into user_traveller_type (user_id, traveller_type_id) values (4, 1);
insert into user_traveller_type (user_id, traveller_type_id) values (5, 4);
insert into user_traveller_type (user_id, traveller_type_id) values (6, 1);
insert into user_traveller_type (user_id, traveller_type_id) values (7, 3);
insert into user_traveller_type (user_id, traveller_type_id) values (8, 4);
insert into user_traveller_type (user_id, traveller_type_id) values (9, 1);
insert into user_traveller_type (user_id, traveller_type_id) values (10, 4);



-- User nationalities mock data

INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (1, 39, true);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (2, 136, true);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (3, 131, false);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (4, 62, false);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (5, 125, false);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (6, 68, true);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (7, 222, false);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (8, 22, true);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (9, 206, false);
INSERT INTO user_nationality (user_id, nationality_id, has_passport) VALUES (10, 203, true);



-- Trip mockup data

insert into trip (user_id, name) values (8, 'Sandpiper, spotted wood');
insert into trip (user_id, name) values (4, 'Cape fox');
insert into trip (user_id, name) values (10, 'Leopard');
insert into trip (user_id, name) values (2, 'Striped dolphin');
insert into trip (user_id, name) values (2, 'Sloth, pale-throated three-toed');
insert into trip (user_id, name) values (4, 'Pheasant, common');
insert into trip (user_id, name) values (6, 'Sparrow, rufous-collared');
insert into trip (user_id, name) values (2, 'Screamer, crested');
insert into trip (user_id, name) values (1, 'Beaver, european');
insert into trip (user_id, name) values (10, 'Red-tailed hawk');



-- Trip destination mock data

insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (1, 36, 1470248814, 1471993983, 1);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (2, 6, 1352368526, 1352947491, 2);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (3, 6, 1376652949, 1377898352, 3);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (4, 43, 1289337209, 1290537510, 4);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (5, 38, 1282698934, 1283470314, 5);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (6, 17, 1368765373, 1369315451, 6);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (7, 46, 1411549619, 1412053109, 7);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (8, 4, 1439738752, 1441041831, 8);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (9, 18, 1452638325, 1453623082, 9);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (10, 48, 1486923593, 1488597337, 10);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (11, 49, 1502343286, 1504549405, 11);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (12, 15, 1376711655, 1377306345, 12);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (13, 8, 1377752674, 1379256534, 13);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (14, 23, 1394161223, 1396178308, 14);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (15, 36, 1345886399, 1346570665, 15);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (16, 16, 1414223668, 1415066723, 16);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (17, 6, 1275057339, 1276455092, 17);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (18, 50, 1517915400, 1519481931, 18);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (19, 8, 1443984314, 1446216346, 19);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (20, 7, 1377035981, 1377902619, 20);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (21, 11, 1475895613, 1476852551, 21);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (22, 44, 1407064340, 1409180958, 22);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (23, 47, 1448607594, 1449297149, 23);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (24, 15, 1342832889, 1343460813, 24);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (25, 0, 1440452142, 1441477740, 25);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (26, 44, 1391835524, 1393589146, 26);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (27, 21, 1317750115, 1318414895, 27);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (28, 10, 1396361667, 1398565805, 28);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (29, 37, 1496478195, 1497261106, 29);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (30, 7, 1429302221, 1430702085, 30);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (31, 46, 1481474166, 1483254582, 31);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (32, 9, 1291693611, 1292721847, 32);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (33, 17, 1395859778, 1396948920, 33);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (34, 27, 1429714643, 1430429171, 34);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (35, 17, 1510433496, 1512641289, 35);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (36, 1, 1372961145, 1373579100, 36);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (37, 25, 1314616840, 1316727072, 37);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (38, 22, 1379818593, 1381694903, 38);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (39, 25, 1454972600, 1456583600, 39);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (40, 38, 1468201480, 1469474646, 40);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (41, 6, 1483045363, 1484339206, 41);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (42, 9, 1285458920, 1287032864, 42);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (43, 44, 1453756095, 1454605811, 43);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (44, 45, 1382502912, 1384063956, 44);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (45, 18, 1475786605, 1477685018, 45);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (46, 45, 1370999689, 1372820133, 46);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (47, 8, 1419992350, 1421148831, 47);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (48, 48, 1406113842, 1406580115, 48);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (49, 11, 1299404841, 1299983002, 49);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (50, 44, 1458310077, 1459626661, 50);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (51, 43, 1487900054, 1488545176, 51);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (52, 28, 1282754155, 1283721728, 52);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (53, 34, 1390159300, 1391859491, 53);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (54, 4, 1433465639, 1435057455, 54);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (55, 50, 1394261357, 1395308954, 55);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (56, 44, 1334529823, 1335187426, 56);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (57, 37, 1419797870, 1420900623, 57);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (58, 20, 1436614908, 1438245690, 58);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (59, 41, 1281808949, 1283215304, 59);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (60, 28, 1417997379, 1418663282, 60);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (61, 40, 1477198543, 1478593608, 61);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (62, 10, 1318481901, 1319155084, 62);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (63, 15, 1493727418, 1495635091, 63);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (64, 19, 1383757820, 1385712424, 64);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (65, 21, 1320467200, 1322270060, 65);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (66, 27, 1296268420, 1297728967, 66);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (67, 30, 1330347085, 1332229163, 67);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (68, 37, 1476113116, 1477123458, 68);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (69, 20, 1507808041, 1509722151, 69);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (70, 47, 1314281463, 1316317606, 70);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (71, 27, 1301562861, 1303183722, 71);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (72, 34, 1393032148, 1393650817, 72);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (73, 46, 1349912926, 1351965575, 73);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (74, 28, 1461925219, 1463637126, 74);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (75, 39, 1355074927, 1355489425, 75);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (76, 13, 1371524125, 1373439419, 76);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (77, 19, 1405992684, 1407010818, 77);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (78, 37, 1399044479, 1400702256, 78);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (79, 45, 1351643305, 1352727711, 79);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (80, 41, 1381793022, 1383345255, 80);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (81, 41, 1304099345, 1304924707, 81);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (82, 48, 1461429516, 1462701227, 82);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (83, 36, 1372687697, 1373669468, 83);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (84, 23, 1410327406, 1411825557, 84);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (85, 14, 1351617990, 1353613948, 85);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (86, 28, 1306837441, 1308950650, 86);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (87, 40, 1500243644, 1501136647, 87);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (88, 23, 1519180267, 1520465933, 88);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (89, 31, 1517604427, 1518608311, 89);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (90, 12, 1448142997, 1449614264, 90);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (91, 45, 1477059141, 1478122251, 91);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (92, 27, 1313872023, 1314897042, 92);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (93, 10, 1352484811, 1352874791, 93);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (94, 9, 1340140704, 1341796676, 94);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (95, 9, 1306591299, 1307785480, 95);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (96, 37, 1383779111, 1384725447, 96);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (97, 16, 1289578519, 1290061470, 97);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (98, 20, 1280105475, 1282280095, 98);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (99, 5, 1455775467, 1457917809, 99);
insert into trip_destination (trip_id, destination_id, arrival_date, departure_date, ordinal) values (100, 8, 1479400053, 1480570205, 100);