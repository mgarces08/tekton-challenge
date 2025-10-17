CREATE TABLE failed_async_task (
    id BIGSERIAL PRIMARY KEY,
    process VARCHAR(50) NOT NULL,
    error TEXT NOT NULL,
    data TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    retries INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);
