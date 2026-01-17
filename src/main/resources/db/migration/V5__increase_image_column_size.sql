-- Increase image column sizes to support base64 storage
ALTER TABLE users ALTER COLUMN profile_image_url TYPE TEXT;
ALTER TABLE shop_items ALTER COLUMN image_url TYPE TEXT;

