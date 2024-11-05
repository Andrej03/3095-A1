CREATE TABLE t_rooms (
    id BIGSERIAL NOT NULL ,
    room_name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    features TEXT,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);
