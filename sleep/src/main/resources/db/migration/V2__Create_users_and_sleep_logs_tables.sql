-- Create the 'users' table with username field
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL  -- Added username field with uniqueness constraint
);

-- Create the 'sleep_logs' table with username reference
CREATE TABLE sleep_logs (
                            id UUID PRIMARY KEY,
                            username VARCHAR(255) NOT NULL,
                            date DATE NOT NULL,
                            time_in_bed_start TIMESTAMP NOT NULL,
                            time_in_bed_end TIMESTAMP NOT NULL,
                            total_time_in_bed INT NOT NULL,
                            feeling VARCHAR(10) NOT NULL,
                            CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

