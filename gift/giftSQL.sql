DROP DATABASE IF EXISTS gift_spring;
CREATE SCHEMA gift_spring DEFAULT CHARACTER SET utf8;
SET NAMES UTF8;
USE gift_spring;
CREATE TABLE gift_certificate
(
    id               INT         NOT NULL AUTO_INCREMENT,
    name             varchar(20) NOT NULL,
    description      VARCHAR(30) NOT NULL,
    price            INT DEFAULT 0,
    duration         INT         NOT NULL,
    create_date      DATETIME    NOT NULL,
    last_update_date DATETIME    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tag
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       INT         NOT NULL AUTO_INCREMENT,
    name     VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    account  INT         NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE user_role
(
    user_id INT NOT NULL,
    role    enum ('ADMIN', 'USER') null,
    FOREIGN KEY (user_id) references user (id)
);
CREATE TABLE purchase
(
    id             INT      NOT NULL AUTO_INCREMENT,
    price          INT      NOT NULL,
    purchase_date  DATETIME NOT NULL,
    user_id        INT      NOT NULL,
    certificate_id INT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id),
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificate_tag
(
    certificate_id INT NOT NULL,
    tag_id         INT NOT NULL,
    FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id),
    UNIQUE (certificate_id, tag_id)
);
DELIMITER //

CREATE PROCEDURE GenerateGiftCertificateRecords()
BEGIN
    DECLARE counter INT DEFAULT 1;

    WHILE counter <= 10000
        DO

            SET @name = CONCAT('Certificate', counter);
            SET @description = CONCAT('Description', counter);
            SET @price = FLOOR(RAND() * 100) + 1;
            SET @duration = FLOOR(RAND() * 10) + 1;
            SET @create_date = NOW();
            SET @last_update_date = NOW();


            INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
            VALUES (@name, @description, @price, @duration, @create_date, @last_update_date);

            SET counter = counter + 1;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

CREATE PROCEDURE GenerateTagRecords()
BEGIN
    DECLARE counter INT DEFAULT 1;

    WHILE counter <= 1000
        DO

            SET @tagName = CONCAT('Tag', counter);


            INSERT INTO tag (name) VALUES (@tagName);

            SET counter = counter + 1;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

CREATE PROCEDURE GenerateUser()
BEGIN
    DECLARE counter INT DEFAULT 1;

    WHILE counter <= 1000
        DO

            SET @tagName = CONCAT('User', counter);
            SET @account = FLOOR(RAND() * 100) + 1;
            SET @password = '1234';


            INSERT INTO user (name, password, account) VALUES (@tagName, @password, @account);

            SET counter = counter + 1;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

CREATE PROCEDURE GeneratePurchase()
BEGIN
    DECLARE counter INT DEFAULT 1;
    DECLARE duplicate_count INT;
    WHILE counter <= 1000
        DO

            SET @user_id = FLOOR(RAND() * 100) + 1;
            SET @certificate_id = FLOOR(RAND() * 100) + 1;
            SET @create_date = NOW();
            SELECT COUNT(*)
            INTO duplicate_count
            FROM purchase
            WHERE user_id = @user_id
              AND certificate_id = @certificate_id;

            IF duplicate_count = 0 THEN
                SET @price = 0;
                SELECT price INTO @price FROM gift_certificate WHERE gift_certificate.id = @certificate_id;
                INSERT INTO purchase (user_id, certificate_id, price, purchase_date)
                VALUES (@user_id, @certificate_id, @price, @create_date);
                SET counter = counter + 1;
            END IF;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

CREATE PROCEDURE GenerateGiftCertificateTagRecords()
BEGIN
    DECLARE counter INT DEFAULT 1;
    DECLARE duplicate_count INT;

    WHILE counter <= 10000
        DO

            SET @certificate_id = FLOOR(RAND() * 100) + 1;
            SET @tag_id = FLOOR(RAND() * 100) + 1;

            SELECT COUNT(*)
            INTO duplicate_count
            FROM gift_certificate_tag
            WHERE certificate_id = @certificate_id
              AND tag_id = @tag_id;

            IF duplicate_count = 0 THEN

                INSERT INTO gift_certificate_tag (certificate_id, tag_id)
                VALUES (@certificate_id, @tag_id);

                SET counter = counter + 1;
            END IF;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

CREATE PROCEDURE GenerateRolesRecords()
BEGIN
    DECLARE counter INT DEFAULT 1;

    WHILE counter <= 1000
        DO

            SET @user_id = counter;
            SET @role = 'USER';

            INSERT INTO user_role (user_id, role) VALUES (@user_id, @role);

            SET counter = counter + 1;
        END WHILE;

    SELECT 'Records generated successfully.';
END //

DELIMITER ;
CALL GenerateGiftCertificateRecords();
CALL GenerateTagRecords();
CALL GenerateGiftCertificateTagRecords();
CALL GenerateUser();
CALL GeneratePurchase();
CALL GenerateRolesRecords();
