CREATE TABLE tb_user_external_project (
    id      VARCHAR(200) NOT NULL COMMENT 'identifier of external project',
    user_id VARCHAR(36)  NOT NULL COMMENT 'unique identifier of the user',
    name    VARCHAR(120) NOT NULL COMMENT 'name of external project',
    PRIMARY KEY (id, user_id),
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES tb_user (id) ON DELETE CASCADE
) COMMENT 'External Project identifier for users';