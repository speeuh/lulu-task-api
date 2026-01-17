-- Add button_color column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS button_color VARCHAR(50) DEFAULT '#81D4FA';

-- Update existing users to have the default button color
UPDATE users SET button_color = '#81D4FA' WHERE button_color IS NULL;

