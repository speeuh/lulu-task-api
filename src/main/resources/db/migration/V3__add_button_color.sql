-- Add button_color column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS button_color VARCHAR(50) DEFAULT '#FF9B8A';

-- Update existing users to have the default button color
UPDATE users SET button_color = '#FF9B8A' WHERE button_color IS NULL;

