--
-- ISERT BIDS
--
INSERT INTO bid VALUES(1, 80, 1, 1, 3, 'Hello', current_timestamp, current_timestamp, true);
INSERT INTO bid VALUES(2, 7.5, 2, 2, 6, 'My name is Stephan, i m your friend', current_timestamp, current_timestamp, true);
INSERT INTO bid VALUES(3, 999999, 3, 3, 9, 'Nobody can beat my bid', current_timestamp, current_timestamp, true);
INSERT INTO bid VALUES(4, 30, 4, 4, 11, 'Nice product', current_timestamp, current_timestamp, true);
INSERT INTO bid VALUES(5, 100, 1, 5, 3, 'I bit expansive but it is fine', current_timestamp, current_timestamp, true);
INSERT INTO bid VALUES(6, 50, 1, 1, 1, 'I won this bid', current_timestamp, current_timestamp, false);
SELECT setval('bid_id_seq', max(id)) FROM bid;