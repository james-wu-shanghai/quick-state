-- 状态机表结构创建脚本
CREATE TABLE IF NOT EXISTS state_machine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    config LONGVARCHAR,
    current_state VARCHAR(100),
    create_time TIMESTAMP NOT NULL,
    update_time TIMESTAMP NOT NULL
);