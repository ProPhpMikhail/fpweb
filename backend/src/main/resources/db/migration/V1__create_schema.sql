CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    code        VARCHAR(255),
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL
);

CREATE TABLE accounts (
  id       BIGSERIAL PRIMARY KEY,
  user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  name     VARCHAR(100) NOT NULL,
  sort     INT DEFAULT 1,
  currency VARCHAR(3)   NOT NULL DEFAULT 'RUB',
  balance  NUMERIC(14,2) NOT NULL DEFAULT 0,
  version  BIGINT        NOT NULL DEFAULT 0
);

CREATE TABLE categories (
  id          BIGSERIAL PRIMARY KEY,
  user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  sort        INT DEFAULT 1,
  name        VARCHAR(255) NOT NULL
);

CREATE TABLE transactions (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  account_id   BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
  category_id  BIGINT REFERENCES categories(id),
  sort         INT DEFAULT 1,
  name         VARCHAR(255) NOT NULL,
  amount       NUMERIC(14,2) NOT NULL,
  created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
  latitude     NUMERIC(9, 6),
  longitude    NUMERIC(9, 6)
);

CREATE TABLE transfers (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  from_account_id  BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
  from_amount      NUMERIC(14,2) NOT NULL,
  to_account_id    BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
  to_amount        NUMERIC(14,2) NOT NULL,
  previous_id      INT DEFAULT 1,
  created_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE TABLE plans (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  name         VARCHAR(255) NOT NULL,
  sort         INT DEFAULT 1,
  account_id   BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
  amount       NUMERIC(14,2) NOT NULL,
  date_from    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  date_to      TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE plan_categories (
  id           BIGSERIAL PRIMARY KEY,
  sort         INT DEFAULT 1,
  plan_id      BIGINT NOT NULL REFERENCES plans(id) ON DELETE CASCADE,
  amount       NUMERIC(14,2) NOT NULL
);
