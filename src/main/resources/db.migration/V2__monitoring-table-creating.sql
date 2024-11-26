CREATE TABLE monitorings (
                             id BIGSERIAL PRIMARY KEY,
                             ticker VARCHAR(100) NOT NULL,
                             target_price DECIMAL(18, 2) NOT NULL,
                             user_id BIGINT NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             created_by VARCHAR(255),
                             updated_by VARCHAR(255),
                             CONSTRAINT fk_monitorings_users FOREIGN KEY (user_id) REFERENCES users (id)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE
);