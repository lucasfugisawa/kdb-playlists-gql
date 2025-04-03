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

## Architecture & Application Design

This project follows a clean architecture approach with Domain-Driven Design principles, organized into the following layers:

### Domain Layer

The domain layer contains the core business logic and entities, independent of any external frameworks or technologies:

- **Entities**: Core business objects (User, Playlist, Song, PlaylistSong, Vote)
- **Repositories**: Interfaces defining data access methods
- **Value Objects**: Immutable objects representing concepts in the domain

Location: `src/main/kotlin/com/fugisawa/playlistsgql/domain/`

### Application Layer

The application layer coordinates the application's tasks and delegates work to the domain layer:

- **Services**: Implements use cases by orchestrating domain entities and repositories
- **DTOs**: Data Transfer Objects for communication between layers

Location: `src/main/kotlin/com/fugisawa/playlistsgql/services/`

### Infrastructure Layer

The infrastructure layer provides implementations for interfaces defined in the domain layer:

- **Database**: Database configuration, connection management, and migrations
- **Repositories**: Implementations of domain repository interfaces
- **GraphQL**: API implementation using GraphQL
- **Dependency Injection**: Koin configuration for DI

Location: `src/main/kotlin/com/fugisawa/playlistsgql/infrastructure/`

### GraphQL Implementation

The GraphQL implementation is organized into:

- **Types**: GraphQL type definitions and mapping functions
- **Queries**: Query resolvers for fetching data
- **Mutations**: Mutation resolvers for modifying data
- **DataLoaders**: Efficient batch loading to avoid N+1 problems
- **Inputs**: Input types for mutations and queries
- **Scalars**: Custom scalar type definitions

Location: `src/main/kotlin/com/fugisawa/playlistsgql/infrastructure/graphql/`

### Database Implementation

The database implementation uses:

- **Exposed ORM**: Type-safe SQL framework for Kotlin
- **DAO**: Data Access Objects for database operations
- **Mappers**: Functions to map between domain entities and database models
- **Flyway**: Database migrations

Location: `src/main/kotlin/com/fugisawa/playlistsgql/infrastructure/data/`

### Dependency Injection

The project uses Koin for dependency injection:

- **Modules**: Koin modules defining dependencies
- **Component Registration**: Registration of services, repositories, and other components

Location: `src/main/kotlin/com/fugisawa/playlistsgql/infrastructure/di/`

## Performance Optimizations

### DataLoaders and Batch Loading

To avoid the N+1 problem common in GraphQL implementations, this project uses DataLoaders to batch and cache database queries:

- Each entity type has a dedicated DataLoader (UserDataLoader, PlaylistDataLoader, etc.)
- DataLoaders implement the `getByIds` method to efficiently fetch multiple entities in a single query
- Results are cached within a request to avoid duplicate queries

This approach significantly improves performance when resolving nested relationships in GraphQL queries.

## Maintenance & Evolution Guide

### Adding a New Entity

To add a new entity to the system:

1. **Domain Layer**:
   - Create the entity class in `domain/entities/`
   - Define the repository interface in `domain/repositories/`

2. **Database Layer**:
   - Create the DAO and Table in `infrastructure/data/dao/`
   - Implement mappers in `infrastructure/data/mappers/`
   - Implement the repository in `infrastructure/data/repositories/`
   - Add database migration in `resources/db/migration/`

3. **Service Layer**:
   - Create a service class in `services/`

4. **GraphQL Layer**:
   - Create GraphQL type in `infrastructure/graphql/types/`
   - Create input types in `infrastructure/graphql/inputs/`
   - Create query service in `infrastructure/graphql/queries/`
   - Create mutation service in `infrastructure/graphql/mutations/`
   - Create DataLoader in `infrastructure/graphql/dataloaders/`

5. **Dependency Injection**:
   - Register components in `infrastructure/di/Modules.kt`
   - Register GraphQL components in `infrastructure/graphql/GraphQLModule.kt`

### Adding a New Query

To add a new query:

1. Add the query method to the appropriate service class
2. Add the query resolver to the appropriate query service class
3. If needed, create new input types for filtering or pagination

### Adding a New Mutation

To add a new mutation:

1. Add the mutation method to the appropriate service class
2. Create input types for the mutation in `infrastructure/graphql/inputs/`
3. Add the mutation resolver to the appropriate mutation service class

### Modifying an Existing Entity

When modifying an existing entity:

1. Update the entity class in the domain layer
2. Update the DAO and Table in the database layer
3. Update mappers if needed
4. Add a database migration for schema changes
5. Update GraphQL types and resolvers

### Testing

When making changes, ensure:

1. Unit tests cover the new or modified functionality
2. Integration tests verify the end-to-end behavior
3. GraphQL queries and mutations work as expected

## GraphQL API Usage

The project exposes a GraphQL API that allows clients to interact with the system. Below are some general patterns for using the API:

### Query Patterns

- **Fetching a single entity by ID**: Most entities can be fetched by their UUID
- **Fetching collections**: Collections of entities can be fetched, often with filtering options
- **Nested relationships**: GraphQL allows fetching related entities in a single query

### Mutation Patterns

- **Creating entities**: Mutations are available to create new entities
- **Updating entities**: Existing entities can be updated via mutations
- **Deleting entities**: Entities can be removed from the system

### Using DataLoaders

The API uses DataLoaders to efficiently load related entities. This means:

1. When you request nested relationships (e.g., a playlist's songs), the system batches these requests
2. If the same entity is requested multiple times in a query, it's only fetched once from the database
3. This significantly improves performance for complex queries with many relationships

### Testing the API

You can explore and test the API using GraphQL Playground, which is available when running the application:

1. Start the application with `./gradlew run`
2. Open a browser and navigate to `http://localhost:8080/graphql`
3. Use the interactive playground to explore the schema and execute queries

The playground provides documentation of all available queries, mutations, and types.
