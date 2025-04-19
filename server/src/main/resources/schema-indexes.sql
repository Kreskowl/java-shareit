CREATE INDEX IF NOT EXISTS idx_bookings_requester_id ON bookings (requester_id);
CREATE INDEX IF NOT EXISTS idx_bookings_item_id ON bookings (item_id);
CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings (status);
CREATE INDEX IF NOT EXISTS idx_bookings_start_time ON bookings (start_time);
CREATE INDEX IF NOT EXISTS idx_bookings_item_status ON bookings (item_id, status);

CREATE INDEX IF NOT EXISTS idx_items_owner_id ON items (owner_id);
CREATE INDEX IF NOT EXISTS idx_items_request_id ON items (request_id);

CREATE INDEX IF NOT EXISTS idx_comments_item_id ON comments (item_id);
CREATE INDEX IF NOT EXISTS idx_comments_author_id ON comments (author_id);

CREATE INDEX IF NOT EXISTS idx_requests_user_id ON requests (user_id);