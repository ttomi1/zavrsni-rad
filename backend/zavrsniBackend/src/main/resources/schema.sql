CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(100) UNIQUE,
                       password VARCHAR(255),
                       email VARCHAR(100) UNIQUE NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       provider VARCHAR(50),
                       image_url TEXT
);

CREATE TABLE user_roles (
                            user_id BIGINT,
                            role VARCHAR(50),
                            FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE refresh_token (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               token VARCHAR(255) UNIQUE NOT NULL,
                               expiry_date TIMESTAMP NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE post (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      text TEXT,
                      image_url VARCHAR(255),
                      created_at TIMESTAMP,
                      author_id BIGINT,
                      CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         text VARCHAR(1000) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         author_id BIGINT NOT NULL,
                         post_id BIGINT NOT NULL,
                         CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
                         CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);

CREATE TABLE post_likes (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            post_id BIGINT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_post_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
                            UNIQUE (user_id, post_id)
);

CREATE TABLE follows (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         follower_id BIGINT NOT NULL,
                         following_id BIGINT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT uq_follower_following UNIQUE (follower_id, following_id),
                         FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
);


