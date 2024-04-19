ALTER TABLE users.info
    ALTER COLUMN id SET DATA TYPE UUID USING (gen_random_uuid());