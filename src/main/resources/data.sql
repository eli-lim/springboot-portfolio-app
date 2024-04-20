-- Seed the database with some initial data
INSERT INTO security (security_type, id, symbol, expected_return, standard_deviation) VALUES
    ('STOCK', 1, 'AAPL', 0.1, 0.5),
    ('STOCK', 2, 'TSLA', 0.2, 0.3),
    ('STOCK', 3, 'NFLX', 0.4, 0.7);

INSERT INTO security (security_type, option_type, id, symbol, expiration, strike, underlying_id) VALUES
    ('OPTION', 'PUT', 4, 'AAPL-OCT-2024-110-P', '2024-10-18 11:59:59.999Z', '110', 1),
    ('OPTION', 'CALL', 5, 'AAPL-OCT-2024-110-C', '2024-10-18 11:59:59.999Z', '110', 1),
    ('OPTION', 'PUT', 6, 'AAPL-NOV-2024-110-P', '2024-11-15 11:59:59.999Z', '110', 1),
    ('OPTION', 'CALL', 7, 'AAPL-NOV-2024-110-C', '2024-11-15 11:59:59.999Z', '110', 1),
    ('OPTION', 'PUT', 8, 'TSLA-NOV-2024-400-P', '2024-11-18 11:59:59.999Z', '400', 2),
    ('OPTION', 'CALL', 9, 'TSLA-NOV-2024-400-C', '2024-11-18 11:59:59.999Z', '400', 2),
    ('OPTION', 'PUT', 10, 'TSLA-DEC-2024-400-P', '2024-12-20 11:59:59.999Z', '400', 2),
    ('OPTION', 'CALL', 11, 'TSLA-DEC-2024-400-C', '2024-12-20 11:59:59.999Z', '400', 2);

