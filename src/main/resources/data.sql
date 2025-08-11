SET time_zone = 'Asia/Seoul';

INSERT INTO member (email, password, nickname, auth, createdAt, updatedAt) VALUES ('hanaro', '$2a$10$gdvGsh/6zhBXMJ.QHBotLek/pwP9hY8OqhGgJh.3hHIgGHrvUsbeq', 'hanaro', 'ADMIN', NOW(), NOW());

INSERT INTO `item` (`name`, `price`, `stock`) VALUES
                                                  ('테스트상품 01',  9900,  10),
                                                  ('테스트상품 02', 14900, 15),
                                                  ('테스트상품 03', 19900,  8),
                                                  ('테스트상품 04', 25900, 20),
                                                  ('테스트상품 05',  8900,  5),
                                                  ('테스트상품 06', 32900, 12),
                                                  ('테스트상품 07', 45900,  7),
                                                  ('테스트상품 08', 11900, 30),
                                                  ('테스트상품 09',  5900, 50),
                                                  ('테스트상품 10', 18900,  9);

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/2a8f0e3f4e6a4f23a3b1b1a1a12c01a1.jpg', i.id
FROM `item` i WHERE i.`name` = '테스트상품 01';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/5f7c1d2e3a4b4c55d6e7f8a9b0c1d2e3.png', i.id
FROM `item` i WHERE i.`name` = '테스트상품 02';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/7aa91234bcde45f0aa01bb22cc33dd44.jpg', i.id
FROM `item` i WHERE i.`name` = '테스트상품 03';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/81c2a3d4e5f607189a0b1c2d3e4f5a6b.webp', i.id
FROM `item` i WHERE i.`name` = '테스트상품 04';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/90ab12cd34ef56ab78cd90ef12ab34cd.jpg', i.id
FROM `item` i WHERE i.`name` = '테스트상품 05';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/a1b2c3d4e5f60718293a4b5c6d7e8f90.png', i.id
FROM `item` i WHERE i.`name` = '테스트상품 06';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/bb11cc22dd33ee44ff5566778899aabb.jpg', i.id
FROM `item` i WHERE i.`name` = '테스트상품 07';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/c0ffee00deadbeefcafebabefeedface.png', i.id
FROM `item` i WHERE i.`name` = '테스트상품 08';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/d1e2f3a4b5c6d7e8f90123456789abcd.jpg', i.id
FROM `item` i WHERE i.`name` = '테스트상품 09';

INSERT INTO `itemimage` (`imgUrl`, `item`)
SELECT '/upload/2025/08/11/ee11ff22aa33bb44cc55dd66ee77ff88.webp', i.id
FROM `item` i WHERE i.`name` = '테스트상품 10';
