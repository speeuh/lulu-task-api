-- Add recurrence column to tasks table
ALTER TABLE tasks ADD COLUMN IF NOT EXISTS recurrence VARCHAR(20) DEFAULT 'NONE';

-- Add next_occurrence_date column for recurring tasks
ALTER TABLE tasks ADD COLUMN IF NOT EXISTS next_occurrence_date TIMESTAMP;

-- Update existing tasks to have NONE recurrence
UPDATE tasks SET recurrence = 'NONE' WHERE recurrence IS NULL;

-- Make recurrence NOT NULL
ALTER TABLE tasks ALTER COLUMN recurrence SET NOT NULL;

