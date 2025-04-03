# kdb-playlists-gql

A complete Kotlin + Ktor + GraphQL project using graphql-kotlin that serves as a clean, educational codebase for learning and teaching GraphQL servers with Kotlin. This project demonstrates how to build a music playlist management system with GraphQL APIs.

## Features

- **Kotlin & Ktor**: Modern, concise, and safe programming with Kotlin and Ktor web framework
- **GraphQL API**: Complete GraphQL implementation using graphql-kotlin
- **PostgreSQL Database**: Persistent storage with PostgreSQL
- **Exposed ORM**: Type-safe SQL framework for Kotlin
- **Flyway Migrations**: Database versioning and migrations
- **HikariCP**: High-performance JDBC connection pool
- **Domain-Driven Design**: Clean architecture with separation of concerns
- **Docker Support**: Easy setup with Docker Compose

## Domain Overview & Core Entities

This project implements a minimalist music playlist management system with the following core entities:

### User
- Simple user model with ID and username
- Users can create playlists and vote on songs

### Playlist
- Collection of songs created by a user
- Has title, description, and optional tags
- Tracks creation time

### Song
- Music track with title, artist, duration, and genre
- Supports various genres: Rock, Pop, Jazz, Metal, Indie, Hip-Hop, Electronic

### PlaylistSong
- Join entity representing a song within a playlist
- Tracks who added the song and its position in the playlist

### Vote
- Users can upvote or downvote songs in playlists
- Tracks who voted and when

## Database Setup

This project uses PostgreSQL as its database. A Docker Compose file is provided to easily set up the database.

### Starting the Database

To start the PostgreSQL database, run:

```bash
docker-compose up -d
```

This will start a PostgreSQL container with the following configuration:
- **Database name**: kdb_playlists
- **Username**: postgres
- **Password**: postgres
- **Port**: 5432 (mapped to host)

### Stopping the Database

To stop the PostgreSQL container, run:

```bash
docker-compose down
```

To stop the container and remove all data (volumes), run:

```bash
docker-compose down -v
```

## Testing, Building & Running

To test, build or run the project, use one of the following tasks:

| Task                                    | Description                                                          |
|-----------------------------------------|----------------------------------------------------------------------|
| `./gradlew test`                        | Run the tests                                                        |
| `./gradlew build`                       | Build everything                                                     |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `./gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `./gradlew run`                         | Run the server                                                       |
| `./gradlew runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see something like the following output:

```
[main] INFO  Application - Application started in 0.303 seconds.
[main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Useful Links

- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Ktor Documentation](https://ktor.io/docs/)
- [GraphQL Kotlin Documentation](https://expediagroup.github.io/graphql-kotlin/)
- [Exposed Documentation](https://github.com/JetBrains/Exposed)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Koin Documentation](https://insert-koin.io/docs/reference/introduction/)
- [PostgreSQL documentation](https://www.postgresql.org/docs/)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Contributing

Contributions are welcome! Here's how you can contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/my-new-feature`
5. Submit a pull request

Please make sure your code follows the project's coding design and style, and includes appropriate tests.
