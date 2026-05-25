CREATE TABLE tb_user (
    id         VARCHAR(36)  NOT NULL COMMENT 'unique identifier of the user',
    email      VARCHAR(200) NOT NULL COMMENT 'email for user',
    password   VARCHAR(129) NOT NULL COMMENT 'password',
    name       VARCHAR(120)     NULL COMMENT 'name of the user',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_email (email)
) COMMENT 'All users';