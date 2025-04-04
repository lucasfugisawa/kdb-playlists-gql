-- Add roles column to users table
ALTER TABLE users ADD COLUMN roles VARCHAR(255) NOT NULL DEFAULT 'USER';