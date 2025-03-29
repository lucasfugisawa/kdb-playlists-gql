-- Create tables for the application

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE
);

-- Songs table
CREATE TABLE songs (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    duration INTEGER NOT NULL,
    genre VARCHAR(50) NOT NULL
);

-- Playlists table
CREATE TABLE playlists (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creator_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    tags TEXT[]
);

-- PlaylistSongs table
CREATE TABLE playlist_songs (
    id UUID PRIMARY KEY,
    playlist_id UUID NOT NULL REFERENCES playlists(id),
    song_id UUID NOT NULL REFERENCES songs(id),
    added_by_id UUID NOT NULL REFERENCES users(id),
    position INTEGER NOT NULL,
    UNIQUE(playlist_id, position)
);

-- Votes table
CREATE TABLE votes (
    id UUID PRIMARY KEY,
    playlist_song_id UUID NOT NULL REFERENCES playlist_songs(id),
    user_id UUID NOT NULL REFERENCES users(id),
    vote_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE(playlist_song_id, user_id)
);