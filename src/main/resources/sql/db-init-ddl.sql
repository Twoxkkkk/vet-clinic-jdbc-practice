
CREATE TABLE IF NOT EXISTS pet_owners (

    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(32) NOT NULL CHECK (trim(first_name) <> ''),
    last_name VARCHAR(32) NOT NULL CHECK (trim(last_name) <> ''),
    contact_number VARCHAR(16) NOT NULL CHECK (trim(contact_number) <> ''),
    email VARCHAR(256) UNIQUE CHECK (trim(email) <> ''),
    address VARCHAR(128),
    registration_date DATE DEFAULT CURRENT_DATE

);

CREATE TABLE IF NOT EXISTS vet_specializations (

    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type VARCHAR(16) NOT NULL CHECK (trim(type) <> '')

);

CREATE TABLE IF NOT EXISTS vets (

    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(32) NOT NULL CHECK (trim(first_name) <> ''),
    last_name VARCHAR(32) NOT NULL CHECK (trim(last_name) <> ''),
    contact_number VARCHAR(16) CHECK (trim(contact_number) <> ''),
    email VARCHAR(256) UNIQUE NOT NULL CHECK (trim(email) <> ''),
    specialization_id INT NOT NULL,

    CONSTRAINT fk_specialization_id FOREIGN KEY (specialization_id) REFERENCES vet_specializations(id)

);

CREATE TABLE IF NOT EXISTS breeds (

    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pet_type VARCHAR(16) NOT NULL CHECK (trim(pet_type) <> ''),
    name VARCHAR(16) NOT NULL CHECK (trim(name) <> '')

);

CREATE TABLE IF NOT EXISTS pets (

    id UUID PRIMARY KEY NOT NULL,
    owner_id UUID NOT NULL,
    breed_id INT NOT NULL,
    nickname VARCHAR(16) NOT NULL CHECK (trim(nickname) <> ''),
    date_of_birth DATE NOT NULL,
    sex VARCHAR(8) DEFAULT 'UNKNOWN',
    weight NUMERIC NOT NULL,

    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES pet_owners(id) ON DELETE CASCADE,
    CONSTRAINT fk_breed_id FOREIGN KEY (breed_id) REFERENCES breeds(id)

);

CREATE TABLE IF NOT EXISTS procedures (

    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(32) NOT NULL CHECK (trim(name) <> ''),
    price NUMERIC NOT NULL,
    duration_minutes INT DEFAULT 30
);

CREATE TABLE IF NOT EXISTS appointments (

    id UUID PRIMARY KEY NOT NULL,
    vet_id UUID NOT NULL,
    pet_id UUID NOT NULL,
    procedure_id INT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'PLANNED',

    CONSTRAINT fk_vet_id FOREIGN KEY (vet_id) REFERENCES vets(id) ON DELETE CASCADE,
    CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    CONSTRAINT fk_procedure_id FOREIGN KEY (procedure_id) REFERENCES procedures(id)

);

CREATE TABLE IF NOT EXISTS medical_records (

    id UUID PRIMARY KEY NOT NULL,
    vet_id UUID NOT NULL,
    pet_id UUID NOT NULL,
    diagnosis VARCHAR(64) NOT NULL,
    treatment TEXT NOT NULL,
    record_date DATE,

    CONSTRAINT fk_vet_id FOREIGN KEY (vet_id) REFERENCES vets(id),
    CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE

)
